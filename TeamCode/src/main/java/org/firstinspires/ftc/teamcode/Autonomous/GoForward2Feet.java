package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name = "GoForward2Feet")
public class GoForward2Feet extends BaseSkyStoneAuto {
    @Override
    public void runOpMode() throws InterruptedException {
        init(false, true, true, true, false, null, 2);
        robot.crane.setPosition(0);
        waitForStart();
        robot.crane.setPosition(.5);
        while(opModeIsActive());

    }
}
