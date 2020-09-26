package org.firstinspires.ftc.teamcode.Presentation;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "PResentation TeleOP ")
@Disabled
public class PresentationTeleOp extends OpMode {

    PresentationBotHardware robot = new PresentationBotHardware();

    public void init() {

        robot.init(hardwareMap);

    }

    public void loop() {

        double left = -gamepad1.left_stick_y;
        double right = -gamepad1.right_stick_y;

        robot.leftMotor.setPower(left);
        robot.rightMotor.setPower(right);


    }

}