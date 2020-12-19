package org.firstinspires.ftc.teamcode.UltimateGoal.Hardware;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class GrantHardware extends BaseUltimateGoalHardware {

    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);
        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }


}