package org.firstinspires.ftc.teamcode.PGPathfinder;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name = "PGP Example Mechanum", group = "PGP")
public class ExamplePGPathfinderMechanum extends BasePGPathfinderMechanum {
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