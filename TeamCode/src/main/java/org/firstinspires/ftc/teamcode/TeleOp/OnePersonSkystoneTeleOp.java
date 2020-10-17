package org.firstinspires.ftc.teamcode.TeleOp;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.Hardware.BuilderSkystoneHardware;
import org.firstinspires.ftc.teamcode.Hardware.CompetitionSkystoneHardware;
import org.firstinspires.ftc.teamcode.Utility.AngleUtilities;

@Disabled
@SuppressLint("DefaultLocale")
@TeleOp(name= "Competition Skystone - One Person", group = "BUILDER_SKYSTONE")
public class OnePersonSkystoneTeleOp extends OpMode {

    public CompetitionSkystoneHardware robot = new CompetitionSkystoneHardware();
    public ElapsedTime timer = new ElapsedTime();
    public ImprovedGamepad improvedGamepad1;
    public ImprovedGamepad improvedGamepad2;

    public double wheelPowerRatio = BuilderSkystoneHardware.WHEEL_POWER_RATIO;

    @Override
    public void init() {
        robot.init(hardwareMap);
       // robot.swerveStraight(0, 0);
        this.improvedGamepad1 = new ImprovedGamepad(gamepad1, timer, "g1");
        this.improvedGamepad2 = new ImprovedGamepad(gamepad2, timer, "g2");
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       }

    @Override
    public void loop() {


        improvedGamepad1.update();
        improvedGamepad2.update();

        boolean pause = improvedGamepad1.start.isPressed()|| improvedGamepad1.back.isPressed() || improvedGamepad1.guide.isPressed();

        // WHEEL CONTROL
        if (improvedGamepad1.right_stick_x.isPressed()) {
            double power = getWheelPower(improvedGamepad1.right_stick.x.getValue(), pause);
            robot.swerveTurn(power);
        } else if (improvedGamepad1.left_stick.isPressed()) {
            double power = getWheelPower(improvedGamepad1.left_stick.getValue(), pause);
            double joyWheelAngle = improvedGamepad1.left_stick.getAngel();
            robot.swerveStraight(joyWheelAngle, power);
        } else {
            robot.setWheelMotorPower(0, 0, 0, 0);
        }

        //LIFT CONTROL
        if (gamepad1.left_trigger > .2) {
            robot.lift.setPower(-.5);
        } else if (gamepad1.right_trigger > .2) {
            robot.lift.setPower(1);
        } else {
            robot.lift.setPower(0);
        }
//CRANE CONTROL
        if (gamepad1.right_bumper) {
            robot.crane.setPosition(robot.crane.getPosition() + .015);
        } else if (gamepad1.left_bumper) {
            robot.crane.setPosition(robot.crane.getPosition() - .015);
        }
//WRIST CONTROL
        if (gamepad1.x) {
            robot.wrist.setPosition(robot.wrist.getPosition() + .01);
        } else if (gamepad1.y) {
            robot.wrist.setPosition(robot.wrist.getPosition() - .01);
        }
//JAW CONTROL
        if (gamepad1.a) {
            robot.jaw.setPosition(robot.jaw.getPosition() + .01);
        } else if (gamepad1.b) {
            robot.jaw.setPosition(robot.jaw.getPosition() - .01);
        }
//Waffle Grabber
        if (gamepad1.dpad_up) {
            robot.setGrabberPositition(.7, .84);
        } else if (gamepad1.dpad_down) {
            robot.setGrabberPositition(0, 0);
        }

        if (improvedGamepad2.right_bumper.isInitialPress()) {
            wheelPowerRatio += .05;
        } else if (improvedGamepad2.left_bumper.isInitialPress()) {
            wheelPowerRatio -= .05;
        }

        telemetry.addData("wheelPowerRatio", wheelPowerRatio);
telemetry.addData("crane", robot.crane.getPosition());
        telemetry.addData("FL", String.format("angle: %.2f, mod: %d, pos: %.2f",
                robot.frontLeftSwerveWheel.targetAngle, robot.frontLeftSwerveWheel.modifier, robot.frontLeftSwerveWheel.wheelAngleToServoPosition()));
        telemetry.addData("FR", String.format("angle: %.2f, mod: %d, pos: %.2f",
                robot.frontRightSwerveWheel.targetAngle, robot.frontRightSwerveWheel.modifier, robot.frontRightSwerveWheel.wheelAngleToServoPosition()));
        telemetry.addData("BL", String.format("angle: %.2f, mod: %d, pos: %.2f",
                robot.backLeftSwerveWheel.targetAngle, robot.backLeftSwerveWheel.modifier, robot.backLeftSwerveWheel.wheelAngleToServoPosition()));
        telemetry.addData("BR", String.format("angle: %.2f, mod: %d, pos: %.2f",
                robot.backRightSwerveWheel.targetAngle, robot.backRightSwerveWheel.modifier, robot.backRightSwerveWheel.wheelAngleToServoPosition()));

        telemetry.addData("lift position" , robot.lift.getCurrentPosition() );
        telemetry.addData("Jaw position" , robot.jaw.getPosition() );
        telemetry.addData("Wrist position" , robot.wrist.getPosition() );
        telemetry.addData("Crane Position" , robot.crane.getPosition() );

        telemetry.addData("", robot.frontLeftSwerveWheel.toString());
        telemetry.addData("", robot.frontRightSwerveWheel.toString());
        telemetry.addData("", robot.backLeftSwerveWheel.toString());
        telemetry.addData("", robot.backRightSwerveWheel.toString());
        telemetry.update();
    }

    public double joystickPositionToWheelAngle(double joystickPositionX, double joystickPositionY) {
        double wheelAngleRad = Math.atan2(joystickPositionY, joystickPositionX);
        double wheelAngle = AngleUtilities.radiansDegreesTranslation(wheelAngleRad) - 90;
        return AngleUtilities.getPositiveNormalizedAngle(wheelAngle);
    }

    public double getWheelPower(double radius, boolean pause) {
        if (pause) {
            return 0;
        } else {
            return radius * wheelPowerRatio;
        }
    }
}
