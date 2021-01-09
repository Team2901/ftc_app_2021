package org.firstinspires.ftc.teamcode.RoverRuckus_2018_2019.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import static org.firstinspires.ftc.teamcode.RoverRuckus_2018_2019.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_CRATER;

@Disabled
@Autonomous(name = "RoverRuckus Blue Crater", group = "2019_RoverRuckus")
public class BlueCraterRoverRuckusAuto extends BaseRoverRuckusAuto {

    public BlueCraterRoverRuckusAuto() {
        super(BLUE_CRATER);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        runOpModeCraterCorner();

        while (opModeIsActive()) {
            idle();
        }
    }
}
