package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class MetoebesUltimateGoalHardware extends BaseUltimateGoalHardware {

    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);

        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

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