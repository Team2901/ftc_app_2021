package org.firstinspires.ftc.teamcode.RoverRuckus.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.RoverRuckus.Autonomous.BaseRoverRuckusAuto;

import static org.firstinspires.ftc.teamcode.RoverRuckus.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_CRATER;

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
