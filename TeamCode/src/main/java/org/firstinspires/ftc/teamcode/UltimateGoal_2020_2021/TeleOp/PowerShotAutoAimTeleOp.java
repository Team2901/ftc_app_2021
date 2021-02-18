package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Power Shot Auto Aim", group = "2021_UltimateGoal")
public class PowerShotAutoAimTeleOp extends KevinQualifierUltimateGoalTeleOp {
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        //use auto aim code from KevinUltimateGoalTeleOp

        //once facing correct angle, stop auto aim

        //use x and y distances from the image in atan(x/y) to get angle

        //move in direction of robot angle +/- previous angle for distance between image and power shot

        //pause and shoot

        //move to next power shot
    }
}
