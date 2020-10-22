package org.firstinspires.ftc.teamcode.SkyStone.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import static org.firstinspires.ftc.teamcode.Shared.Hardware.ExemplaryBlinkinLED.LED_BLUE;

@Disabled
@Autonomous(name = "Blue Quarry SkyStone Park Bridge (19pt)", group = "_BLUE")
public class BlueQuarrySkyStoneParkBridge extends BaseSkyStoneAuto {

    @Override
    public void runOpMode() throws InterruptedException {
        init(true, true, true, true, false, null, LED_BLUE);
        waitForStart();
        quarrySkyStoneParkBridge(false, true, true);
    }
}