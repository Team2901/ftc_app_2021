package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.BaseUltimateGoalHardware;

import java.util.List;

import static org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.ScrimmageUltimateGoalHardware.ELEMENT_SINGLE;
import static org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.ScrimmageUltimateGoalHardware.ELEMENT_QUAD;

/*
 Strafe further done
 slow turns done
 Reset arm done
 Get speed done
 Separate forwards/center speeds done
 change the plan
 */

public class BaseUltimateGoalAuto extends LinearOpMode {

    public final TeamColor teamColor;

    public BaseUltimateGoalHardware robot = BaseUltimateGoalHardware.create();
    public int starterStackResult = -1;
    public double forwardMotorPower = .75;
    boolean debugging = false;

    public BaseUltimateGoalAuto(TeamColor teamColor) {
        super();
        this.teamColor = teamColor;
    }

    @Override
    public void runOpMode() throws InterruptedException {

    }

    public void init(boolean camera) {
        robot.init(this.hardwareMap);
        if (camera) {
            initAndActivateWebCameraWithTensorFlow();
        }
    }

    /**
     * Scans that starter stack to see the number of elements in front
     * @return 1 if there is one element, 2 if there are 4 elements, and 0 if there are none
     */
    public int starterStackSensor() {
        int stackID = 0;
        double confidence = 0.0;
        if (robot.webCamera.tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.

            ElapsedTime timer = new ElapsedTime();

            while (true) {
                if (!opModeIsActive()) {
                    break;
                }
                if (stackID > 0){
                    break;
                }
                if(timer.seconds() > 1){
                    break;
                }
                List<Recognition> updatedRecognitions = robot.webCamera.tfod.getRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        if (stackID != 2 && recognition.getLabel().equals(ELEMENT_SINGLE) && recognition.getConfidence() > 0.8) {
                            stackID = 1;
                            confidence = recognition.getConfidence();
                        } else if (recognition.getLabel().equals(ELEMENT_QUAD) && recognition.getConfidence() > 0.8) {
                            stackID = 2;
                            confidence = recognition.getConfidence();
                        }
                    }
                    telemetry.addData("stackID", stackID);
                    telemetry.addData("confidence", confidence);
                    telemetry.update();
                }
            }
        }
        robot.webCamera.deactivateTfod();
        return stackID;
    }

    public void grabWobble() {
        robot.configureWobbleGrabber(true);
    }

    public void releaseWobble() {
        robot.configureWobbleGrabber(false);
    }

    public void turnToDesiredAngle(float desiredAngle) {
        // Robot's current angle
        float robotAngle = robot.getAngle();

        // Determine the speed that the motors should be set to.
        double speed = robot.getMotorTurnSpeed(desiredAngle, robotAngle);
        speed = speed * robot.getForwardSpeed(3);

        // The robot should keep on turning until it reaches its desired angle.
        while (speed != 0 && opModeIsActive()) {

            if (speed > 0){
                speed = Math.max(.02, speed);
            }
            else {
                speed = Math.min(-.02, speed);
            }

            // Set the motors to their appropriate powers.
            robot.leftMotor.setPower(-speed);
            robot.rightMotor.setPower(speed);

            // Print out what the speed is.
            telemetry.addData("Speed", speed);

            // This prints out what the angle difference is.
            telemetry.addData("Angle difference", AngleUnit.normalizeDegrees(desiredAngle - robotAngle));

            // This updates our telemetry to continuously print out our telemetry.
            telemetry.update();

            // Update robot angle.
            robotAngle = robot.getAngle();

            // Update speed variable.
            speed = robot.getMotorTurnSpeed(desiredAngle, robotAngle) * robot.getForwardSpeed(3);
        }

        // We don't want the robot to turn anymore; therefore, we set the motors' powers to 0.
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
    }

    public void moveInchesCenter(double inches) {
        int direction = (TeamColor.RED_TEAM == this.teamColor) ? 1 : -1;
        int ticks = (int) (inches * robot.centerTicksPerInch * direction);
        double cruisingSpeed = robot.getStrafeSpeed(2);
        double distanceTraveled = 0;
        double minSpeed = .02;
        double startSlope = 1.0 / 10.0;
        double endSlope = 1.0 / 15.0;
        double startAngle = robot.getAngle();

        /*
        cruisingSpeed = 1;
        if(inches < 60){
            cruisingSpeed = cruisingSpeed * (inches / 60);
        }
         */

        robot.middleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int targetPosition = robot.middleMotor.getCurrentPosition() + ticks;

        robot.middleMotor.setTargetPosition(targetPosition);

        robot.middleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive() && (robot.middleMotor.isBusy())) {
            double distanceRemaining = Math.abs(inches) - distanceTraveled;
            //double sinRampSpeed = Math.sin(distanceTraveled/inches * Math.PI) * cruisingSpeed + minSpeed;
            double rampUpSpeed = Math.abs(distanceTraveled * startSlope) + minSpeed;
            double rampDownSpeed = Math.abs(distanceRemaining * endSlope) + minSpeed;

            double motorSpeed = Math.min(cruisingSpeed, Math.min(rampDownSpeed, rampUpSpeed));

            robot.middleMotor.setPower(motorSpeed);

            double angleTuning = pidTune(startAngle, robot.getAngle());

            robot.leftMotor.setPower(angleTuning);
            robot.rightMotor.setPower(-angleTuning);

            telemetry.addData("stackID", starterStackResult);
            telemetry.addData("Current Middle Position", robot.middleMotor.getCurrentPosition()); // moving towards neg
            telemetry.addData("Target in Ticks", ticks); // 1701
            telemetry.addData("Target Position", targetPosition); // 1701
            telemetry.addData("motor Speed", motorSpeed); // 0.979

            // power positive =  increasing
            // power negative = decreasing

            telemetry.update();

            distanceTraveled = Math.abs(robot.middleMotor.getCurrentPosition() / robot.centerTicksPerInch);
        }
        robot.middleMotor.setPower(0);

        robot.middleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public void moveInchesForward(double inches, boolean correctingRun) {
        int ticks = (int) (inches * robot.forwardTicksPerInch);
        double startAngle = robot.getAngle();
        double angleTuning = 0;
        double cruisingSpeed = 1;
        double distanceTraveled = 0;
        double minSpeed = .05;
        double startSlope = 1.0 / 15.0;
        double endSlope = 1.0 / 30.0;

        /*
        cruisingSpeed = 1;
        if(inches < 60){
            cruisingSpeed = cruisingSpeed * (inches / 60);
        }
         */

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftMotor.setTargetPosition(ticks);
        robot.rightMotor.setTargetPosition(ticks);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive() && (robot.leftMotor.isBusy() || robot.rightMotor.isBusy())) {
            double distanceRemaining = Math.abs(inches) - distanceTraveled;
            //double sinRampSpeed = Math.sin(distanceTraveled/inches * Math.PI) * cruisingSpeed + minSpeed;
            double rampUpSpeed = Math.abs(distanceTraveled * startSlope);
            double rampDownSpeed = Math.abs(distanceRemaining * endSlope);

            // The speed the motors are being set to which increases the farther away we are from
            // our starting position (rampUp) and decreases the closer we are to the target position (rampDown).
            double motorSpeed = Math.min(cruisingSpeed, Math.min(rampDownSpeed, rampUpSpeed));

            // Corrects the path of the robot if correctingRun is true
            if (correctingRun) {
                angleTuning = pidTune(startAngle, robot.getAngle());
            }

            double leftPower = Math.max(angleTuning + motorSpeed, minSpeed);
            double rightPower = Math.max(-angleTuning + motorSpeed, minSpeed);

            robot.leftMotor.setPower(leftPower);
            robot.rightMotor.setPower(rightPower);

            telemetry.addData("Adjusting:", angleTuning);
            telemetry.addData("stackID", starterStackResult);
            telemetry.addData("Current Left Position", robot.leftMotor.getCurrentPosition());
            telemetry.addData("Current Right Position", robot.rightMotor.getCurrentPosition());
            telemetry.addData("Left Motor Power", leftPower);
            telemetry.addData("Right Motor Power", rightPower);
            telemetry.addData("Target Ticks", ticks);
            telemetry.update();

            // Updates how far the motor has traveled
            distanceTraveled = Math.abs(robot.leftMotor.getCurrentPosition() / robot.forwardTicksPerInch);

            if(distanceTraveled >= inches && inches > 0){
                break;
            }

            if(distanceTraveled <= inches && inches < 0){
                break;
            }
        }

        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    // a^2 + b^2 = c^2
    // total inches traveled = c
    public void moveInchesDiagonal(double inchesForward, double inchesCenter, boolean correctingRun) {
        int ticksForward = (int) (inchesForward * robot.forwardTicksPerInch);
        int ticksCenter = (int) (inchesCenter * robot.centerTicksPerInch);
        double startAngle = robot.getAngle();
        double angleTuning = 0;
        double cruisingSpeed = robot.getForwardSpeed(2);
        double distanceTraveledForward = 0;
        double distanceTraveledCenter = 0;
        double minSpeed = .02;
        double startSlope = 1.0 / 20.0;
        double endSlope = 1.0 / 30.0;

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.middleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftMotor.setTargetPosition(ticksForward);
        robot.rightMotor.setTargetPosition(ticksForward);
        robot.middleMotor.setTargetPosition(ticksCenter);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.middleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive() && (robot.leftMotor.isBusy() || robot.rightMotor.isBusy()
                || robot.middleMotor.isBusy())) {
            double distanceRemainingForward = Math.abs(inchesForward) - distanceTraveledForward;
            double rampUpSpeedForward = Math.abs(distanceTraveledForward * startSlope) + minSpeed;
            double rampDownSpeedForward = Math.abs(distanceRemainingForward * endSlope) + minSpeed;
            double motorSpeedForward = Math.min(cruisingSpeed, Math.min(rampDownSpeedForward, rampUpSpeedForward));

            double distanceRemainingCenter = Math.abs(inchesCenter) - distanceTraveledCenter;
            double rampUpSpeedCenter = Math.abs(distanceTraveledCenter * startSlope) + minSpeed;
            double rampDownSpeedCenter = Math.abs(distanceRemainingCenter * endSlope) + minSpeed;
            double motorSpeedCenter = Math.min(cruisingSpeed, Math.min(rampDownSpeedCenter, rampUpSpeedCenter));

            if (correctingRun) {
                angleTuning = pidTune(startAngle, robot.getAngle());
            }

            robot.leftMotor.setPower(angleTuning + motorSpeedForward);
            robot.rightMotor.setPower(-angleTuning + motorSpeedForward);
            robot.middleMotor.setPower(motorSpeedCenter);

            telemetry.addData("Adjusting:", angleTuning);
            telemetry.addData("stackID", starterStackResult);
            telemetry.addData("Current Left Position", robot.leftMotor.getCurrentPosition());
            telemetry.addData("Current Right Position", robot.rightMotor.getCurrentPosition());
            telemetry.addData("Current Middle Position", robot.middleMotor.getCurrentPosition());
            telemetry.addData("Target in Ticks", ticksForward);
            telemetry.addData("Motor Speed Forward", motorSpeedForward);
            telemetry.addData("Motor Speed Center", motorSpeedCenter);
            telemetry.update();

            distanceTraveledForward = Math.abs(robot.leftMotor.getCurrentPosition() / robot.forwardTicksPerInch);
        }

        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    //PID tuning to keep straight line driving on track
    public double pidTune(float startingAngle, float currentAngle) {
        double correction = pidTuneOverflow((double) startingAngle, (double) currentAngle);
        return correction;
    }

    public double pidTune(double startingAngle, float currentAngle) {
        double correction = pidTuneOverflow((double) startingAngle, (double) currentAngle);
        return correction;
    }

    public double pidTune(float startingAngle, double currentAngle) {
        double correction = pidTuneOverflow((double) startingAngle, (double) currentAngle);
        return correction;
    }

    public double pidTune(double startingAngle, double currentAngle) {
        double correction = pidTuneOverflow((double) startingAngle, (double) currentAngle);
        return correction;
    }

    /**
     * a single PID overflow so that we only have to change the value in one place
     * @param startingAngle what the program wants the robot's angle is
     * @param currentAngle the current angle the robot is actually facing
     * @return correction which is proportional to the angle difference
     */
    public double pidTuneOverflow(double startingAngle, double currentAngle) {
        double correction = (currentAngle - startingAngle) / 100;
        return correction;
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

    public enum TeamColor {
        RED_TEAM, BLUE_TEAM
    }

    public void safeWait(int millis){
        ElapsedTime timer = new ElapsedTime();

        while(opModeIsActive() && timer.milliseconds() < millis){

        }
    }

    //will this work
    public void ringShot(int num){
        telemetry.addData("Shooting Rings: ", num);
        for(int i = 0; i < num; i++) {
            robot.kicker.setPosition(robot.KICKER_MAX);
            safeWait(500);
            robot.kicker.setPosition(robot.KICKER_MIN);
            safeWait(250);
        }
    }

    /**
     * If debugging is true, waits for the driver to press the a button.
     * While a has not been pressed, dpad up sets the forwardMotorPower to 0.75 while dpad down sts the forwardMotorPower to 0.5
     */
    public void nextStep(String stepName) {
        RobotLog.i("Next Step:" + stepName);
        if (debugging) {
            telemetry.addData("Next Step", stepName);
            telemetry.addData("Press A if you want it to keep going", "");
            telemetry.update();
            while (!gamepad1.a && opModeIsActive()) {

            }
        }
    }
}
