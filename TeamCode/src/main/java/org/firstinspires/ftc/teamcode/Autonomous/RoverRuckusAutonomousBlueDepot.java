package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_DEPOT;
@Disabled
@Autonomous( name =  "Depot: Full", group = "RoverRuckus")
public class RoverRuckusAutonomousBlueDepot extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousBlueDepot() {
        super(BLUE_DEPOT);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        runOpModeDepotCorner();

        while(opModeIsActive()) {
            idle();
        }
    }
}
