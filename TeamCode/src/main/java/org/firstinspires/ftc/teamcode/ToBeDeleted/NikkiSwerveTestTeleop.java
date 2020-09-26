package org.firstinspires.ftc.teamcode.ToBeDeleted;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@TeleOp (name = "swerve test teleop")
public class NikkiSwerveTestTeleop extends OpMode {
    public final static double WHEEL_SERVO_GEAR_RATIO = 1 / 1;
    public final static int SERVO_MAX_ANGLE = 190;
    Servo servoFrontLeft;
    DcMotor motorFrontLeft;

    @Override
    public void init() {
        servoFrontLeft = hardwareMap.servo.get("servoFrontLeft");
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");

    }

    @Override
    public void loop() {
        double joystickPositionX = gamepad1.left_stick_x;
        double joystickPositionY = -gamepad1.left_stick_y;

        double wheelAngle = joystickPositionToWheelAngle(joystickPositionX, joystickPositionY);
        double servoAngle = wheelServoAngle(wheelAngle);
        double servoPosition = servoAngleToPosition(servoAngle);
        servoFrontLeft.setPosition(servoPosition);
        motorFrontLeft.setPower(getPower(joystickPositionX,joystickPositionY));

        telemetry.addData("wheelAngle", wheelAngle);
        telemetry.addData("servoAngle", servoAngle);
        telemetry.addData("servoPosition", servoPosition);
        telemetry.addData("actual servo position", servoFrontLeft.getPosition());
        telemetry.addData("power", getPower(joystickPositionX,joystickPositionY));
        telemetry.update();
    }

    public double wheelServoAngle(double wheelAngle) {
        double servoAngle = WHEEL_SERVO_GEAR_RATIO * wheelAngle;
        return servoAngle;
    }

    public double servoAngleToPosition(double servoAngle) {
        double servoPosition = servoAngle / SERVO_MAX_ANGLE;
        return servoPosition;
    }

    public double joystickPositionToWheelAngle(double joystickPositionX, double joystickPositionY) {
        double wheelAngle = Math.atan2(joystickPositionY, joystickPositionX);
        wheelAngle = radiansDegreesTranslation(wheelAngle);
        wheelAngle = standardizeAngle(wheelAngle);
        return wheelAngle;
    }

    public double radiansDegreesTranslation(double radians) {
        double degrees = radians * 180 / Math.PI;
        return degrees;
    }

    // Converting -180 to 180, 0 to 360
    public double standardizeAngle(double angle) {
        return (angle + 360) % 360;
}
    public double getPower (double x, double y){
        double power = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        return power;
    }










}
