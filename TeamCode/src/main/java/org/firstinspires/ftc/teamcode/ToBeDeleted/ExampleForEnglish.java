package org.firstinspires.ftc.teamcode.ToBeDeleted;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Disabled
public class ExampleForEnglish extends LinearOpMode {

    public static final double TICKS_PER_MOTOR_REV = 1140;
    public static final double DRIVE_GEAR_RATIO = 2;
    public static final double TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * DRIVE_GEAR_RATIO;
    public static final double WHEEL_CIRCUMFERENCE = 4 * Math.PI;
    public static final double TICKS_PER_INCH = TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE;

    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public Servo armServo;
    public BNO055IMU imu;

    public void init(HardwareMap hardwareMap) {
        leftDrive = hardwareMap.dcMotor.get("left_drive");
        rightDrive = hardwareMap.dcMotor.get("right_drive");

        armServo = hardwareMap.servo.get("arm");

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        leftDrive.setPower(0);
        rightDrive.setPower(0);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    @Override
    public void runOpMode() throws InterruptedException {

        init(this.hardwareMap);

        waitForStart();

        moveInches(6);
        waveServoHand();
        turnByAngle(90);
    }

    public void moveInches(double inches) {
        int ticks = (int) (inches * TICKS_PER_INCH);

        leftDrive.setTargetPosition(leftDrive.getCurrentPosition() + ticks);
        rightDrive.setTargetPosition(rightDrive.getCurrentPosition() + ticks);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(.5);
        rightDrive.setPower(.5);

        while (opModeIsActive() &&
                (leftDrive.isBusy() && rightDrive.isBusy())) {
            telemetry.addData("Current Left Position", leftDrive.getCurrentPosition());
            telemetry.addData("Current Right Position", rightDrive.getCurrentPosition());

            telemetry.update();
        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void waveServoHand() {
        for (int i = 0; i < 4; i++) {
            armServo.setPosition(.25);
            sleep(1000);
            armServo.setPosition(.75);
            sleep(1000);
        }
    }

    public void turnByAngle(double turnAngle) {

        double startAngle = getAngle();

        double targetAngle = AngleUnit.normalizeDegrees(startAngle + turnAngle);

        int turnDirection;
        if (turnAngle >= 0) {
            turnDirection = 1;
        } else {
            turnDirection = -1;
        }

        leftDrive.setPower(-0.25 * turnDirection);
        rightDrive.setPower(0.25 * turnDirection);

        double currentAngle = getAngle();

        if (turnDirection == 1) {
            while (AngleUnit.normalizeDegrees(currentAngle - targetAngle) < 0 && opModeIsActive()) {

                telemetry.addData("Current Angle", currentAngle);
                telemetry.addData("Target Angle", targetAngle);
                telemetry.update();

                currentAngle = getAngle();
            }
        } else {
            while (AngleUnit.normalizeDegrees(currentAngle - targetAngle) > 0 && opModeIsActive()) {

                telemetry.addData("Current Angle", currentAngle);
                telemetry.addData("Target Angle", targetAngle);
                telemetry.update();

                currentAngle = getAngle();
            }
        }

        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }

    public double getAngle() {
        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUnit.normalizeDegrees(orientation.firstAngle);
    }
}


