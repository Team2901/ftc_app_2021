package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


public class MetoebesUltimateGoalHardware extends BaseUltimateGoalHardware {
    // gobilda 5202-0002-0019  yellow jacket - 312 RPM = 1 rev of 4" wheels =  12.6" in  .19sec
    public static final double TICKS_PER_MOTOR_REV = 537.6;
    public static final double FORWARD_DRIVE_GEAR_RATIO = 1.0;
    public static final double RECOMMENDED_MAX_POWER = .3;
    public static final double RECOMMENDED_MIN_POWER = .3;

    public static final double CENTER_DRIVE_GEAR_RATIO = 1;
    public static final double FORWARD_TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * FORWARD_DRIVE_GEAR_RATIO;
    public static final double CENTER_TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * CENTER_DRIVE_GEAR_RATIO;
    public static final double WHEEL_CIRCUMFERENCE_INCHES = 4 * Math.PI;
    public static final double FORWARD_TICKS_PER_INCH = FORWARD_TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE_INCHES;
    public static final double CENTER_TICKS_PER_INCH = CENTER_TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE_INCHES;

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

    public float getAngle() {

        /*
        ME Toebes robot has the control hub mounted along its side.
        Y points robot UP. X points robot front. Z points robot right.

        Heading is the measure of rotation along the z-axis.
        If the Hub is laying flat on a table, the z-axis points upwards through the front plate of the Hub.

        Pitch is the measure of rotation along the x-axis.
        The x-axis is the axis that runs from the bottom of the hub, near the servo ports, to the top of the hub ,where the USB ports are.

        Roll is the measure along the y-axis.
        The y-axis is the axis that runs from the sensor ports on the right to the motor ports on the left.
         */

        // Gets orientation of robot.
        Orientation angularOrientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        // Returns the angle of the robot.
        return AngleUnit.normalizeDegrees(angularOrientation.secondAngle + offset);
    }
}