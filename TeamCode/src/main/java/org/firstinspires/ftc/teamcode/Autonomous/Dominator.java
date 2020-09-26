package org.firstinspires.ftc.teamcode.Autonomous;

public interface Dominator {

    void motorReset();

    void powerBusy();

    void goForward(int goFront);

    void resetAngle();

    double getAngle();

    void rotate(int degrees);

    void initRobot();
}
