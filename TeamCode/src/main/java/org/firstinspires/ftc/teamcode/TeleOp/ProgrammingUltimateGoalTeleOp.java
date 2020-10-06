package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.ProgrammingUltimateGoalHardware;

@TeleOp(name = "ProgrammingUltimateGoalTeleOp")
public class ProgrammingUltimateGoalTeleOp extends OpMode {
    public ProgrammingUltimateGoalHardware robot = new ProgrammingUltimateGoalHardware();

    @Override
    public void init() {
        robot.init(this.hardwareMap);
    }

    @Override
    public void loop() {
        float rightStickX = gamepad1.right_stick_x;
        float rightStickY = -1 * gamepad1.right_stick_y;
        float leftStickX = gamepad1.left_stick_x;
        float leftStickY = -1 * gamepad1.left_stick_y;
        double rightStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(rightStickY, rightStickX));
        double leftMotorPower = 0;
        double rightMotorPower = 0;
        float robotAngle = robot.getAngle();

        if(gamepad1.left_bumper){
            // When pressing the left bumper, the robot will turn counterclockwise.
            leftMotorPower = -1;
            rightMotorPower = 1;
        } else if(gamepad1.right_bumper){
            // When pressing the right bumper, the robot will turn clockwise.
            leftMotorPower = 1;
            rightMotorPower = -1;
        } else {
            // This sets the motor's power to however far the left joystick is pushed.
            leftMotorPower = leftStickY;
            rightMotorPower = leftStickY;
        }

        if(rightStickX != 0 || rightStickY != 0) {
            // Calculate the angle difference between our desired angle and the actual angle of
            // the robot.
            double angleDifference = AngleUnit.normalizeDegrees(rightStickAngle - robotAngle);
            // This prints out what the angle difference is.
            telemetry.addData("Angle difference", angleDifference);

            // If the angle difference is greater than 0, the robot will turn counterclockwise.
            if (angleDifference > 0) {
                leftMotorPower = -1;
                rightMotorPower = 1;
            }
            // Otherwise, the robot will turn clockwise.
            else if (angleDifference < 0) {
                leftMotorPower = 1;
                rightMotorPower = -1;
            }
        }

        // Program sets left, middle, and right motors to their respective powers.
        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);
        robot.middleMotor.setPower(leftStickX);

        /*
        * This code below prints out the robot angle, right stick angle, right motor power, the
        * left motor power, and performs a telemetry update.
         */
        telemetry.addData("Robot angle", robotAngle);
        telemetry.addData("Right Stick Angle", rightStickAngle);
        telemetry.addData("Right Motor Power", rightMotorPower);
        telemetry.addData("Left Motor Power", leftMotorPower);
        telemetry.update();
    }
}
