package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class GrantHardware extends BaseUltimateGoalHardware {

    public GrantHardware(){
        super(0, 0, 5);
    }

    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);
        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}