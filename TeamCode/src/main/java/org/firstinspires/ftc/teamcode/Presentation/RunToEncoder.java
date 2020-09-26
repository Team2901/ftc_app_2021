package org.firstinspires.ftc.teamcode.Presentation;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class RunToEncoder extends LinearOpMode {

    PresentationBotHardware robot = new PresentationBotHardware();

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftMotor.setTargetPosition(5000);
        robot.rightMotor.setTargetPosition(5000);

        robot.leftMotor.setPower(.75);
        robot.rightMotor.setPower(.75);

        while(robot.leftMotor.isBusy());


    }
}
