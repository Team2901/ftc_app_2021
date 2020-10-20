package org.firstinspires.ftc.teamcode.Autonomous.PastGames;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Hardware.CompetitionSkystoneHardware;
import org.firstinspires.ftc.teamcode.Hardware.ExemplaryBlinkinLED;
import org.firstinspires.ftc.teamcode.Utility.AngleUtilities;

import java.util.List;

import static org.firstinspires.ftc.teamcode.Hardware.BaseSkyStoneHardware.LABEL_SKY_BUTTER;

@SuppressLint("DefaultLocale")
public abstract class BaseSkyStoneAuto extends LinearOpMode {

    public static final int GO_TO_ANGLE_BUFFER = 7;
    public static final int SAFE_WALL_DISTANCE_INCHES = 2;
    public static final int PARK_BRIDGE_INCHES = 30;
    final static int CONFIDENCE_PERCENTAGE = 5;
    public CompetitionSkystoneHardware robot = new CompetitionSkystoneHardware();

    public void turnTo(double angle) {
        turnTo(angle, .5, 0 , 0);
    }

    public void turnTo(double angle, double power) {
        turnTo(angle, power, 0, 0);
    }

    public void turnTo(double angle, double power, double tolerance, int milliseconds) {

        if (Math.abs(AngleUtilities.getNormalizedAngle(robot.getAngle() - angle)) > tolerance) {
            robot.wait(milliseconds, this);
            goToAngle(robot.getAngle(), angle, power);
            robot.wait(milliseconds, this);
        }
    }

    public void moveInches(double angle, double inches, double power) {

        robot.swerveStraight(angle, 0);
        robot.setWheelMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setWheelMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.setWheelMotorMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.setWheelTargetPositions((int) (inches * robot.inchesToEncoder));

        robot.swerveStraight(angle, power);
        while (robot.wheelsAreBusy() && opModeIsActive()) {
            telemetry.addData("FL", String.format("angle: %.2f, mod: %d, pos: %d",
                    robot.frontLeftSwerveWheel.targetAngle, robot.frontLeftSwerveWheel.modifier, robot.frontLeft.getCurrentPosition()));
            telemetry.addData("FR", String.format("angle: %.2f, mod: %d, pos: %d",
                    robot.frontRightSwerveWheel.targetAngle, robot.frontRightSwerveWheel.modifier, robot.frontRight.getCurrentPosition()));
            telemetry.addData("BL", String.format("angle: %.2f, mod: %d, pos: %d",
                    robot.backLeftSwerveWheel.targetAngle, robot.backLeftSwerveWheel.modifier, robot.backLeft.getCurrentPosition()));
            telemetry.addData("BR", String.format("angle: %.2f, mod: %d, pos: %d",
                    robot.backRightSwerveWheel.targetAngle, robot.backRightSwerveWheel.modifier, robot.backRight.getCurrentPosition()));

            telemetry.update();
        }

        robot.setWheelMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setWheelMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.swerveStraight(angle, 0);
    }

    public void moveInchesAbsolute(double angle, double inches, double power) {

        robot.swerveStraightAbsolute(angle, 0);

        robot.setWheelMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setWheelMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.setWheelMotorMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.setWheelTargetPositions((int) (inches * robot.inchesToEncoder));

        robot.swerveStraightAbsolute(angle, power);
        while (robot.wheelsAreBusy() && opModeIsActive()) {
            boolean setServoPosition = robot.swerveStraightAbsolute(angle, power, true);
            telemetry.addData("FL", String.format("angle: %.2f, mod: %d, pos: %d",
                    robot.frontLeftSwerveWheel.targetAngle, robot.frontLeftSwerveWheel.modifier, robot.frontLeft.getCurrentPosition()));
            telemetry.addData("FR", String.format("angle: %.2f, mod: %d, pos: %d",
                    robot.frontRightSwerveWheel.targetAngle, robot.frontRightSwerveWheel.modifier, robot.frontRight.getCurrentPosition()));
            telemetry.addData("BL", String.format("angle: %.2f, mod: %d, pos: %d",
                    robot.backLeftSwerveWheel.targetAngle, robot.backLeftSwerveWheel.modifier, robot.backLeft.getCurrentPosition()));
            telemetry.addData("BR", String.format("angle: %.2f, mod: %d, pos: %d",
                    robot.backRightSwerveWheel.targetAngle, robot.backRightSwerveWheel.modifier, robot.backRight.getCurrentPosition()));
            telemetry.addData("Set Servo Position", setServoPosition);

            telemetry.update();
        }

        robot.setWheelMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setWheelMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.swerveStraightAbsolute(angle, 0);
    }

    public void goToAngle(double angleStart, double angleGoal, double power) {

        robot.swerveTurn(0);

        double angleCurrent = angleStart;

        while ((Math.abs(AngleUtilities.getNormalizedAngle(angleGoal - angleCurrent)) > GO_TO_ANGLE_BUFFER) && opModeIsActive()) {
            angleCurrent = robot.getAngle();
            double powerCurrent = robot.getCurrentTurnPower(angleCurrent, angleGoal, angleStart, power);
            robot.swerveTurn(powerCurrent);

            telemetry.addData("Start Angle ", "%.1f", angleStart);
            telemetry.addData("Goal Angle  ", "%.1f", angleGoal);
            telemetry.addData("Cur Angle   ", "%.1f", angleCurrent);
            telemetry.addData("Remain Angle", "%.1f", AngleUnit.normalizeDegrees(angleGoal - angleCurrent));
            telemetry.addData("Power       ", "%.2f", powerCurrent);
            telemetry.update();

        }
        robot.swerveTurn(0);
        telemetry.addData("Is Stopped", "");
        telemetry.update();
    }

    public void platformParkInner(int team) {
        double colorAngle;
        if (team == Color.RED) {
            colorAngle = robot.ROBOT_LEFT_ANGLE;
        } else {
            colorAngle = robot.ROBOT_RIGHT_ANGLE;

        }
        //Prequel Step 1 clear wall by 2 inches, center on waffle

        moveInchesAbsolute(0, SAFE_WALL_DISTANCE_INCHES, .5);
        moveInchesAbsolute(colorAngle,12, .5);

        //Step one: turn wheels 90 degrees counterclockwise and go forward 28.5 inches and lower grabbers.
        moveInchesAbsolute(colorAngle, 26.5, 0.5);
        robot.rightGrabber.setPosition(robot.GRABBER_MAX);
        robot.leftGrabber.setPosition(robot.GRABBER_MAX);
        ElapsedTime timer = new ElapsedTime();
        while (timer.milliseconds() < 500) ;

        //Step two: back up 26 inches and raise grabbers.
        moveInchesAbsolute(colorAngle, -26, 0.5);
        robot.rightGrabber.setPosition(robot.GRABBER_MIN);
        robot.leftGrabber.setPosition(robot.GRABBER_MIN);
        //Step three: turn wheels 90 degrees counterclockwise and slide out 2 ft.
        moveInchesAbsolute(robot.ROBOT_FRONT_ANGLE, -24, 0.5);
        //Step four: turn wheels to position 90, move forwards 2 ft
        moveInchesAbsolute(colorAngle, 24, 0.5);
        //Step Five: Turn Wheels to 0, move forward 2 ft
        moveInchesAbsolute(robot.ROBOT_FRONT_ANGLE, -24, 0.5);
        //Step  Six: Stop
    }

    public Float findSkyStone() {

        Float centerPercentDifference = null;
        float stonePercentLocation = 0;
        if (robot.webCamera.tfod == null) {
            return (0.0f);
        }

        List<Recognition> updatedRecognitions = robot.webCamera.tfod.getRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("# Object Detected", updatedRecognitions.size());
            // step through the list of recognitions and display boundary info.
            int i = 0;
            for (Recognition recognition : updatedRecognitions) {
                //If x > 380, the skystone is in position three. (Three away from the edge) If x > 620 it is at position 2, and if x > 350 it is in position 1
                if (!recognition.getLabel().equals(LABEL_SKY_BUTTER)) {
                    continue;
                }

                telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                        recognition.getLeft(), recognition.getTop());
                telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                        recognition.getRight(), recognition.getBottom());
                int centerFrame = recognition.getImageWidth() / 2;
                float centerSkyStone = (recognition.getRight() + recognition.getLeft()) / 2;
                telemetry.addData("Center Frame", centerFrame);
                telemetry.addData("Center Stone", centerSkyStone);
                float centerDifference = centerSkyStone - centerFrame;
                telemetry.addData("Difference", centerDifference);
                centerPercentDifference = (centerDifference / centerFrame) * 100;
                telemetry.addData("Percent Difference", centerPercentDifference);
                stonePercentLocation = (centerSkyStone / recognition.getImageWidth() * 100);

            }
        }
        telemetry.addData("Skystones Location Debugger", stonePercentLocation);
        telemetry.update();
        return centerPercentDifference;
    }

    public void initAndActivateWebCameraWithTensorFlow() {

        // Init the web camera with TensorFlow
        robot.initWebCamera(hardwareMap);
        robot.initTfod();

        // Activate TensorFlow
        robot.webCamera.activateTfod();

        // Check for errors
        if (robot.webCamera.hasError()) {
            telemetry.addData("Failed!", robot.webCamera.errorMessage);
        } else {
            telemetry.addData("Successful!", "");
        }
    }

    public void scanForSkystone(double towardsQuarryAngle) {
            //robot.crane.setPosition(1);
            //robot.wrist.setPosition(.5);
            // Save the robot's current position prior to search for a skystone
            robot.setWheelMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.setWheelMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // Step 1) Move forwards/backwards until a skystone location is within 10% of the center of the camera's view
            Float skyStoneCenterPercentDiff = findSkyStone();
            Float skyStoneOffsetPercentDiff = skyStoneCenterPercentDiff == null ? null : skyStoneCenterPercentDiff + 45;

            while (skyStoneOffsetPercentDiff == null /* don't see skystone yet */
                    || Math.abs(skyStoneOffsetPercentDiff) > CONFIDENCE_PERCENTAGE /* overshot or undershot */) {
                telemetry.addData("loop is running", "");
                telemetry.addData("percent dif.", skyStoneCenterPercentDiff);
                telemetry.addData("percent offset", skyStoneOffsetPercentDiff);
                telemetry.update();

                if (skyStoneOffsetPercentDiff == null) {
                    // If we don't see a skystone: Move forwards
                    robot.swerveStraightAbsolute(towardsQuarryAngle, 0.2);
                } else if (skyStoneOffsetPercentDiff < 0) {
                    // If the skystone is to the left: Move backwards
                    robot.swerveStraightAbsolute(towardsQuarryAngle, 0.3);
                } else {
                    // If the skystone is to the right: Move forwards
                    robot.swerveStraightAbsolute(towardsQuarryAngle, -0.3);
                }

                // Update the skystone location
                skyStoneCenterPercentDiff = findSkyStone();
                skyStoneOffsetPercentDiff = skyStoneCenterPercentDiff == null ? null : skyStoneCenterPercentDiff + 45;
            }

            robot.swerveStraightAbsolute(0, 0);

            turnTo(0, .5, 5, 1000);

            telemetry.addData("out of loop", "");
            telemetry.addData("percent dif.", skyStoneCenterPercentDiff);
            telemetry.addData("percent offset", skyStoneOffsetPercentDiff);
            telemetry.update();
    }

    public void quarrySkyStoneParkBridge(boolean isRed, boolean scanForSkystone, boolean placeStoneOnWaffle) {

        /**
         *  Steps
         *  0) initialize robot and web camera with TensorFlow, point wheels forward
         *  1) optional: Move forwards/backwards until a skystone location is within 10% of the center of the camera's view
         *  3) Open the jaw
         *  4) Move forwards 2 feet towards the skystone
         *  5) Close the jaw on the skystone
         *  6) Move backwards 2 feet away from the skystone
         *  8) Move back to where we were in step 1
         *  9) Move forwards to in front of the waffle
         *  10) deposit skystone on waffle
         *  11) Park under the skybridge
         */

        this.moveInchesAbsolute(0, 5, .2);

        double towardsQuarryAngle = isRed? 90 : -90;
        double towardsFoundationAngle = isRed? -90 : 90;

        robot.swerveStraightAbsolute(towardsQuarryAngle, 0);

        double fLeftStart = Math.abs(robot.frontLeft.getCurrentPosition());

        if (scanForSkystone) {
            scanForSkystone(towardsQuarryAngle);
        }

        // Calculate in inches how far the robot has moved while finding the skystone
        double fLeftEnd = Math.abs(robot.frontLeft.getCurrentPosition());
        double diff = Math.abs(fLeftEnd - fLeftStart);
        double diffInches = diff / robot.inchesToEncoder;

        // Step 2) Turn to face the skystone
        //turnTo(0, .2);

        // Step 3) Open the jaw
        robot.jaw.setPosition(robot.OPEN_JAW);
        //robot.crane.setPosition(1);

        // Step 4) Move forwards 2 feet towards the skystone
        this.moveInchesAbsolute(0, 32, .2);

        // Step 5) Close the jaw on the skystone
        robot.jaw.setPosition(robot.CLOSED_JAW);

        // Step 6) Move backwards 15 inches away from the skystone
        this.moveInchesAbsolute(0, -15, .2);

        //turnTo(0, .5, 5, 1000);

        // Step 8) Move back to where we were in step 1

        this.moveInchesAbsolute(towardsFoundationAngle, diffInches, .3);

        if (placeStoneOnWaffle) {
            //turnTo(0, .5, 5, 1000);

            // Step 9) Move forwards to in front of the waffle
            this.moveInchesAbsolute(towardsFoundationAngle, 72, .3);

            // Step 10) deposit skystone on waffle

            //robot.moveLift(50 );

            moveInchesAbsolute(0, 28, .3);

            robot.jaw.setPosition(robot.OPEN_JAW);

            moveInchesAbsolute(0, -22, .3);

            // Step 11) Park under the skybridge

            moveInchesAbsolute(towardsQuarryAngle, 40, .3);

        } else {

            // Step 9) Move forwards to foundation side
            moveInchesAbsolute(towardsFoundationAngle, 48, .3);

            // Step 10) deposit skystone on foundation
            robot.jaw.setPosition(robot.OPEN_JAW);

            // Step 11) Park under the skybridge
            moveInchesAbsolute(towardsQuarryAngle, 36, .3);
        }

        while (opModeIsActive()) {
        }
    }

    public void init(boolean initWebCam, boolean initRobot, boolean initBlinkinLED, boolean driveWheels, boolean setLiftServos, String message, int teamColor) {
        if (initWebCam) {
            // Init web camera with TensorFlow
            initAndActivateWebCameraWithTensorFlow();
        }

        if(initRobot){
            robot.init(hardwareMap);

            if (setLiftServos) {
                robot.crane.setPosition(0);
                robot.wrist.setPosition(.5);
                robot.setGrabberPositition(.7, .84);
            }

            if (driveWheels) {
                robot.swerveStraightAbsolute(0, 0);
            }
        }

        if(initBlinkinLED){
            robot.initBlinkinLED(hardwareMap);
        }

        robot.blinkinLED.color = teamColor;
        robot.blinkinLED.setTeamPattern(ExemplaryBlinkinLED.TeamColorPattern.LARSON_SCANNER);
        robot.writeTeamColor();
        if (message != null) {
            telemetry.addData(">", message);
        }

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
    }

    public void park(int forwardInches, double directionAngle){
        this.moveInchesAbsolute(0.0, forwardInches, .2);

        robot.wait(1000, this);
        robot.swerveStraight(directionAngle, 0);

        robot.wait(1000, this);
        this.moveInchesAbsolute(directionAngle,24, .2);
    }
}

