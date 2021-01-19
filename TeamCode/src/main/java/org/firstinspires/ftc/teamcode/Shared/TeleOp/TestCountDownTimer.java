package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.Utility.CountDownTimer;

@TeleOp(name = "CountDownTimer Test", group = "Shared Test")
public class TestCountDownTimer extends OpMode {

    ImprovedGamepad impGamepad;
    ElapsedTime timer = new ElapsedTime();

    CountDownTimer countDownTimer = new CountDownTimer(ElapsedTime.Resolution.MILLISECONDS);

    double duration = 5 * 1000; // 5 seconds

    @Override
    public void init() {
        impGamepad = new ImprovedGamepad(this.gamepad1, this.timer, "GP1");
    }

    @Override
    public void loop() {
        impGamepad.update();

        if (impGamepad.a.isPressed()) {
            countDownTimer.reset(duration);
        }

        if (impGamepad.b.isInitialPress()) {
            countDownTimer.reset(duration);
        }

        double rawRemainingTime = countDownTimer.getRawRemainingTime();
        double remainingTime = countDownTimer.getRemainingTime();
        boolean hasRemainingTime = countDownTimer.hasRemainingTime();

        telemetry.addData("Button A", "Reset timer (while pressed)");
        telemetry.addData("Button B", "Reset timer (on initial press)");
        telemetry.addData("has remaining", "%b", hasRemainingTime);
        telemetry.addData("remaining (ms)", "%.1f", remainingTime);
        telemetry.addData("raw remaining (ms)", "%.1f", rawRemainingTime);
        telemetry.addData("duration (ms)", "%.1f", duration);
        telemetry.update();
    }
}
