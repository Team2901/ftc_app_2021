package org.firstinspires.ftc.teamcode.UltimateGoal.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class MtoebesUltimateGoalHardware extends BaseUltimateGoalHardware {
    public DcMotor middleMotor = null;


    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);
        middleMotor = hwMap.dcMotor.get("middle_drive");
        middleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        middleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}