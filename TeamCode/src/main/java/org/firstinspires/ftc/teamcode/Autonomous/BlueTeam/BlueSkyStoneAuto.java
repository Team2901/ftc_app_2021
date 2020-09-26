package org.firstinspires.ftc.teamcode.Autonomous.BlueTeam;

import org.firstinspires.ftc.teamcode.Autonomous.BaseSkyStoneAuto;

import static org.firstinspires.ftc.teamcode.Hardware.ExemplaryBlinkinLED.LED_BLUE;

public class BlueSkyStoneAuto extends BaseSkyStoneAuto {
    @Override
    public void runOpMode() throws InterruptedException {
    }
    public void init(boolean initWebCam, boolean initRobot, boolean initBlinkinLED, boolean driveWheels, boolean setLiftServos, String message){
        super.init(initWebCam, initRobot, initBlinkinLED, driveWheels, setLiftServos, message, LED_BLUE);
    }
}
