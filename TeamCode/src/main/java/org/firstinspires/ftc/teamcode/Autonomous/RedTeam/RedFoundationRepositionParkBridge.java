package org.firstinspires.ftc.teamcode.Autonomous.RedTeam;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name = "Red Foundation Reposition Park Bridge", group = "_RED")
public class RedFoundationRepositionParkBridge extends RedSkyStoneAuto {

    @Override
    public void runOpMode() throws InterruptedException {
        init(false, true, true, true, false, null);
        waitForStart();
        platformParkInner(Color.RED);
    }
}
