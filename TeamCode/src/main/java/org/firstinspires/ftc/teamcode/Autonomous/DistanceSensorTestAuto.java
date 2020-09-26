package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Utility.AngleUtilities;

@Disabled
@Autonomous(name = "DistanceSensorTest", group = "new_programmer")
public class DistanceSensorTestAuto extends LinearOpMode {
    DcMotor leftDrive;
    DcMotor rightDrive;
    DistanceSensor distanceSensor;

    public BNO055IMU imu;
    public IntegratingGyroscope gyroscope;


    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        this.waitForStart();


        while (opModeIsActive()){
            doTheThing();
        }
    }

    public void initRobot() {
        leftDrive = this.hardwareMap.dcMotor.get("left_drive");
        rightDrive = this.hardwareMap.dcMotor.get("right_drive");

        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        distanceSensor = this.hardwareMap.get (DistanceSensor.class,"distance");
        
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        gyroscope = (IntegratingGyroscope) imu;

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.initialize(parameters);
    }

    public void doTheThing() {
        int encoderCount = leftDrive.getCurrentPosition();
        double frontDistance=encoderCount;

        // go forward while 10 inches away from the wall
        while (distanceSensor.getDistance(DistanceUnit.INCH) > 10 && this.opModeIsActive()) {
            leftDrive.setPower(1);
            rightDrive.setPower(1);
            telemetry.addData("state", "going straight");
            double distanceValue = distanceSensor.getDistance(DistanceUnit.INCH);
            telemetry.addData("distance", distanceValue);
            telemetry.update();
        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);

        telemetry.addData("changing","");
        telemetry.update();
        double targetAngle = getCurrentAngle() - 90;
        // turn in place
        double deltaAngle = ((getCurrentAngle() - targetAngle + 360 + 180) % 360) - 180;
        while (Math.abs(deltaAngle) > 10 && this.opModeIsActive()) {
            rightDrive.setPower(-.5);
            leftDrive.setPower(.5);
            telemetry.addData("state","Turning");
            double rotationValue = getCurrentAngle();
            telemetry.addData("current angle", rotationValue);
            telemetry.addData("target angle", targetAngle);
            telemetry.addData("delta angle", deltaAngle);
            telemetry.update();
            deltaAngle = ((getCurrentAngle() - targetAngle + 360 + 180) % 360) - 180;
        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);


    }

    public double getCurrentAngle() {
        Orientation orientation = gyroscope.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUtilities.getNormalizedAngle(orientation.firstAngle);
    }

}
