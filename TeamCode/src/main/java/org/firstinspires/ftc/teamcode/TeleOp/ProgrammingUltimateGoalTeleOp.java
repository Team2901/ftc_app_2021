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
        float leftStickX = gamepad1.left_stick_x;
        float leftStickY = -1 * gamepad1.left_stick_y;
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
        } else if (leftStickY < 0) {
            //If the left stick y value is less than 0 then we will move backward.
            leftMotorPower = -1;
            rightMotorPower = -1;
        } else if (leftStickY > 0) {
            //If the left stick y value is greater than 0 then we will move forward.
            leftMotorPower = 1;
            rightMotorPower = 1;
        }

        // Program sets left and right motors to their respective powers.
        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);

        robot.middleMotor.setPower(leftStickX);
    }
}
