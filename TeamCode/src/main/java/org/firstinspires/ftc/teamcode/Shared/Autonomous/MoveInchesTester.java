package org.firstinspires.ftc.teamcode.Shared.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Autonomous.BaseUltimateGoalAuto;

@Autonomous(name = "Moving Tester", group = "Testers")
public class MoveInchesTester extends BaseUltimateGoalAuto {
    public MoveInchesTester() {
        super(TeamColor.BLUE_TEAM);
    }

    public void runOpMode(){
        moveInchesForward(60, true);

        moveInchesForward(10, true);
    }
}
