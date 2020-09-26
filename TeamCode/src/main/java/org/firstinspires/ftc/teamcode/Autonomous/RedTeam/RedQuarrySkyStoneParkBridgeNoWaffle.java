package org.firstinspires.ftc.teamcode.Autonomous.RedTeam;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Red Quarry SkyStone Park Bridge No Waffle (15pt)", group = "_RED")
public class RedQuarrySkyStoneParkBridgeNoWaffle extends RedSkyStoneAuto {

    @Override
    public void runOpMode() throws InterruptedException {
        init(true, true, true, true, false, null);
        waitForStart();
        quarrySkyStoneParkBridge(true, true, false);
    }
}