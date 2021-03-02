package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class QualifierUltimateGoalHardware extends BaseUltimateGoalHardware {

    public static final double FORWARD_TICKS_PER_MOTOR_REV = 383.6; //5202-0002-0014 Series Yellow Jacket Planetary Gear Motor
    //public static final double CENTER_TICKS_PER_MOTOR_REV = 1425.2; //5202-0002-0051
    public static final double CENTER_TICKS_PER_MOTOR_REV = 383.6;
    public static final double FORWARD_DRIVE_GEAR_RATIO = 1;//1/1.5 if they do pulleys. 2 or 1/2 if they do gears unless its 1:1 gears
    public static final double CENTER_DRIVE_GEAR_RATIO = 1;
    public static final double FORWARD_TICKS_PER_DRIVE_REV = FORWARD_TICKS_PER_MOTOR_REV * FORWARD_DRIVE_GEAR_RATIO;
    public static final double CENTER_TICKS_PER_DRIVE_REV = CENTER_TICKS_PER_MOTOR_REV * CENTER_DRIVE_GEAR_RATIO;
    public static final double WHEEL_CIRCUMFERENCE_INCHES = 4 * Math.PI;
    public static final double FORWARD_TICKS_PER_INCH = FORWARD_TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE_INCHES;
    public static final double CENTER_TICKS_PER_INCH = CENTER_TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE_INCHES;
    public static final double FORWARD_REVOLUTIONS_PER_MINUTE = 435.0;
    public static final double FORWARD_REVOLUTIONS_PER_SECOND = FORWARD_REVOLUTIONS_PER_MINUTE / 60.0;
    public static final double FORWARD_FEET_PER_SECOND = (FORWARD_REVOLUTIONS_PER_SECOND * WHEEL_CIRCUMFERENCE_INCHES) / 12.0;
    //public static final double CENTER_REVOLUTIONS_PER_MINUTE = 117.0;
    public static final double CENTER_REVOLUTIONS_PER_MINUTE = 435.0;
    public static final double CENTER_REVOLUTIONS_PER_SECOND = CENTER_REVOLUTIONS_PER_MINUTE / 60.0;
    public static final double CENTER_FEET_PER_SECOND = (CENTER_REVOLUTIONS_PER_SECOND * WHEEL_CIRCUMFERENCE_INCHES) / 12.0;
    public static final double SHOOTER_VELOCITY = (4800 * 28) / 60;

    public QualifierUltimateGoalHardware() {
        super(FORWARD_TICKS_PER_INCH, CENTER_TICKS_PER_INCH);
    }

    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);
    }
    public double getForwardSpeed(double desiredFeetPerSecond){
        //1 = FEET_PER_SECOND, x=desiredFeetPerSecond
        double speed = desiredFeetPerSecond / FORWARD_FEET_PER_SECOND;
        return speed;
    }
    public double getStrafeSpeed(double desiredFeetPerSecond){
        //1 = FEET_PER_SECOND, x=desiredFeetPerSecond
        double speed = desiredFeetPerSecond / CENTER_FEET_PER_SECOND;
        return speed;
    }
}
