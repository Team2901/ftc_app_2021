package org.firstinspires.ftc.teamcode.UltimateGoal.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utility.MissingDcMotor;

import java.util.ArrayList;
import java.util.List;

public class MtoebesUltimateGoalHardware extends BaseUltimateGoalHardware {
    public DcMotor middleMotor = null;

    public DcMotor intakeMotor = null;

    public List<String> missingHardwareNames = new ArrayList<>();

    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);
        middleMotor = hwMap.dcMotor.get("middle_drive");
        middleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        middleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intakeMotor = getDevice(hwMap, DcMotor.class, "intake_motor");
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public <T> T getDevice(HardwareMap hwMap, Class<? extends T> classOrInterface, String motorName) {
        try {
            return hwMap.get(classOrInterface, motorName);
        } catch (Exception e) {
            missingHardwareNames.add(motorName);

            if (classOrInterface == DcMotor.class) {
                return classOrInterface.cast(new MissingDcMotor());
            } else {
                return null;
            }
        }
    }
}