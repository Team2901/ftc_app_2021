package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Shared.Hardware.BaseCamera;
import org.firstinspires.ftc.teamcode.Shared.Hardware.MockCRServo;
import org.firstinspires.ftc.teamcode.Shared.Hardware.MockDcMotor;
import org.firstinspires.ftc.teamcode.Shared.Hardware.MockDcMotorEx;
import org.firstinspires.ftc.teamcode.Shared.Hardware.MockServo;
import org.firstinspires.ftc.teamcode.Utility.ConfigUtilities;

import java.util.ArrayList;
import java.util.List;

public class BaseUltimateGoalHardware {
    public static final String WEB_CAM_NAME = "Webcam 1";
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    public static final String ELEMENT_QUAD = "Quad";
    public static final String ELEMENT_SINGLE = "Single";
    public static final double KICKER_MIN  = 0.45;
    public static final double KICKER_MAX = 0.15;
    public int elbowRetractedPosition = 0;
    public int elbowExtendedPosition = 3750;
    // Instance Variables
    public DcMotorEx leftMotor = null;
    public DcMotorEx rightMotor = null;
    BNO055IMU imu;
    public BaseCamera webCamera = new BaseCamera();
    public double robotTurnRampDownAngle = 45;
    public double robotTurnStopAngle = 5;
    public List<String> failedHardware = new ArrayList<>();
    public DcMotorEx middleMotor = null;
    public DcMotor intakeMotor = null;
    public DcMotorEx shooterMotor = null;
    public DcMotorEx shooterMotor2 = null;
    public DcMotor transferMotor = null;
    public Servo wobbleGrabber;
    public DcMotor wobbleElbow;
    public double forwardTicksPerInch;
    public double centerTicksPerInch;
    public String hardwareClassName;
    public Servo kicker;
    public CRServo backupKicker;

    public BaseUltimateGoalHardware() {
    }

    public BaseUltimateGoalHardware(double forwardTicksPerInch, double centerTicksPerInch) {
        this(forwardTicksPerInch, centerTicksPerInch, 5);
    }

    public BaseUltimateGoalHardware(double forwardTicksPerInch, double centerTicksPerInch, double turningDeadzone) {
        this.forwardTicksPerInch = forwardTicksPerInch;
        this.centerTicksPerInch = centerTicksPerInch;
        this.robotTurnStopAngle = turningDeadzone;
    }

    public BaseUltimateGoalHardware(double forwardTicksPerInch, double centerTicksPerInch, double turningDeadzone, int elbowRetractedPosition, int elbowExtendedPosition) {
        this.forwardTicksPerInch = forwardTicksPerInch;
        this.centerTicksPerInch = centerTicksPerInch;
        this.robotTurnStopAngle = turningDeadzone;
        this.elbowRetractedPosition = elbowRetractedPosition;
        this.elbowExtendedPosition = elbowExtendedPosition;
    }

    public void init(HardwareMap hwMap) {
        // Define and Initialize Motors
        leftMotor = getMotorEx(hwMap, "left_drive");
        rightMotor = getMotorEx(hwMap, "right_drive");

        // Setting left motor to reverse, making the robot moveable now.
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);//left for temp right for gears
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


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

        middleMotor = getMotorEx(hwMap,"middle_drive");

        middleMotor.setDirection(DcMotor.Direction.REVERSE);
        middleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        middleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        middleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeMotor = getMotor(hwMap,"intake_motor");
        shooterMotor = getMotorEx(hwMap,"shooter_motor");
        shooterMotor2 = getMotorEx(hwMap,"shooter_motor_2");
        transferMotor = getMotor(hwMap, "transfer_motor");
        wobbleElbow = getMotor(hwMap, "elbow_motor");

        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        transferMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        transferMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        transferMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        wobbleElbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobbleElbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wobbleElbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wobbleGrabber = getServo(hwMap, "grabber");
        kicker = getServo(hwMap, "kicker");
        backupKicker = getCRServo(hwMap, "backup_kicker");
        backupKicker.setDirection(CRServo.Direction.REVERSE);
    }

    public String initWebCamera(HardwareMap hardwareMap){
        return webCamera.initWebCamera(hardwareMap, WEB_CAM_NAME);
    }

    public String initPhoneCamera(HardwareMap hardwareMap){
        return webCamera.initBackCamera(hardwareMap);
    }

    public String initTfod(){
        return webCamera.initTfod(.8, TFOD_MODEL_ASSET, ELEMENT_QUAD, ELEMENT_SINGLE);
    }

    public float getAngle() {
        // Gets orientation of robot.
        Orientation angularOrientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        // Returns the angle of the robot.
        return angularOrientation.firstAngle;
    }

    /**
     * This method determines the speed of a motor.
     * @param desiredAngle our desired angle
     * @param robotAngle the robot's current angle
     * @return
     */
    public double getMotorTurnSpeed(double desiredAngle, double robotAngle){
        // Calculate the angle difference between our desired angle and the actual angle of
        // the robot.
        double angleDifference = AngleUnit.normalizeDegrees(desiredAngle - robotAngle);

        // Declare the speed variable for later use.
        double speed;

        // If the angle difference is greater than 10 the robot will turn counterclockwise.
        // Otherwise, if the angle difference is less than -10 the robot will turn clockwise.
        if (Math.abs(angleDifference) > robotTurnStopAngle) {
            // Equation for determining target speed: Speed = angle / robotTurnRampDownAngle.
            speed = angleDifference/robotTurnRampDownAngle;
        }
        // Otherwise, we don't want the robot to turn at all.
        else {
            speed = 0;
        }

        // Return the speed that the motor should be turning to.
        return (speed * 3) / 4;
    }

    public DcMotor getMotor(HardwareMap hwMap, String name) {
        try {
            return hwMap.dcMotor.get(name);
        } catch (Exception e){
            failedHardware.add(name);
            return new MockDcMotor();
        }
    }

    public DcMotorEx getMotorEx(HardwareMap hwMap, String name){
        try {
            return hwMap.get(DcMotorEx.class, name);
        } catch (Exception e){
            failedHardware.add(name);
            return new MockDcMotorEx();
        }
    }

    public Servo getServo(HardwareMap hwMap, String name){
        try{
            return hwMap.servo.get(name);
        } catch(Exception e){
            failedHardware.add(name);
            return new MockServo();
        }
    }

    public CRServo getCRServo(HardwareMap hwMap, String name){
        try{
            return hwMap.crservo.get(name);
        } catch(Exception e){
            failedHardware.add(name);
            return new MockCRServo();
        }
    }

    /**
     * This helper method configures the wobble goal grabber to either be opened or closed.
     * @param isClosed determines whether we want the wobble grabber to close or open
     */
    public void configureWobbleGrabber(boolean isClosed){
        // If true is passed as the parameter, the wobble grabber will close.
        if(isClosed){
            wobbleGrabber.setPosition(.745);
        }
        // Otherwise, the wobble grabber will remain open.
        else{
            wobbleGrabber.setPosition(0);
        }
    }

    public static BaseUltimateGoalHardware create() {
        BaseUltimateGoalHardware baseUltimateGoalHardware;
        try{
            String hardwareName = ConfigUtilities.getRobotConfigurationName();
            baseUltimateGoalHardware = (BaseUltimateGoalHardware) Class.forName("org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware." + hardwareName).newInstance();
            baseUltimateGoalHardware.hardwareClassName = hardwareName;
            return baseUltimateGoalHardware;
        } catch (Exception ClassNotFoundException){
            baseUltimateGoalHardware =  new BaseUltimateGoalHardware();
            baseUltimateGoalHardware.hardwareClassName = "BaseUltimateGoalHardware";
            return baseUltimateGoalHardware;
        }
    }

    public double getForwardSpeed(double desiredFeetPerSecond){
        //1 = FEET_PER_SECOND, x=desiredFeetPerSecond
        return 1.0;
    }

    public double getStrafeSpeed(double desiredFeetPerSecond){
        return 1.0;
    }

    public double getShooterVelocity(){
        // (rpm of motor * ticks/revolution) / seconds per minute
        return (4800 * 28) / 60;
    }
}

