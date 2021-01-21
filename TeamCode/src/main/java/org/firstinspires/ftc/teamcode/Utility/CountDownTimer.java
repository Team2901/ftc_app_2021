package org.firstinspires.ftc.teamcode.Utility;

import com.qualcomm.robotcore.util.ElapsedTime;

public class CountDownTimer {

    private final ElapsedTime elapsedTime;
    private double startTime = 0;

    public CountDownTimer() {
        this(ElapsedTime.Resolution.MILLISECONDS, 0);
    }

    public CountDownTimer(ElapsedTime.Resolution resolution) {
        this(resolution, 0);
    }

    public CountDownTimer(ElapsedTime.Resolution resolution, double startTime) {
        this.elapsedTime = new ElapsedTime(resolution);
        this.reset(startTime);
    }

    public void reset() {
        reset(this.startTime);
    }

    public void reset(double startTime) {
        this.startTime = startTime;
        elapsedTime.reset();
    }

    public double getRawRemainingTime() {
        return startTime - elapsedTime.time();
    }

    public double getRemainingTime() {
        double rawRemainingTime = getRawRemainingTime();
        return Math.max(rawRemainingTime, 0);
    }

    public boolean hasRemainingTime() {
        return getRemainingTime() > 0;
    }
}
