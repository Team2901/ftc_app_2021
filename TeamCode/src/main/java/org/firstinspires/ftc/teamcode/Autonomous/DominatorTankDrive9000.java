package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created with Team 6183's Duckinator 3000
 */

@Autonomous(name = "DominatorTankDrive9000", group = "DuckSquad")
public class DominatorTankDrive9000 extends BaseDominatorTankDrive {
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