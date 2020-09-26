package org.firstinspires.ftc.teamcode.Autonomous.BlueTeam;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue Foundation Park Bridge (5pt)", group = "_BLUE")
public class BlueFoundationParkBridge extends BlueSkyStoneAuto {
    @Override
    public void runOpMode() throws InterruptedException {
        init(false, true, true, true, false, null);
        waitForStart();
        this.park (PARK_BRIDGE_INCHES, -90);
    }
}
