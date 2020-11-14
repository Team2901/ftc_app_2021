package org.firstinspires.ftc.teamcode.UltimateGoal.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Scrimmage Blue Park", group = "Blue Team")
public class ScrimmageBlueParking extends BaseUltimateGoalAuto {
    public ScrimmageBlueParking() {
        super(TeamColor.BLUE_TEAM);
    }

    @Override
    public void runOpMode(){
        waitForStart();
        moveInchesCenter(12);
        moveInchesForward(72, false);
    }
}
