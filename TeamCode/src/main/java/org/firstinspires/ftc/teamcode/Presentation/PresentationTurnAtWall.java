package org.firstinspires.ftc.teamcode.Presentation;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "PresentationTurnatWall")
@Disabled
public class PresentationTurnAtWall extends LinearOpMode {

    PresentationBotHardware robot = new PresentationBotHardware();

    public void runOpMode() {
        // Inits robot that we creates in Hardware
        robot.init(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            //Determines distance to the nearest surface IN INCHES
            double distance = robot.distanceSensor.getDistance(DistanceUnit.INCH);
            //If less than 2ft, turn if not jeep going
            if (distance < 24) {
                turn(90);
            } else {
                robot.leftMotor.setPower(.75);
                robot.rightMotor.setPower(.75);
            }

            idle();


            telemetry.addData("Angle", robot.getAngle());
            telemetry.update();
        }


    }


    //Using The IMU to turn 90^
    double getPower(double absCurrent, double absGoal, double absStart) {

        double relCurrent = AngleUnit.normalizeDegrees(absCurrent - absStart);
        double relGoal = AngleUnit.normalizeDegrees(absGoal - absStart);
        if (relCurrent < relGoal / 2) {

            return (.01 * relCurrent + (Math.signum(relCurrent) * .075));
        } else {
            return (.01 * (relGoal - relCurrent) + (Math.signum(relGoal - relCurrent) * .025));
        }
    }

    public void turn(double relGoal) {

        ElapsedTime timer = new ElapsedTime();
        Double goalTime = null;

        double absStart = AngleUnit.normalizeDegrees(robot.getAngle());
        double absGoal = AngleUnit.normalizeDegrees(relGoal + absStart);

        while (goalTime == null || timer.time() - goalTime < 5) {
            double absCurrent = robot.getAngle();
            double power;
            if (Math.abs(absGoal - absCurrent) < 1) {
                power = 0;
                robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(0);

                if (goalTime == null) {

                    goalTime = timer.time();
                }
            } else {
                power = getPower(absCurrent, absGoal, absStart);
                robot.leftMotor.setPower(-getPower(absCurrent, absGoal, absStart));
                robot.rightMotor.setPower(getPower(absCurrent, absGoal, absStart));

                goalTime = null;
            }

            Log.e("turnAtWall", String.format("%.2f, %.2f , %.2f , %.2f", power, absGoal, absStart, absCurrent));

            telemetry.addData("Angle", absCurrent);
            telemetry.addData("abs Start", absStart);
            telemetry.addData("abs current", absCurrent);
            telemetry.addData("abs goal", absGoal);
            telemetry.addData("speed", getPower(absCurrent, absGoal, absStart));
            telemetry.update();
        }

        telemetry.addData("I BROKE FREEE", "");
        telemetry.update();
    }

}