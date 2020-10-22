package org.firstinspires.ftc.teamcode.RoverRuckus.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_DEPOT;

@Disabled
@Autonomous(name = "RoverRuckus Blue Depot", group = "2019_RoverRuckus")
public class BlueDepotRoverRuckusAuto extends BaseRoverRuckusAuto {

    public BlueDepotRoverRuckusAuto() {
        super(BLUE_DEPOT);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        runOpModeDepotCorner();

        while (opModeIsActive()) {
            idle();
        }
    }
}
