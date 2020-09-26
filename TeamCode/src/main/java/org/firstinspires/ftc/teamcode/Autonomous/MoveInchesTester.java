package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Move Inches Tester", group = "__TEST")
public class MoveInchesTester extends BaseSkyStoneAuto{
    @Override
    public void runOpMode() throws InterruptedException {
            robot.init(hardwareMap);
            robot.swerveStraight(0,0);
        waitForStart();

        robot.setWheelMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setWheelMotorMode(DcMotor.RunMode.RUN_USING_ENCODER);

        moveInchesAbsolute(0, 6*12, .1);
        //measure inches traveled
        //divide by encorders
        // equal encoders to inches


        while (opModeIsActive())
        {

        }
    }
}
