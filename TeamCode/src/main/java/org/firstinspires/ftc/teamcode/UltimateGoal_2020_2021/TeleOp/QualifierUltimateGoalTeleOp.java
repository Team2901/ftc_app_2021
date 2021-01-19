package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstArray;
import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.BaseUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.QualifierUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;

import java.io.IOException;
import java.util.ArrayList;

@TeleOp(name = "Qualifier UltimateGoal", group = "2021_UltimateGoal")
public class QualifierUltimateGoalTeleOp extends OpMode {
    public BaseUltimateGoalHardware robot = BaseUltimateGoalHardware.create();
    ImprovedGamepad impGamepad1;
    ImprovedGamepad impGamepad2;
    ElapsedTime timer = new ElapsedTime();
    // Relative to you
    public static final int ABSOLUTE_MODE = 0;
    // Relative to front of robot
    public static final int RELATIVE_MODE = 1;
    public int currentMode = RELATIVE_MODE;
    public boolean isIntakeOn = false;
    double turnPowerRatio = 1;
    double movePowerRatio = 1;
    double shooterPowerRatio = 1;

    ArrayList<String> logMessages = new ArrayList<String>();
    ElapsedTime timestampTimer = new ElapsedTime();
    ImprovedGamepad improvedGamepad;

    @Override
    public void init() {
        // Instantiate gamepads
        impGamepad1 = new ImprovedGamepad(this.gamepad1, this.timer, "GP1");
        impGamepad2 = new ImprovedGamepad(this.gamepad2, this.timer, "GP2");

        robot.init(this.hardwareMap);

        /*
        Failed Hardware: 2
        1: shooter_motor
        2: intake_motor

        Failed Hardware: 0
         */
        telemetry.addData("Failed Hardware", robot.failedHardware.size());
        for(int i = 0; i < robot.failedHardware.size(); i++){
            telemetry.addData(String.valueOf(i + 1), robot.failedHardware.get(i));
        }

        telemetry.addData("Current Hardware", robot.hardwareClassName);

        telemetry.update();
    }

    @Override
    public void loop() {
        impGamepad1.update();
        impGamepad2.update();
        // Declare variables that will be used later in this method.
        float rightStickX = gamepad1.right_stick_x;
        float rightStickY = -1 * gamepad1.right_stick_y;
        float leftStickX = gamepad1.left_stick_x;
        float leftStickY = -1 * gamepad1.left_stick_y;
        double rightStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(rightStickY, rightStickX));
        double leftStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(leftStickY, leftStickX));
        double leftMotorPower = 0;
        double rightMotorPower = 0;
        double middleMotorPower = 0;
        float robotAngle = robot.getAngle();
        double desiredAngle = 0;
        boolean isLetterOnGamepad1Pressed = gamepad1.b || gamepad1.x;

        // If we press the dpad_left button, then the current mode will be changed to absolute mode.
        if(gamepad1.dpad_left){
            currentMode = ABSOLUTE_MODE;
        }
        // If we press the dpad_right button, then the current mode will be changed to relative mode.
        else if(gamepad1.dpad_right){
            currentMode = RELATIVE_MODE;
        }

        // If we press the left bumper, we turn the intake mechanism off.
        if(gamepad2.left_bumper){
            isIntakeOn = false;
        }
        // If we press the left bumper, we turn the intake mechanism on.
        else if(gamepad2.right_bumper){
            isIntakeOn = true;
        }

        // Prints out the current mode that we are in.
        telemetry.addData("Current Mode", currentMode == 1 ? "Relative" : "Absolute");

        // If the dpad up button is pressed, the shooter power ratio will increase by 0.1, assuming
        // that shooterPowerRatio is less than 1.
        if(impGamepad2.dpad_up.isInitialPress() && shooterPowerRatio < 1){
            shooterPowerRatio += 0.1;
        }
        // If the dpad down button is pressed, the shooter power ratio will decrease by 0.1, assuming
        // that shooterPowerRatio is greater than 0.
        else if(impGamepad2.dpad_down.isInitialPress() && shooterPowerRatio > 0){
            shooterPowerRatio -= 0.1;
        }

        // If the x button is pressed, the turn power ratio will increase by 0.1, assuming
        // that turnPowerRatio is less than 1.
        if(impGamepad2.x.isInitialPress() && turnPowerRatio < 1){
            turnPowerRatio += 0.1;
        }
        // If the b button is pressed, the turn power ratio will decrease by 0.1, assuming
        // that turnPowerRatio is greater than 0.
        else if(impGamepad2.b.isInitialPress() && turnPowerRatio > 0){
            turnPowerRatio -= 0.1;
        }

        // If the y button is pressed, the move power ratio will increase by 0.1, assuming
        // that the movePowerRatio is less than 1.
        if(impGamepad2.y.isInitialPress() && movePowerRatio < 1){
            movePowerRatio += 0.1;
        }
        // If the a button is pressed, the move power ratio will decrease by 0.1, assuming
        // that the movePowerRatio is greater than 0.
        else if(impGamepad2.a.isInitialPress() && movePowerRatio > 0){
            movePowerRatio -= 0.1;
        }

        // Determine radii of joysticks through Pythagorean Theorem.
        double rightStickRadius = Math.hypot(rightStickX, rightStickY);
        double leftStickRadius = Math.hypot(leftStickX, leftStickY);

        // When pressing the left bumper, the robot will turn counterclockwise.
        if(gamepad1.left_bumper){
            leftMotorPower = -turnPowerRatio;
            rightMotorPower = turnPowerRatio;
        }
        // When pressing the right bumper, the robot will turn clockwise.
        else if(gamepad1.right_bumper){
            leftMotorPower = turnPowerRatio;
            rightMotorPower = -turnPowerRatio;
        }
        // This sets the motor's power to however far the left joystick is pushed.
        else {
            // Declare variable for storing the angle relative to field to move at.
            double angleToMoveFieldTo;

            if(currentMode == ABSOLUTE_MODE){
                // Step 1: Calculate angle relative to field to move at (from left joystick)
                angleToMoveFieldTo = leftStickAngle + 180;
            }
            else
            {
                // Step 1: Calculate angle relative to field to move at (from robot)
                angleToMoveFieldTo = (robotAngle + leftStickAngle)-90;
            }

            // Step 2: Calculate angle relative to the robot to move at
            double angleToMoveRobotTo = angleToMoveFieldTo - robotAngle;
            // Step 3: Calculate forwards/sideways components to move at
            double xToMoveTo = Math.cos(Math.toRadians(angleToMoveRobotTo));
            double yToMoveTo = Math.sin(Math.toRadians(angleToMoveRobotTo));
            // Step 4: Calculate forwards/sideways powers to move at
            leftMotorPower = leftStickRadius * xToMoveTo * movePowerRatio;
            rightMotorPower = leftStickRadius * xToMoveTo * movePowerRatio;
            middleMotorPower = leftStickRadius * yToMoveTo * movePowerRatio;

            telemetry.addData("x To Move To", xToMoveTo);
            telemetry.addData("y To Move To", yToMoveTo);
            telemetry.addData("Angle To Move To", angleToMoveRobotTo);
        }

        /*
         * If the radius of the right stick is greater than the radius of our circular dead zone (0.8)
         * or if one of the letter buttons on the controller is pressed, the robot will turn to the
         * angle that we want it to be turned to.
         */
        if(rightStickRadius > 0.8 || isLetterOnGamepad1Pressed) {
            // If x is pressed, the robot will turn to 180 degrees.
            if(gamepad1.x) {
                desiredAngle = angleToTurnTo(2);
            }
            // If b is pressed, the robot will turn to 0 degrees.
            else if(gamepad1.b){
                desiredAngle = angleToTurnTo(4);
            }
            // Otherwise, the robot will turn to the angle that we point the right stick down in.
            else{
                desiredAngle = rightStickAngle + 180;
            }

            // This prints out what the angle difference is.
            telemetry.addData("Angle difference", AngleUnit.normalizeDegrees(desiredAngle - robotAngle));

            // Determine the speed that the motors should be set to.
            double speed = robot.getMotorTurnSpeed(desiredAngle, robotAngle);

            // Set the motors to their appropriate powers.
            leftMotorPower = -speed * turnPowerRatio;
            rightMotorPower = speed * turnPowerRatio;

            // Print out what the speed is.
            telemetry.addData("Speed",speed);
        }

        // Program sets left, middle, and right motors to their respective powers.
        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);
        robot.middleMotor.setPower(middleMotorPower);

        // If the y button is pressed, then the wobble grabber will open.
        if(gamepad1.y){
            robot.configureWobbleGrabber(false);
        }
        // Otherwise, if the a button is pressed, then the wobble grabber will open.
        else if(gamepad1.a){
            robot.configureWobbleGrabber(true);
        }

        // If dpad up is pressed we want the wobble elbow to keep on extending forward.
        if(gamepad1.dpad_up){
            if(robot.wobbleElbow.getCurrentPosition() >= 500) {
                robot.wobbleElbow.setPower(0.5);
            } else {
                robot.wobbleElbow.setPower(0);
            }
        }
        // If dpad down is pressed we want the wobble elbow to keep on retracting.
        else if(gamepad1.dpad_down){
            if(robot.wobbleElbow.getCurrentPosition() <= -15000) {
                robot.wobbleElbow.setPower(-0.5);
            } else {
                robot.wobbleElbow.setPower(0);
            }
        }
        // Otherwise, we want the robot's wobble elbow to stay still.
        else{
            robot.wobbleElbow.setPower(0);
        }

        // If the left trigger is pressed, then the intake and transfer motors will turn forward.
        if(gamepad1.left_trigger > 0)
        {
            robot.intakeMotor.setPower(0.5);
            robot.transferMotor.setPower(-0.5);
        }
        // Otherwise, if the right trigger is pressed, then the intake and transfer motors will turn backward.
        else if(gamepad1.right_trigger > 0)
        {
            robot.intakeMotor.setPower(-0.5);
            robot.transferMotor.setPower(0.5);
        }
        // Otherwise, the intake and transfer motors will stop turning.
        else {
            if (isIntakeOn) {
                robot.intakeMotor.setPower(0.5);
            } else {
                robot.intakeMotor.setPower(0);
            }
            robot.transferMotor.setPower(0);
        }

        // Always have the shooter motors running at 100% speed.
        robot.shooterMotor.setPower(shooterPowerRatio);
        robot.shooterMotor2.setPower(shooterPowerRatio);

        if (timer.milliseconds() >= 1) {
             /*
             Log information every millisecond
             1) timestamp
             2) encoder counts for shooter
             3) encoder counts for shooter 2
             */

            String msg = String.format("%f, %f, %f", timestampTimer.milliseconds(), (float) robot.shooterMotor.getCurrentPosition(), (float) robot.shooterMotor2.getCurrentPosition());

            logMessages.add(msg);

            timer.reset();
        }

        if(improvedGamepad.back.isInitialPress()) {

            int time = (int)(System.currentTimeMillis());

            try {
                FileUtilities.writeConfigFile("shooterLogFile_" + time + "_.csv", logMessages);
                logMessages.clear();
            } catch (IOException e) {
                telemetry.addData("Error writing to file", e.getMessage());
            }
        }

        telemetry.update();

        /*
         * This code below prints out the robot angle, right stick angle, right motor power, the
         * left motor power, and performs a telemetry update.
         */
        telemetry.addData("Robot angle", robotAngle);
        telemetry.addData("Right Stick Angle", rightStickAngle);
        telemetry.addData("Left Stick Angle", leftStickAngle);
        telemetry.addData("Right Motor Power", rightMotorPower);
        telemetry.addData("Left Motor Power", leftMotorPower);
        telemetry.addData("Middle Motor Power", middleMotorPower);
        telemetry.addData("Turn Power Ratio", turnPowerRatio);
        telemetry.addData("Move Power Ratio", movePowerRatio);
        telemetry.addData("Shooter Power Ratio", shooterPowerRatio);
        telemetry.update();
    }

    /**
     * This method has the robot turn to a certain angle based on the letter button that is pressed.
     * @param buttonDeterminer the number used to determine how many degrees the robot should turn by
     * @return
     */
    public int angleToTurnTo(int buttonDeterminer){
        // Declare angle variable.
        int angle = 0;

        // If y is pressed, the robot will turn by 90 degrees.
        if(buttonDeterminer == 1){
            angle = 90;
        }
        // If x is pressed, the robot will turn by 180 degrees.
        else if(buttonDeterminer == 2){
            angle = 180;
        }
        // If a is pressed, the robot will turn by 270 degrees.
        else if(buttonDeterminer == 3){
            angle = 270;
        }
        // If b is pressed, the robot will turn by 360 degrees.
        else if(buttonDeterminer == 4){
            angle = 360;
        }

        // Return angle variable.
        return angle + 180;
    }
}
