package org.firstinspires.ftc.teamcode.SkyStone.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SkyStone.Hardware.CompetitionSkystoneHardware;
import org.firstinspires.ftc.teamcode.Shared.Hardware.ExemplaryBlinkinLED;

@Disabled
@TeleOp(name = "Test LED Team Color", group = "TEST")
public class TestLEDTeamColor extends OpMode {

    CompetitionSkystoneHardware robot = new CompetitionSkystoneHardware();

    @Override
    public void init() {
        robot.initBlinkinLED(hardwareMap);
        robot.blinkinLED.setTeamPattern(ExemplaryBlinkinLED.TeamColorPattern.SOLID);
        telemetry.update();
    }

    @Override
    public void loop() {

    }
}
