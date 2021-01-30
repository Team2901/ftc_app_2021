package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class MetoebesUltimateGoalHardware extends BaseUltimateGoalHardware {
    // gobilda 5202-0002-0019  yellow jacket - 312 RPM = 1 rev of 4" wheels =  12.6" in  .19sec
    public static final double TICKS_PER_MOTOR_REV = 537.6;
    public static final double FORWARD_DRIVE_GEAR_RATIO = 1.0;
    public static final double RECOMMENDED_MAX_POWER = .3;
    public static final double RECOMMENDED_MIN_POWER = .3;
    public static final double REVOLUTIONS_PER_MINUTE = 312;
    public static final double REVOLUTIONS_PER_SECOND = REVOLUTIONS_PER_MINUTE / 60.0;

    public static final double CENTER_DRIVE_GEAR_RATIO = 1;
    public static final double FORWARD_TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * FORWARD_DRIVE_GEAR_RATIO;
    public static final double CENTER_TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * CENTER_DRIVE_GEAR_RATIO;
    public static final double WHEEL_CIRCUMFERENCE_INCHES = 4 * Math.PI;
    public static final double FORWARD_TICKS_PER_INCH = FORWARD_TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE_INCHES;
    public static final double CENTER_TICKS_PER_INCH = CENTER_TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE_INCHES;
    public static final double FEET_PER_SECOND = REVOLUTIONS_PER_SECOND * WHEEL_CIRCUMFERENCE_INCHES;


    public MetoebesUltimateGoalHardware() {
        super(FORWARD_TICKS_PER_INCH, CENTER_TICKS_PER_INCH, 1);
    }

    @Override
    public void init(HardwareMap hwMap) {

        super.init(hwMap);

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        middleMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    public double getRecommendedMaxMotorSpeed() {
        return RECOMMENDED_MAX_POWER;
    }

    public double getSpeed(double desiredFeetPerSecond){
        //1 = FEET_PER_SECOND, x=desiredFeetPerSecond
        double speed = desiredFeetPerSecond / FEET_PER_SECOND;
        return speed;
    }
}