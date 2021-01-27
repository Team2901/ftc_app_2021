package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class QualifierUltimateGoalHardware extends BaseUltimateGoalHardware {

    public static final double TICKS_PER_MOTOR_REV = 383.6; //5202 Series Yellow Jacket Planetary Gear Motor
    public static final double FORWARD_DRIVE_GEAR_RATIO = 1;
    public static final double CENTER_DRIVE_GEAR_RATIO = 1;
    public static final double FORWARD_TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * FORWARD_DRIVE_GEAR_RATIO;
    public static final double CENTER_TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * CENTER_DRIVE_GEAR_RATIO;
    public static final double WHEEL_CIRCUMFERENCE_INCHES = 4 * Math.PI;
    public static final double FORWARD_TICKS_PER_INCH = FORWARD_TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE_INCHES;
    public static final double CENTER_TICKS_PER_INCH = CENTER_TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE_INCHES;

    public QualifierUltimateGoalHardware() {
        super(FORWARD_TICKS_PER_INCH, CENTER_TICKS_PER_INCH);
    }

    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);
    }
}
