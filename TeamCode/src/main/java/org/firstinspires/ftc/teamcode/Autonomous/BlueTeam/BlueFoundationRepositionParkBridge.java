package org.firstinspires.ftc.teamcode.Autonomous.BlueTeam;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name = "Blue Foundation Reposition Park Bridge", group = "_BLUE")
public class BlueFoundationRepositionParkBridge extends BlueSkyStoneAuto {

    @Override
    public void runOpMode() throws InterruptedException {
        init(false, true, true, true, false, null);
        waitForStart();
        platformParkInner(Color.BLUE);
    }
}
