package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Autonomous;

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
