package org.firstinspires.ftc.teamcode.Autonomous.PGP;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created with Team 6183's Duckinator 3000
 */

@Autonomous(name = "DuckinatorAuto", group = "DuckSquad")
public class TheDuckinator extends LinearOpMode {
    private DcMotor leftWheel;
    private DcMotor rightWheel;
    private int globalAngle;
    BNO055IMU imu;
    Orientation lastAngles = new Orientation();
    @Override
    public void runOpMode() throws InterruptedException {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }
        leftWheel = hardwareMap.dcMotor.get("leftWheel");
        rightWheel = hardwareMap.dcMotor.get("rightWheel");
        rightWheel.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();
        if (opModeIsActive()){
            goForward(4873);
            rotate(-127);
            goForward(4840);
            rotate(-90);
            goForward(4772);
            rotate(127);
            goForward(4604);
            rotate(46);
            goForward(6788);
            rotate(135);
            goForward(4648);

        }
    }
    public void motorReset() {
        leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void powerBusy() {
        leftWheel.setPower(0.5);
        rightWheel.setPower(0.5);
        while ((rightWheel.isBusy() && leftWheel.isBusy())){}
        leftWheel.setPower(0);
        rightWheel.setPower(0);
    }
    public void goForward(int gofront){
        motorReset();
        rightWheel.setTargetPosition(gofront);
        leftWheel.setTargetPosition(gofront);
        powerBusy();
    }
    private void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }
    private double getAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;
        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;
        globalAngle += deltaAngle;
        lastAngles = angles;
        return globalAngle;
    }
    private void rotate(int degrees) {
        double leftPower, rightPower;
        resetAngle();
        if (degrees < 0) {   // turn right.
            leftPower = 0.5;
            rightPower = -0.5;
        }
        else if (degrees > 0) {   // turn left.
            leftPower = -0.5;
            rightPower = 0.5;
        }
        else return;
        leftWheel.setPower(leftPower);
        rightWheel.setPower(rightPower);
        if (degrees < 0) {//right
            while (opModeIsActive() && getAngle() == 0) {}
            while (opModeIsActive() && getAngle() > degrees) {}
        } else {//left
            while (opModeIsActive() && getAngle() < degrees) {}
        }
        rightWheel.setPower(0);
        leftWheel.setPower(0);
        sleep(1000);
        resetAngle();
    }
}