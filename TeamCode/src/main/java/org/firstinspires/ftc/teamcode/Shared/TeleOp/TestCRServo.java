package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@Disabled
@TeleOp (name = "CRServo Test" , group = "TEST")
public class TestCRServo extends OpMode {

    CRServo crServo;

    @Override
    public void init() {
       crServo = hardwareMap.crservo.get("crServo");
    }

    @Override
    public void loop() {
    crServo.setPower( - gamepad1.right_stick_y);

    telemetry.addData("Joystick" , gamepad1.right_stick_y);
    telemetry.addData("crServo" , crServo.getPower());
    telemetry.update();
    }
}
