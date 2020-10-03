package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class ProgrammingUltimateGoalHardware {
    // Instance Variables
    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;
    public DcMotor middleMotor = null;
    BNO055IMU imu;

    public void init(HardwareMap hwMap) {
        // Define and Initialize Motors
        leftMotor = hwMap.dcMotor.get("left_drive");
        rightMotor = hwMap.dcMotor.get("right_drive");
        middleMotor = hwMap.dcMotor.get("middle_drive");
        // Setting left motor to reverse, making the robot moveable now.
        leftMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    // This method returns the angle of the robot.
    public float getAngle() {
        // Gets orientation of robot.
        Orientation angularOrientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        // Returns the angle of the robot.
        return angularOrientation.firstAngle;
    }
}