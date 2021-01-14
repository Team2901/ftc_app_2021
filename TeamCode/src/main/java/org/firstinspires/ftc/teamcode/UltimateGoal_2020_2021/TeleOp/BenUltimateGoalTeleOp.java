package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.BaseUltimateGoalHardware;

@Disabled
@TeleOp(name = "Ben UltimateGoal", group = "2021_UltimateGoal")
public class BenUltimateGoalTeleOp extends OpMode {
    public BaseUltimateGoalHardware robot = BaseUltimateGoalHardware.create();

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
