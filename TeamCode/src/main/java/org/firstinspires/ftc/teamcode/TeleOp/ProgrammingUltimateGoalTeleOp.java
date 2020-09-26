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
        float leftStickY = -1 * gamepad1.left_stick_y;
        if (leftStickY < 0) {
            //If the left stick y value is less than 0 then we will move backward.
            robot.leftMotor.setPower(-1);
            robot.rightMotor.setPower(-1);
        } else if (leftStickY > 0) {
            //If the left stick y value is greater than 0 then we will move forward.
            robot.leftMotor.setPower(1);
            robot.rightMotor.setPower(1);
        } else {
            //If the left stick y is not less than or greater than 0 then we want
            //the robot to stop.
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);
        }
    }
}