package org.firstinspires.ftc.teamcode.UltimateGoal.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Shared.Hardware.MockDcMotor;
import org.firstinspires.ftc.teamcode.Shared.Hardware.MockServo;

public class MtoebesUltimateGoalHardware extends BaseUltimateGoalHardware {

    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);

        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

    }


}