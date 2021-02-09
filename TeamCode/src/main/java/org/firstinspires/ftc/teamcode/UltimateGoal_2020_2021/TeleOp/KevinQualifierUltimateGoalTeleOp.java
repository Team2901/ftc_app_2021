package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.TeleOp;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.BaseUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.Utility.CountDownTimer;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;
import org.firstinspires.ftc.teamcode.Utility.MatrixHelper;

import java.io.IOException;
import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities.MM_TO_INCHES;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Kevin Qualifier UltimateGoal", group = "2021_UltimateGoal")
public class KevinQualifierUltimateGoalTeleOp extends OpMode {
    // Relative to you
    public static final int ABSOLUTE_MODE = 0;
    // Relative to front of robot
    public static final int RELATIVE_MODE = 1;
    public BaseUltimateGoalHardware robot = BaseUltimateGoalHardware.create();
    public int currentMode = RELATIVE_MODE;
    public boolean isIntakeOn = false;
    public boolean wobbleOverride = false;
    ImprovedGamepad impGamepad1;
    ImprovedGamepad impGamepad2;
    ElapsedTime timer = new ElapsedTime();
    CountDownTimer countDownTimer = new CountDownTimer(ElapsedTime.Resolution.MILLISECONDS);
    double turnPowerRatio = 1;
    double movePowerRatio = 1;
    double shooterPowerRatio = 0.8;
    double intakePowerRatio = 0.9;
    boolean pauseShooterMode; //Stealth Mode
    double kickerPosition = 0.65;
    int shooterOffset = 5; //Offset launch angle
    int wobbleElbowMinPosition = 0;
    int wobbleElboxMaxPosition = 5626;
    boolean cameraLoaded = true;

    ArrayList<String> logMessages = new ArrayList<>();
    ElapsedTime timestampTimer = new ElapsedTime();

    @Override
    public void init() {
        // Instantiate gamepads
        impGamepad1 = new ImprovedGamepad(this.gamepad1, this.timer, "GP1");
        impGamepad2 = new ImprovedGamepad(this.gamepad2, this.timer, "GP2");

        robot.init(this.hardwareMap);

        int cameraRotationX = 0;
        String errorMessage = robot.initWebCamera(this.hardwareMap);

        if(errorMessage != null) {
            cameraLoaded = false;
        }

        // Loading the Vuforia trackables.
        robot.webCamera.loadVuforiaTrackables("UltimateGoal");

        // Sets up the position of the Vuforia web image and web camera.
        OpenGLMatrix webcamLocation = OpenGLMatrix.translation(-6/MM_TO_INCHES, -0.5f/MM_TO_INCHES, 0).multiplied(
                Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZY, AngleUnit.DEGREES,
                        90, 90, 0));

        // Sets up vuforia trackables.
        robot.webCamera.ultimateGoalSetupTrackables(webcamLocation);

        // We are ready to use the trackables and this gives us the opportunity to deactivate them later.
        robot.webCamera.activateVuforiaTrackables();

        telemetry.addData("Camera loaded", cameraLoaded);

        /*
        Failed Hardware: 2
        1: shooter_motor
        2: intake_motor

        Failed Hardware: 0
         */
        telemetry.addData("Failed Hardware", robot.failedHardware.size());
        for (int i = 0; i < robot.failedHardware.size(); i++) {
            telemetry.addData(String.valueOf(i + 1), robot.failedHardware.get(i));
        }

        telemetry.addData("Current Hardware", robot.hardwareClassName);

        telemetry.update();
    }

    @Override
    public void loop() {
        impGamepad1.update();
        impGamepad2.update();

        float rightStickX = gamepad1.right_stick_x;
        float rightStickY = -1 * gamepad1.right_stick_y;
        double rightStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(rightStickY, rightStickX));
        double rightStickRadius = Math.hypot(rightStickX, rightStickY);

        float leftStickX = gamepad1.left_stick_x;
        float leftStickY = -1 * gamepad1.left_stick_y;
        double leftStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(leftStickY, leftStickX));
        double leftStickRadius = Math.hypot(leftStickX, leftStickY);

        float robotAngle = robot.getAngle();

        /*
         * isIntakeOn - Press the b button (g1) to toggle isIntakeOn off and on
         */
        if (impGamepad1.b.isInitialPress()) {
            isIntakeOn = !isIntakeOn;
        }

        /*
         * wobbleOverride - Press the a button (g1) to toggle wobbleOverride off and on
         */
        if (impGamepad1.a.isInitialPress()) {
            wobbleOverride = !wobbleOverride;
        }

        /*
         * pauseShooterMode - Press the x button (g1) to toggle pauseShooterMode off and on
         */
        if (impGamepad1.x.isInitialPress()) {
            pauseShooterMode = !pauseShooterMode;
        }

        /*
         * currentMode - Press the y button (g1) to toggle between absolute and relative move
         */
        if (impGamepad1.y.isInitialPress()) {
            if (currentMode == ABSOLUTE_MODE) {
                currentMode = RELATIVE_MODE;
            } else {
                currentMode = ABSOLUTE_MODE;
            }
        }

        /*
         * Intake and Transfer motors
         *
         * Hold left trigger (g2) to move the intake/transfer inwards.
         * Hold right trigger (g2) to move the intake/transfer outwards.
         * Otherwise, use the isIntakeOn flag to either move inwards or not move at all.
         */
        if (gamepad2.left_trigger > 0.5) {
            // Hold left trigger to move the intake/transfer inwards.
            robot.intakeMotor.setPower(intakePowerRatio);
            robot.transferMotor.setPower(intakePowerRatio);
        } else if (gamepad2.right_trigger > 0.5) {
            // Hold right trigger to move the intake/transfer outwards.
            robot.intakeMotor.setPower(-intakePowerRatio);
            robot.transferMotor.setPower(-intakePowerRatio);
        } else {
            // Otherwise, look at the intake flag
            if (isIntakeOn) {
                // if isIntakeOn is toggled on then move the intake/transfer inwards
                robot.intakeMotor.setPower(intakePowerRatio);
                robot.transferMotor.setPower(intakePowerRatio);
            } else {
                // else (isIntakeOn is toggled off) don't move
                robot.intakeMotor.setPower(0);
                robot.transferMotor.setPower(0);
            }
        }

        /*
         * Kicker Servo
         *
         * Hold the right trigger (g1) to start the kicker timer for 1.5 seconds
         *
         * If the timer is running, extend the kicker, otherwise retract it
         */

        if (gamepad1.right_trigger > 0) {
            // Hold the right trigger (g1) to start the kicker timer for 1.5 seconds
            countDownTimer.setTargetTime(1500);
        }

        if (countDownTimer.hasRemainingTime()) {
            // If timer is running then extend the kicker
            robot.kicker.setPosition(.3);
        } else {
            // Else (timer has expired) retract the kicker
            robot.kicker.setPosition(.65);
        }

        /*
         * shooterPowerRatio
         *
         * Press dpad_up (g2) to increase the ratio by 0.1 (up to a max of 1)
         * Press dpad_down (g2) to decrease the ratio by 0.1 (down to a min of 0)
         */
        if (impGamepad2.dpad_up.isInitialPress() && shooterPowerRatio < 1) {
            // Press dpad_up (g2) to increase the ratio by 0.1 (up to a max of 1)
            shooterPowerRatio += 0.1;
        } else if (impGamepad2.dpad_down.isInitialPress() && shooterPowerRatio > 0) {
            // Press dpad_down (g2) to decrease the ratio by 0.1 (down to a min of 0)
            shooterPowerRatio -= 0.1;
        }

        /*
         * turnPowerRatio
         *
         * Press x (g2) to increase the ratio by 0.1 (up to a max of 1)
         * Press b (g2) to decrease the ratio by 0.1 (down to a min of 0)
         */
        if (impGamepad2.x.isInitialPress() && turnPowerRatio < 1) {
            // Press x (g2) to increase the ratio by 0.1 (up to a max of 1)
            turnPowerRatio += 0.1;
        } else if (impGamepad2.b.isInitialPress() && turnPowerRatio > 0) {
            // Press b (g2) to decrease the ratio by 0.1 (down to a min of 0)
            turnPowerRatio -= 0.1;
        }

        /*
         * movePowerRatio
         *
         * Press y (g2) to increase the ratio by 0.1 (up to a max of 1)
         * Press a (g2) to decrease the ratio by 0.1 (down to a min of 0)
         */
        if (impGamepad2.y.isInitialPress() && movePowerRatio < 1) {
            // Press y (g2) to increase the ratio by 0.1 (up to a max of 1)
            movePowerRatio += 0.1;
        } else if (impGamepad2.a.isInitialPress() && movePowerRatio > 0) {
            // Press a (g2) to decrease the ratio by 0.1 (down to a min of 0)
            movePowerRatio -= 0.1;
        }

        /*
         * intakePowerRatio
         *
         * Press dpad_left (g2) to increase the ratio by 0.1 (up to a max of 1)
         * Press dpad_right (g2) to decrease the ratio by 0.1 (down to a min of 0)
         */
        if (impGamepad2.dpad_left.isInitialPress() && intakePowerRatio < 1) {
            // Press dpad_left (g2) to increase the ratio by 0.1 (up to a max of 1)
            intakePowerRatio += .1;
        } else if (impGamepad2.dpad_right.isInitialPress() && intakePowerRatio > 0) {
            // Press dpad_right (g2) to decrease the ratio by 0.1 (down to a min of 0)
            intakePowerRatio -= .1;
        }

        double leftMotorPower = 0;
        double rightMotorPower = 0;
        double middleMotorPower = 0;

        /*
         * Move robot around the field (leftMotorPower, rightMotorPower, middleMotorPower)
         *
         * Hold the left stick (g1) in a given angle to make the robot move in that angle.
         * If in absolute mode, the joystick angle is the move angle relative to the field, else (in relative mode), it is relative to the robot
         *
         * The movement speed will be proportional to the joystick's push radius and movePowerRatio
         */

        // Calculate angle relative to field to move in
        double moveAngleAbsolute;
        if (currentMode == ABSOLUTE_MODE) {
            // If in absolute mode, the joystick angle is the move angle relative to the field
            moveAngleAbsolute = leftStickAngle + 180;
        } else {
            // else (in relative mode), it is relative to the robot (so convert it to be relative to the field)
            moveAngleAbsolute = (robotAngle + leftStickAngle) - 90;
        }

        // Calculate angle relative to the robot to move at
        double moveAngleRelative = moveAngleAbsolute - robotAngle;

        // Calculate forwards/sideways components to move at
        double xToMoveTo = Math.cos(Math.toRadians(moveAngleRelative));
        double yToMoveTo = Math.sin(Math.toRadians(moveAngleRelative));

        // Calculate forwards/sideways powers to move at
        leftMotorPower = leftStickRadius * xToMoveTo * movePowerRatio;
        rightMotorPower = leftStickRadius * xToMoveTo * movePowerRatio;
        middleMotorPower = leftStickRadius * yToMoveTo * movePowerRatio;

        /*
         * Turn the robot in place (leftMotorPower, rightMotorPower)
         *
         * Use the bumpers and right joystick (g1) to turn the robot to face a given angle
         * Hold left bumper to turn to 0 degrees
         * Hold right bumper to 180 degrees
         * If you turn the right joystick to the left, the robot turns counterclockwise, and vice versa.
         * If the left trigger is held down, the robot will face the tower goal image if it is
         * visible or turn to degree 0.
         * The turn speed will be proportional to turnPowerRatio.
         */

        // Gets the robot's location.
        OpenGLMatrix robotLocation = robot.webCamera.getRobotLocation();

        Double turnAngleAbsolute;
        if (gamepad1.left_bumper) {
            // Hold left bumper to turn to 0 degrees.
            turnAngleAbsolute = 0.0;
        } else if (gamepad1.right_bumper) {
            // Hold right bumper to turn to 180 degrees.
            turnAngleAbsolute = 180.0;
        } else if(gamepad1.left_trigger > 0.5){
                turnAngleAbsolute = robot.webCamera.getRobotTurnAngle(robotLocation, false);
        } else {
            // Else, don't turn
            turnAngleAbsolute = null;
        }

        if (turnAngleAbsolute != null) {
            turnAngleAbsolute += shooterOffset;
            // Determine the speed that the motors should be set to.
            double speed = robot.getMotorTurnSpeed(turnAngleAbsolute, robotAngle);

            // Set the motors to their appropriate powers.
            leftMotorPower = -speed * turnPowerRatio;
            rightMotorPower = speed * turnPowerRatio;
        } else if(gamepad1.right_stick_x > 0.1 || gamepad1.right_stick_x < -0.1) {
            double speed = gamepad1.right_stick_x;

            leftMotorPower = -speed * turnPowerRatio;
            rightMotorPower = speed * turnPowerRatio;
        }

        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);
        robot.middleMotor.setPower(middleMotorPower);

        /*
         * Wobble Grabber servo - Hold the dpad right to open or dpad left (g1) to close the grabber
         */
        if (gamepad1.dpad_right) {
            // Hold dpad_right to open the wobble grabber
            robot.configureWobbleGrabber(false);
        } else if (gamepad1.dpad_left) {
            // Hold dpad_left to close the wobble grabber
            robot.configureWobbleGrabber(true);
        }

        /*
         * Wobble Elbow motor
         *
         * Hold dpad_up (g1) to extend the elbow (up to the max position or past if wobbleOverride is toggled on)
         * Hold dpad_down (g1) to retract the elbow (down to the min position or past if wobbleOverride is toggled on)
         * Otherwise, don't move
         */
        if (gamepad1.dpad_up && (robot.wobbleElbow.getCurrentPosition() <= wobbleElboxMaxPosition || wobbleOverride)) {
            // Hold dpad_up (g1) to extend the elbow (up to the max position or past if wobbleOverride is toggled on)
            robot.wobbleElbow.setPower(1);
        } else if (gamepad1.dpad_down && (robot.wobbleElbow.getCurrentPosition() >= wobbleElbowMinPosition || wobbleOverride)) {
            // Hold dpad_down (g1) to retract the elbow (down to the min position or past if wobbleOverride is toggled on)
            robot.wobbleElbow.setPower(-1);
        } else {
            // Otherwise, dont move
            robot.wobbleElbow.setPower(0);
        }

        /*
         * Shooter Motors (shooterMotor, shooterMotor2)
         *
         * If pauseShooterMode is off, set the shooters at a power of shooterPowerRatio, else don't move them
         */
        if (!pauseShooterMode) {
            robot.shooterMotor.setPower(shooterPowerRatio);
            robot.shooterMotor2.setPower(shooterPowerRatio);
        } else {
            robot.shooterMotor.setPower(0);
            robot.shooterMotor2.setPower(0);
        }

        if (timer.milliseconds() >= 1) {
            // Log information every millisecond: timestamp, encoder counts for shooter, encoder counts for shooter 2
            String msg = String.format("%f, %f, %f", timestampTimer.milliseconds(), (float) robot.shooterMotor.getCurrentPosition(), (float) robot.shooterMotor2.getCurrentPosition());
            logMessages.add(msg);
            timer.reset();
        }

        if (impGamepad1.back.isInitialPress()) {
            // Press back button (g1) to save the shooter logs to a file
            try {
                int time = (int) (System.currentTimeMillis());
                FileUtilities.writeConfigFile("shooterLogFile_" + time + "_.csv", logMessages);
                logMessages.clear();
            } catch (IOException e) {
                telemetry.addData("Error writing to file", e.getMessage());
            }
        }

        telemetry.addData("Is visible", robotLocation != null);
        telemetry.addData("robot position", MatrixHelper.getInchesPositionString(robotLocation));
        telemetry.addData("robot angle", MatrixHelper.getAngleString(robotLocation));
        telemetry.addData("Current Mode", currentMode == 1 ? "Relative" : "Absolute");
        telemetry.addData("Robot angle", robotAngle);
        telemetry.addData("Wobble Arm Position", robot.wobbleElbow.getCurrentPosition());
        telemetry.addData("Right Stick Angle", rightStickAngle);
        telemetry.addData("Left Stick Angle", leftStickAngle);
        telemetry.addData("Right Motor Power", rightMotorPower);
        telemetry.addData("Left Motor Power", leftMotorPower);
        telemetry.addData("Middle Motor Power", middleMotorPower);
        telemetry.addData("Turn Power Ratio", turnPowerRatio);
        telemetry.addData("Move Power Ratio", movePowerRatio);
        telemetry.addData("Shooter Power Ratio", shooterPowerRatio);
        telemetry.addData("Shooter Motor Paused", pauseShooterMode);
        telemetry.addData("Intake Power", intakePowerRatio);
        telemetry.addData("Wobble Override", wobbleOverride);

        telemetry.addData("x To Move To", xToMoveTo);
        telemetry.addData("y To Move To", yToMoveTo);
        telemetry.addData("Angle To Move To", moveAngleRelative);

        if (turnAngleAbsolute != null) {
            telemetry.addData("Angle difference", AngleUnit.normalizeDegrees(turnAngleAbsolute - robotAngle));
        }

        telemetry.update();
    }
}
