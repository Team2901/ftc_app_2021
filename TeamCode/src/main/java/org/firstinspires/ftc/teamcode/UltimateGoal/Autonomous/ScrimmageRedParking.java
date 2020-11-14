package org.firstinspires.ftc.teamcode.UltimateGoal.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Scrimmage Red Park", group = "Red Team")
public class ScrimmageRedParking extends BaseUltimateGoalAuto {
    public ScrimmageRedParking() {
        super(TeamColor.RED_TEAM);
    }

    @Override
    public void runOpMode(){
        waitForStart();
        moveInchesCenter(12);
        moveInchesForward(72, false);
    }
}
