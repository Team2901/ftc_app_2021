package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
        double rightStickAngle = Math.atan2(rightStickY, rightStickX);
        double leftMotorPower = 0;
        double rightMotorPower = 0;

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

        // Program sets left, middle, and right motors to their respective powers.
        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);
        robot.middleMotor.setPower(leftStickX);
    }
}
