package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Utility.CountDownTimer;

@TeleOp(name = "Countdown Timer Test", group = "Shared Test")
public class TestCountdownTimer extends OpMode {
    CountDownTimer countDownTimer;

    @Override
    public void init() {
        countDownTimer = new CountDownTimer(ElapsedTime.Resolution.MILLISECONDS);
    }

    @Override
    public void loop() {
        if(gamepad1.a){
            countDownTimer.setTargetTime(10000);
        }
        telemetry.addData("Remaining Time", countDownTimer.getRemainingTime());
        telemetry.addData("Has Remaining Time", countDownTimer.hasRemainingTime());

        telemetry.update();
    }
}
