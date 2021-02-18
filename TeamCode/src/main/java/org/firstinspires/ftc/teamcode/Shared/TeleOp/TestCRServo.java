package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;


@TeleOp(name = "CRServo Test", group = "Shared Test")
public class TestCRServo extends OpMode {

    CRServo crServo;

    @Override
    public void init() {
        crServo = hardwareMap.crservo.get("backup_kicker");
    }

    @Override
    public void loop() {
        if(gamepad1.left_bumper){
            crServo.setPower(-1);
        } else if(gamepad1.right_bumper){
            crServo.setPower(1);
        } else {
            crServo.setPower(-gamepad1.right_stick_y);
        }

        telemetry.addData("Joystick", gamepad1.right_stick_y);
        telemetry.addData("crServo", crServo.getPower());
        telemetry.update();
    }
}
