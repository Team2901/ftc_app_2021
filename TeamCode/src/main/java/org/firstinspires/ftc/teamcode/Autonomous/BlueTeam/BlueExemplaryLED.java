package org.firstinspires.ftc.teamcode.Autonomous.BlueTeam;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue Exemplary LED", group = "_BLUE")
public class BlueExemplaryLED extends BlueSkyStoneAuto {
    @Override
    public void runOpMode() throws InterruptedException {
        init(false, false, true, false, false, null);
        waitForStart();
    }
}
