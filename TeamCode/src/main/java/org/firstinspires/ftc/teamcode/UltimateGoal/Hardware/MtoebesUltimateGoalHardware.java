package org.firstinspires.ftc.teamcode.UltimateGoal.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Shared.Hardware.MockDcMotor;
import org.firstinspires.ftc.teamcode.Shared.Hardware.MockServo;

public class MtoebesUltimateGoalHardware extends BaseUltimateGoalHardware {

    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);
    }

    public <T> T getDevice(HardwareMap hwMap, Class<? extends T> classOrInterface, String motorName) {
        try {
            return hwMap.get(classOrInterface, motorName);
        } catch (Exception e) {
            failedHardware.add(motorName);

            if (classOrInterface == DcMotor.class) {
                return classOrInterface.cast(new MockDcMotor());
            } if (classOrInterface == Servo.class) {
                return classOrInterface.cast(new MockServo());
            } else {
                return null;
            }
        }
    }
}