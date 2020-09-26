package org.firstinspires.ftc.teamcode.Autonomous.BlueTeam;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue Quarry SkyStone Park Bridge (14pt)", group = "_BLUE")
public class BlueQuarrySkyStoneParkBridgeNoWaffle extends BlueSkyStoneAuto {

    @Override
    public void runOpMode() throws InterruptedException {
        init(true, true, true, true, false, null);
        waitForStart();
        quarrySkyStoneParkBridge(false, true, false);
    }
}