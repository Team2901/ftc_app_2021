package org.firstinspires.ftc.teamcode.UltimateGoal.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Scrimmage Park", group = "Blue Team")
public class BlueParking extends BaseUltimateGoalAuto {
    public BlueParking() {
        super(TeamColor.BLUE_TEAM);
    }

    @Override
    public void runOpMode(){
        waitForStart();
        moveInchesCenter(12);
        moveInchesForward(72, false);
    }
}
