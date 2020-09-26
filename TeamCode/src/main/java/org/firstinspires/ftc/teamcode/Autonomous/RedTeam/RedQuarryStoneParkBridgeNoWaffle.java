package org.firstinspires.ftc.teamcode.Autonomous.RedTeam;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Red Quarry Stone Park Bridge No Waffle (7pt)", group = "_RED")
public class RedQuarryStoneParkBridgeNoWaffle extends RedSkyStoneAuto {

    @Override
    public void runOpMode() throws InterruptedException {

        init(false, true, true, true, false, null);
        waitForStart();
        quarrySkyStoneParkBridge(true, false, false);
    }
}
