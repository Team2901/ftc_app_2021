package org.firstinspires.ftc.teamcode.PGP;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous (name = "DominatorXDrive9000", group = "DuckSquad")
public abstract class DominatorXDrive9000 extends BaseDominatorXDrive {
    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();
        waitForStart();
        if (opModeIsActive()) {
            goForward(3071);
            rotate(-89);
            goForward(8531);
            rotate(90);
            goForward(1007);
            rotate(179);
            goForward(1988);
            rotate(-89);
            goForward(4203);

        }
    }
}
