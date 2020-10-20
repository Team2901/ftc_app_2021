package org.firstinspires.ftc.teamcode.Hardware;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Utility.AngleUtilities;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.teamcode.Hardware.ExemplaryBlinkinLED.LED_ERROR;
import static org.firstinspires.ftc.teamcode.Utility.AngleUtilities.getNormalizedAngle;

@SuppressLint("DefaultLocale")
public class BaseSkyStoneHardware {

    public static final String CONFIG_FILENAME = "servo_offset_config.txt";
    public static final String CONFIG_MIN_MAX_FILENAME = "servo_min_max_values_config.txt";
    public static final String CONFIG_POTENTIOMETER_FILENAME = "servo_potentiometer_values_config.txt";
    public static final String CONFIG_TEAM_COLOR_FILENAME = "team_color_config.txt";

    public static final double BASE_POWER_RATIO = 0.025;
    public static final double STALL_POWER_RATIO = 0;

    public static final boolean USE_POTENTIOMETER = false;

    public static final double GRABBER_MIN = 0.25;
    public static final double GRABBER_MAX = 0.75;
    public static final double ROBOT_FRONT_ANGLE = 0;
    public static final double ROBOT_RIGHT_ANGLE = -90;
    public static final double ROBOT_LEFT_ANGLE = 90;
    public static final double OPEN_JAW = 0;
    public static final double CLOSED_JAW = 1;

    public static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    public static final String LABEL_BUTTER = "Stone";
    public static final String LABEL_SKY_BUTTER = "Skystone";

    public static final String WEB_CAM_NAME = "Webcam 1";

    public final double inchesToEncoder;
    public double wheelServoGearRatio;
    public double widthOfRobot;
    public double lengthOfRobot;
    public double turnAngle;
    public double servoMaxAngle;

    public HardwareMap hardwareMap;

    public ExemplaryBlinkinLED blinkinLED = new ExemplaryBlinkinLED();

    //Made for a 4 wheel swerve drive system
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public DcMotor lift;

    //Steering servo for their respective motor
    public Servo servoFrontLeft;
    public Servo servoFrontRight;
    public Servo servoBackLeft;
    public Servo servoBackRight;

    public Servo crane;
    public Servo jaw;
    public Servo wrist;
    public Servo leftGrabber;
    public Servo rightGrabber;

    AnalogInput flPotentiometer;
    AnalogInput frPotentiometer;
    AnalogInput blPotentiometer;
    AnalogInput brPotentiometer;

    //Sensors and Things
    public BNO055IMU imu;
    public IntegratingGyroscope gyroscope;
    public double offset = 0;

    public BaseCamera webCamera = new BaseCamera();

    public SwerveWheel frontLeftSwerveWheel = new SwerveWheel("FL");
    public SwerveWheel frontRightSwerveWheel = new SwerveWheel("FR");
    public SwerveWheel backLeftSwerveWheel = new SwerveWheel("BL");
    public SwerveWheel backRightSwerveWheel = new SwerveWheel("BR");

    public SwerveWheel[] swerveWheels = {frontLeftSwerveWheel, frontRightSwerveWheel, backLeftSwerveWheel, backRightSwerveWheel};

    public BaseSkyStoneHardware(double widthOfRobot,
                                double lengthOfRobot,
                                double wheelServoGearRatio,
                                double servoMaxAngle,
                                double inchesToEncoder) {
        this.inchesToEncoder = inchesToEncoder;
        this.wheelServoGearRatio = wheelServoGearRatio;
        this.widthOfRobot = widthOfRobot;
        this.lengthOfRobot = lengthOfRobot;
        this.servoMaxAngle = servoMaxAngle;
        this.turnAngle = Math.atan(widthOfRobot/lengthOfRobot);
    }

    public class SwerveWheel {

        public List<Double> configValues = new ArrayList<>();

        public String name;
        public Servo servo;
        public DcMotor motor;
        public AnalogInput potentiometer;
        public Double potentiometerTarget;

        public double hardMinWheelPositionRelToZero = 0;
        public double hardMaxWheelPositionRelToZero = 0;
        public double targetAngle = 0;
        public int modifier = 1;
        public double offset = 0;
        public double minWheelAngle=0;
        public double maxWheelAngle = 0;

        public double hardMinWheelAngle = 0;
        public double hardMaxWheelAngle = 0;

        public SwerveWheel (String name) {
            this.name = name;
        }

        public SwerveWheel(String name, double hardMinWheelPositionRelToZero, double hardMaxWheelPositionRelToZero) {
            this.name = name;
            this.hardMinWheelPositionRelToZero = hardMinWheelPositionRelToZero;
            this.hardMaxWheelPositionRelToZero = hardMaxWheelPositionRelToZero;

            for (int i = 0; i < 3; i++) {
                configValues.add(0.0);
            }
        }

        public void setOffset(double offset) {
            this.offset = offset;
            minWheelAngle = servoPositionToWheelAngle(0);
            maxWheelAngle = servoPositionToWheelAngle(1);

            hardMinWheelAngle = servoPositionToWheelAngle(offset + hardMinWheelPositionRelToZero);
            hardMaxWheelAngle = servoPositionToWheelAngle(offset + hardMaxWheelPositionRelToZero);
        }

        public void setTargetAndModifier(double targetAngle, int modifier) {
            this.targetAngle = targetAngle;
            this.modifier = modifier;
        }

        public void setTargetAndModifier(double[] targetAngleAndModifier) {
            this.targetAngle = targetAngleAndModifier[0];
            this.modifier = (int) targetAngleAndModifier[1];
        }

        public double wheelAngleToServoPosition(double wheelAngle) {
            /*
            y=mx+b
            ServoPosition = [gearRatio*wheelAngle]/ServoMaxAngle] + offset
            wheelAngle is x
            */
            double servoAngle = wheelAngleToServoAngle(wheelAngle);
            return servoAngleToServoPosition(servoAngle);
        }

        public double wheelAngleToServoPosition() {
            return wheelAngleToServoPosition(targetAngle);
        }

        public double wheelAngleToServoAngle(double wheelAngle) {
            return wheelAngle / wheelServoGearRatio;
        }

        public double servoAngleToServoPosition(double servoAngle) {
            return (servoAngle / servoMaxAngle) + offset;
        }

        public double servoPositionToWheelAngle(double servoPosition){
            return (wheelServoGearRatio*servoMaxAngle)*(servoPosition-offset);

        }

        @Override
        public String toString() {
            return String.format("%s angle: %.2f, mod: %d, pos: %.2f, offset: %.2f encoder: %d", name, targetAngle, modifier, wheelAngleToServoPosition(), offset, motor.getCurrentPosition());
        }
    }

    public void init(HardwareMap hwMap) {
        hardwareMap = hwMap;

        //Inititialize all Motors
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        setWheelMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setWheelMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftGrabber = hwMap.get(Servo.class, "leftGrabber");
        rightGrabber = hwMap.get(Servo.class, "rightGrabber");
        leftGrabber.setDirection(Servo.Direction.REVERSE);

        lift = hardwareMap.dcMotor.get("lift");
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        //lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Initialize all servos
        servoFrontLeft = hardwareMap.servo.get("servoFrontLeft");
        servoFrontRight = hardwareMap.servo.get("servoFrontRight");
        servoBackLeft = hardwareMap.servo.get("servoBackLeft");
        servoBackRight = hardwareMap.servo.get("servoBackRight");

        crane = hardwareMap.servo.get("crane");
        jaw = hardwareMap.servo.get("jaw");

        jaw.setDirection(Servo.Direction.REVERSE);

        wrist = hardwareMap.servo.get("wrist");

        // crane is skipping, dont move it on init
        //crane.setPosition(.05);


        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        readServoHardMinMaxValues();
        // setting up the gyroscope
        gyroscope = (IntegratingGyroscope) imu;

        readServoOffsets();

        if (USE_POTENTIOMETER) {
            flPotentiometer = hardwareMap.analogInput.get("pfl");
            frPotentiometer = hardwareMap.analogInput.get("pfr");
            blPotentiometer = hardwareMap.analogInput.get("pbl");
            brPotentiometer = hardwareMap.analogInput.get("pbr");
        }

        frontLeftSwerveWheel.servo = servoFrontLeft;
        frontLeftSwerveWheel.motor = frontLeft;
        frontLeftSwerveWheel.potentiometer = flPotentiometer;
        frontLeftSwerveWheel.potentiometerTarget = 2.899;

        frontRightSwerveWheel.servo = servoFrontRight;
        frontRightSwerveWheel.motor = frontRight;
        frontRightSwerveWheel.potentiometer = frPotentiometer;
        frontRightSwerveWheel.potentiometerTarget = 3.336;

        backLeftSwerveWheel.servo = servoBackLeft;
        backLeftSwerveWheel.motor = backLeft;
        backLeftSwerveWheel.potentiometer = blPotentiometer;
        backLeftSwerveWheel.potentiometerTarget = 3.252;

        backRightSwerveWheel.servo = servoBackRight;
        backRightSwerveWheel.motor = backRight;
        backRightSwerveWheel.potentiometer = brPotentiometer;
        backRightSwerveWheel.potentiometerTarget = 1.264;
    }

    public String initWebCamera(HardwareMap hardwareMap) {
        return webCamera.initWebCamera(hardwareMap, WEB_CAM_NAME);
    }

    public String initTfod(){
        return webCamera.initTfod(.8, TFOD_MODEL_ASSET, LABEL_BUTTER, LABEL_SKY_BUTTER);
    }

    public void initBlinkinLED(HardwareMap hardwareMap){
        blinkinLED.init(hardwareMap,"LED");
        blinkinLED.color = this.readTeamColor();
    }

    public String writeTeamColor(){
        try{
            FileUtilities.writeConfigFile(CONFIG_TEAM_COLOR_FILENAME, this.blinkinLED.color);
        } catch (Exception e){
            return String.format("Error writing to file. %s", e.getMessage());
        }
        return null;
    }

    public int readTeamColor(){
        try {
            return FileUtilities.readTeamColor(CONFIG_TEAM_COLOR_FILENAME);
        } catch (Exception e){
            return LED_ERROR;
        }
    }

    public void readServoOffsets() {

        List<Double> offsets;

        try {
            offsets = FileUtilities.readDoubleConfigFile(CONFIG_FILENAME);
        } catch (IOException e) {
            offsets = new ArrayList<>();
        }

        for (int i = 0; i < swerveWheels.length; i++) {
            if (offsets.size() <= i) {
                offsets.add(0.0);
            }
        }

        frontLeftSwerveWheel.setOffset(offsets.get(0));
        frontRightSwerveWheel.setOffset(offsets.get(1));
        backLeftSwerveWheel.setOffset(offsets.get(2));
        backRightSwerveWheel.setOffset(offsets.get(3));
    }

    public String writeServoOffsets() {
        List<Double> values = new ArrayList<>();
        values.add(frontLeftSwerveWheel.offset);
        values.add(frontRightSwerveWheel.offset);
        values.add(backLeftSwerveWheel.offset);
        values.add(backRightSwerveWheel.offset);

        try {
            FileUtilities.writeConfigFile(CONFIG_FILENAME, values);
        } catch (Exception e) {
            return String.format("Error writing to file. %s", e.getMessage());
        }
        return null;
    }

    public void readServoPotentiometerTargets() {

        List<Double> potentiometer;

        try {
            potentiometer = FileUtilities.readDoubleConfigFile(CONFIG_POTENTIOMETER_FILENAME);
        } catch (IOException e) {
            potentiometer = new ArrayList<>();
        }

        for (int i = 0; i < swerveWheels.length; i++) {
            if (potentiometer.size() <= i) {
                potentiometer.add(0.0);
            }
        }

        frontLeftSwerveWheel.potentiometerTarget = potentiometer.get(0);
        frontRightSwerveWheel.potentiometerTarget = potentiometer.get(1);
        backLeftSwerveWheel.potentiometerTarget = potentiometer.get(2);
        backRightSwerveWheel.potentiometerTarget = potentiometer.get(3);
    }

    public String writePotentiometerTargets() {
        List<Double> values = new ArrayList<>();
        values.add(frontLeftSwerveWheel.potentiometerTarget);
        values.add(frontRightSwerveWheel.potentiometerTarget);
        values.add(backRightSwerveWheel.potentiometerTarget);
        values.add(backRightSwerveWheel.potentiometerTarget);

        try {
            FileUtilities.writeConfigFile(CONFIG_FILENAME, values);
        } catch (Exception e) {
            return String.format("Error writing to file. %s", e.getMessage());
        }
        return null;
    }

    public void readServoHardMinMaxValues() {

        List<Double> minMaxValues;

        try {
            minMaxValues = FileUtilities.readDoubleConfigFile(CONFIG_MIN_MAX_FILENAME);
        } catch (IOException e) {
            minMaxValues = new ArrayList<>();
        }

        for (int i = 0; i < 8; i++) {
            if (minMaxValues.size() <= i) {
                minMaxValues.add(0.0);
            }
        }

        frontLeftSwerveWheel.hardMinWheelPositionRelToZero = minMaxValues.get(0);
        frontLeftSwerveWheel.hardMaxWheelPositionRelToZero = minMaxValues.get(1);

        frontRightSwerveWheel.hardMinWheelPositionRelToZero = minMaxValues.get(2);
        frontRightSwerveWheel.hardMaxWheelPositionRelToZero = minMaxValues.get(3);

        backLeftSwerveWheel.hardMinWheelPositionRelToZero = minMaxValues.get(4);
        backLeftSwerveWheel.hardMaxWheelPositionRelToZero = minMaxValues.get(5);

        backRightSwerveWheel.hardMinWheelPositionRelToZero = minMaxValues.get(6);
        backRightSwerveWheel.hardMaxWheelPositionRelToZero = minMaxValues.get(7);
    }

    public String writeServoHardMinMaxValues() {
        try {
            List<Double> values = new ArrayList<>();
            values.add(frontLeftSwerveWheel.hardMinWheelPositionRelToZero);
            values.add(frontLeftSwerveWheel.hardMaxWheelPositionRelToZero);

            values.add(frontRightSwerveWheel.hardMinWheelPositionRelToZero);
            values.add(frontRightSwerveWheel.hardMaxWheelPositionRelToZero);

            values.add(backLeftSwerveWheel.hardMinWheelPositionRelToZero);
            values.add(backLeftSwerveWheel.hardMaxWheelPositionRelToZero);

            values.add(backRightSwerveWheel.hardMinWheelPositionRelToZero);
            values.add(backRightSwerveWheel.hardMaxWheelPositionRelToZero);
            FileUtilities.writeConfigFile(CONFIG_MIN_MAX_FILENAME, values);
        } catch (Exception e) {
            return String.format("Error writing to file. %s", e.getMessage());
        }
        return null;
    }

    public double getRawAngle() {
        Orientation orientation = gyroscope.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUtilities.getNormalizedAngle(orientation.firstAngle);
    }

    public double getAngle() {
        return AngleUtilities.getNormalizedAngle(getRawAngle() + offset);
    }

    public void setWheelMotorPower(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower) {
        backRight.setPower(backRightPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        frontLeft.setPower(frontLeftPower);
    }

    public void setWheelServoPosition(double fLPos, double fRPos, double bLPos, double bRPos) {
        servoFrontRight.setPosition(fRPos);
        servoBackRight.setPosition(bRPos);
        servoFrontLeft.setPosition(fLPos);
        servoBackLeft.setPosition(bLPos);
    }

    public void setWheelMotorMode(DcMotor.RunMode runMode) {
        backLeft.setMode(runMode);
        backRight.setMode(runMode);
        frontLeft.setMode(runMode);
        frontRight.setMode(runMode);
    }

    public void setGrabberPositition (double leftGrabberPos, double rightGrabberPos){
        leftGrabber.setPosition(leftGrabberPos);
        rightGrabber.setPosition(rightGrabberPos);
    }

    public boolean wheelsAreBusy() {
        return frontRight.isBusy() && frontLeft.isBusy() && backLeft.isBusy() && backRight.isBusy();

    }

    public void wait(int milliseconds, LinearOpMode opMode) {
        ElapsedTime timer = new ElapsedTime();
        while (opMode.opModeIsActive() && timer.milliseconds() < milliseconds) {

        }
    }

    public boolean isOkayToOpen() {
        if(crane.getPosition()> .3) {
            if (lift.getCurrentPosition() == 0) {
                if (wrist.getPosition() > .25) {
                    return false;
                }
                return true;
            } else {
                return true;
            }
        }
        return true;
    }

    public void moveStraight (double setPower, int targetPosition){

        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRight.setTargetPosition(targetPosition);
        frontLeft.setTargetPosition(targetPosition);
        backLeft.setTargetPosition(targetPosition);
        backRight.setTargetPosition(targetPosition);

        setWheelMotorMode(DcMotor.RunMode.RUN_TO_POSITION);

        setWheelMotorPower(setPower, setPower, setPower, setPower);
        //call whileMotorisBusy



    }

    public void setWheelTargetPositions(int position){
        frontLeft.setTargetPosition(position* frontLeftSwerveWheel.modifier);
        frontRight.setTargetPosition(position* frontRightSwerveWheel.modifier);
        backLeft.setTargetPosition(position* backLeftSwerveWheel.modifier);
        backRight.setTargetPosition(position* backRightSwerveWheel.modifier);
    }

    public void angleCheck(double goal, BaseSkyStoneHardware.SwerveWheel swerveWheel) {

        double start = swerveWheel.targetAngle;

        goal = getNormalizedAngle(goal);

        double dAngleForward = getNormalizedAngle(goal - start);
        double targetAngleForward = dAngleForward + start;
        boolean forwardPossible = (swerveWheel.minWheelAngle <= targetAngleForward && targetAngleForward <= swerveWheel.maxWheelAngle);

        double dAngleBackward = getNormalizedAngle(dAngleForward + 180);
        double targetAngleBackward = dAngleBackward + start;
        boolean backwardPossible = (swerveWheel.minWheelAngle <= targetAngleBackward && targetAngleBackward <= swerveWheel.maxWheelAngle);

        boolean goForward;

        if (forwardPossible && backwardPossible) {
            goForward = (Math.abs(dAngleForward) < Math.abs(dAngleBackward));
        } else {
            goForward = forwardPossible;
        }

        double targetAngle;
        int modifier;

        if (goForward) {
            targetAngle = targetAngleForward;
            modifier = 1;

        } else {
            targetAngle = targetAngleBackward;
            modifier = -1;
        }

        swerveWheel.setTargetAndModifier(targetAngle, modifier);
    }

    public double[] getWheelTargetAndModifier(double goal, BaseSkyStoneHardware.SwerveWheel swerveWheel) {

        double start = swerveWheel.targetAngle;

        goal = getNormalizedAngle(goal);

        double dAngleForward = getNormalizedAngle(goal - start);
        double targetAngleForward = dAngleForward + start;
        boolean forwardPossible = (swerveWheel.minWheelAngle <= targetAngleForward && targetAngleForward <= swerveWheel.maxWheelAngle);

        double dAngleBackward = getNormalizedAngle(dAngleForward + 180);
        double targetAngleBackward = dAngleBackward + start;
        boolean backwardPossible = (swerveWheel.minWheelAngle <= targetAngleBackward && targetAngleBackward <= swerveWheel.maxWheelAngle);

        boolean goForward;

        if (forwardPossible && backwardPossible) {
            goForward = (Math.abs(dAngleForward) < Math.abs(dAngleBackward));
        } else {
            goForward = forwardPossible;
        }

        double targetAngle;
        int modifier;

        if (goForward) {
            targetAngle = targetAngleForward;
            modifier = 1;

        } else {
            targetAngle = targetAngleBackward;
            modifier = -1;
        }

        double[] targetAndModifier = {targetAngle, modifier};

        return targetAndModifier;
    }

    public double joystickPositionToWheelAngle(double joystickPositionX, double joystickPositionY) {
        double wheelAngleRad = Math.atan2(joystickPositionY, joystickPositionX);
        double wheelAngle = AngleUtilities.radiansDegreesTranslation(wheelAngleRad) - 90;
        return AngleUtilities.getPositiveNormalizedAngle(wheelAngle);

    }

    public void swerveStraight(double joyWheelAngle, double power) {
        swerveMove(joyWheelAngle, joyWheelAngle, joyWheelAngle, joyWheelAngle, power);
    }

    public void swerveStraightAbsolute(double joyWheelAngle, double power) {
        double absoluteAngle = joyWheelAngle - getAngle();
        swerveMove(absoluteAngle, absoluteAngle, absoluteAngle, absoluteAngle, power);
    }

    public boolean swerveStraightAbsolute(double joyWheelAngle, double power, boolean forwardOnly) {
        double absoluteAngle = joyWheelAngle - getAngle();
        return swerveMove(absoluteAngle, absoluteAngle, absoluteAngle, absoluteAngle, power, forwardOnly);
    }

    public void swerveTurn(double power) {

        double fLAngle = joystickPositionToWheelAngle(1, 1);
        double fRAngle = joystickPositionToWheelAngle(1, -1);
        double bLAngle = joystickPositionToWheelAngle(-1, 1);
        double bRAngle = joystickPositionToWheelAngle(-1, -1);

        swerveMove(fLAngle, fRAngle, bLAngle, bRAngle, power);
    }

    public void swerveMove(double fLAngle, double fRAngle, double bLAngle, double bRAngle, double power) {

        angleCheck(fLAngle, frontLeftSwerveWheel);
        angleCheck(fRAngle, frontRightSwerveWheel);
        angleCheck(bLAngle, backLeftSwerveWheel);
        angleCheck(bRAngle, backRightSwerveWheel);

        double servoPositionfL = frontLeftSwerveWheel.wheelAngleToServoPosition();
        double servoPositionfR = frontRightSwerveWheel.wheelAngleToServoPosition();
        double servoPositionbL = backLeftSwerveWheel.wheelAngleToServoPosition();
        double servoPositionbR = backRightSwerveWheel.wheelAngleToServoPosition();

        setWheelServoPosition(servoPositionfL, servoPositionfR, servoPositionbL, servoPositionbR);

        double frontLeftPower = frontLeftSwerveWheel.modifier * power;
        double frontRightPower = frontRightSwerveWheel.modifier * power;
        double backLeftPower = backLeftSwerveWheel.modifier * power;
        double backRightPower = backRightSwerveWheel.modifier * power;

        setWheelMotorPower(frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }

    public boolean swerveMove(double fLAngle, double fRAngle, double bLAngle, double bRAngle, double power, boolean forwardOnly) {

        double[] fLTargetAndModifier = getWheelTargetAndModifier(fLAngle, frontLeftSwerveWheel);
        double[] fRTargetAndModifier = getWheelTargetAndModifier(fRAngle, frontRightSwerveWheel);
        double[] bLTargetAndModifier = getWheelTargetAndModifier(bLAngle, backLeftSwerveWheel);
        double[] bRTargetAndModifier = getWheelTargetAndModifier(bRAngle, backRightSwerveWheel);

        final boolean setServoPosition;

        if (forwardOnly) {
            // If flag is set, only move thr servos if none of the wheels will go backwards (else it will mess up run to position)
            setServoPosition = Math.signum(frontLeftSwerveWheel.modifier) == Math.signum(fLTargetAndModifier[1])
                    && Math.signum(frontRightSwerveWheel.modifier) == Math.signum(fRTargetAndModifier[1])
                    && Math.signum(backLeftSwerveWheel.modifier) == Math.signum(bLTargetAndModifier[1])
                    && Math.signum(backRightSwerveWheel.modifier) == Math.signum(bRTargetAndModifier[1]);
        } else {
            // If flag isn't set, it is always ok to set the servos such that the wheels go backwards
            setServoPosition = true;
        }

        if (setServoPosition) {
            frontLeftSwerveWheel.setTargetAndModifier(fLTargetAndModifier);
            frontRightSwerveWheel.setTargetAndModifier(fRTargetAndModifier);
            backLeftSwerveWheel.setTargetAndModifier(bLTargetAndModifier);
            backRightSwerveWheel.setTargetAndModifier(bRTargetAndModifier);


            double servoPositionfL = frontLeftSwerveWheel.wheelAngleToServoPosition();
            double servoPositionfR = frontRightSwerveWheel.wheelAngleToServoPosition();
            double servoPositionbL = backLeftSwerveWheel.wheelAngleToServoPosition();
            double servoPositionbR = backRightSwerveWheel.wheelAngleToServoPosition();

            setWheelServoPosition(servoPositionfL, servoPositionfR, servoPositionbL, servoPositionbR);
        }

        double frontLeftPower = frontLeftSwerveWheel.modifier * power;
        double frontRightPower = frontRightSwerveWheel.modifier * power;
        double backLeftPower = backLeftSwerveWheel.modifier * power;
        double backRightPower = backRightSwerveWheel.modifier * power;

        setWheelMotorPower(frontLeftPower, frontRightPower, backLeftPower, backRightPower);

        return setServoPosition;
    }

    public double getCurrentTurnPower(double absCurrent, double absGoal, double absStart, double maxPower) {
        double relCurrent = AngleUtilities.getNormalizedAngle(absCurrent - absStart);
        double relGoal = AngleUtilities.getNormalizedAngle(absGoal - absStart);
        double remainingDistance = AngleUtilities.getNormalizedAngle(relGoal - relCurrent);

        double basePower = BASE_POWER_RATIO * remainingDistance;
        double stallPower = STALL_POWER_RATIO * Math.signum(remainingDistance);

        return Range.clip(basePower + stallPower, -Math.abs(maxPower), Math.abs(maxPower));
    }

    public double getCurrentTurnPower(double absCurrent, double absGoal, double maxPower) {
        double remainingDistance = AngleUtilities.getNormalizedAngle(absGoal - absCurrent);

        double basePower = BASE_POWER_RATIO * remainingDistance;
        double stallPower = STALL_POWER_RATIO * Math.signum(remainingDistance);

        return -Range.clip(basePower + stallPower, -Math.abs(maxPower), Math.abs(maxPower));
    }
}

