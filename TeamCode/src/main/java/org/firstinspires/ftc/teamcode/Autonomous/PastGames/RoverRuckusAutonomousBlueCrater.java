package org.firstinspires.ftc.teamcode.Autonomous.PastGames;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import static org.firstinspires.ftc.teamcode.Autonomous.PastGames.BaseRoverRuckusAuto.StartCorner.BLUE_CRATER;

@Disabled
@Autonomous(name = "Crater: Full", group = "RoverRuckus")
public class RoverRuckusAutonomousBlueCrater extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousBlueCrater() {
        super(BLUE_CRATER);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        runOpModeCraterCorner();

        while(opModeIsActive()) {
            idle();
        }
    }
}
