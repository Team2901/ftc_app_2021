package org.firstinspires.ftc.teamcode.Autonomous.BlueTeam;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue Quarry SkyStone Park Bridge (19pt)", group = "_BLUE")
public class BlueQuarrySkyStoneParkBridge extends BlueSkyStoneAuto {

    @Override
    public void runOpMode() throws InterruptedException {
        init(true, true, true, true, false, null);
        waitForStart();
        quarrySkyStoneParkBridge(false, true, true);
    }
}