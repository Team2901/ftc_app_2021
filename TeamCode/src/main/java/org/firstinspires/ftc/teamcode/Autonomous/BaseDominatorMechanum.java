package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public abstract class BaseDominatorMechanum extends LinearOpMode implements Dominator {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor bl;
    private DcMotor br;
    private BNO055IMU imu;

    private int globalAngle;
    private Orientation lastAngles = new Orientation();

    @Override
    public void initRobot() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !imu.isGyroCalibrated()) {
            sleep(50);
            idle();
        }
        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");
    }

    public void motorReset() {
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void powerBusy() {
        fl.setPower(0.5);
        fr.setPower(0.5);
        bl.setPower(0.5);
        br.setPower(0.5);
        while ((fl.isBusy() && fr.isBusy()) && (bl.isBusy() && br.isBusy())) {
        }
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    public void goForward(int gofront) {
        motorReset();
        fl.setTargetPosition((int) Math.round(1.0 * gofront));
        fr.setTargetPosition((int) Math.round(-1.0 * gofront));
        bl.setTargetPosition((int) Math.round(1.0 * gofront));
        br.setTargetPosition((int) Math.round(1.0 * gofront));
        powerBusy();
    }

    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    public double getAngle() {
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

    public void rotate(int degrees) {
        double flp, frp, blp, brp;
        resetAngle();
        if (degrees < 0) {   // turn right.
            flp = 0.5;
            frp = 0.5;
            blp = 0.5;
            brp = 0.5;
        } else if (degrees > 0) {   // turn left.
            flp = -0.5;
            frp = -0.5;
            blp = -0.5;
            brp = -0.5;
        } else return;
        fl.setPower(flp);
        fr.setPower(frp);
        bl.setPower(blp);
        br.setPower(brp);
        if (degrees < 0) {//right
            while (opModeIsActive() && getAngle() == 0) {
            }
            while (opModeIsActive() && getAngle() > degrees) {
            }
        } else {//left
            while (opModeIsActive() && getAngle() < degrees) {
            }
        }
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
        sleep(1000);
        resetAngle();
    }
}