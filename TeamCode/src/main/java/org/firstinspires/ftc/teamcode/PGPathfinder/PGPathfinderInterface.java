package org.firstinspires.ftc.teamcode.PGPathfinder;

public interface PGPathfinderInterface {

    void motorReset();

    void powerBusy();

    void goForward(int goFront);

    void resetAngle();

    double getAngle();

    void rotate(int degrees);

    void initRobot();
}
