package org.firstinspires.ftc.teamcode.UltimateGoal.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.BaseUltimateGoalHardware;

@TeleOp(name = "Ben UltimateGoal", group = "2021_UltimateGoal")
public class BenUltimateGoalTeleOp extends OpMode {
    public BaseUltimateGoalHardware robot = new BaseUltimateGoalHardware();

    @Override
    public void init() {
        robot.init(this.hardwareMap);
    }

    @Override
    public void loop() {


        // Program sets left, middle, and right motors to their respective powers.
        robot.leftMotor.setPower(1);
        robot.rightMotor.setPower(1);
        robot.middleMotor.setPower(1);


    }
}
