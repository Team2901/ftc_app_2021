package org.firstinspires.ftc.teamcode.UltimateGoal.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.TankUltimateGoalHardware;

@TeleOp(name = "Vuforia UltimateGoal Test", group = "2021_UltimateGoal")
public class VuforiaUltimateGoalTest extends OpMode {
    public TankUltimateGoalHardware robot = new TankUltimateGoalHardware();
    @Override
    public void init() {
        robot.init(this.hardwareMap);
    }

    @Override
    public void loop() {

    }
}
