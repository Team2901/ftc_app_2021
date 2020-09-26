package org.firstinspires.ftc.teamcode.Presentation;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Imu Demonstration")
@Disabled
public class ImuDemonstration extends LinearOpMode {

    PresentationBotHardware robot = new PresentationBotHardware();
    ElapsedTime timer = new ElapsedTime();
    double goal = 90;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        waitForStart();
/*
This class is the demonstration in Advanced Programmning where the Robot turns 90 degrees and then
stays there. It also returns to 90 degrees if it is pushed off course
 */
        while (opModeIsActive()) {

            // Gets the current angle of the robot
            double angle = robot.getAngle();

            // Determines if the robot is in the goldilocks zone (In this case it is + or - 1)
            if (Math.abs(goal - angle) < 1) {
                robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(0);

            } else {
             //If not in Goldilocks zone, it sets power to the get power class
                robot.leftMotor.setPower(-getPower(angle));
                robot.rightMotor.setPower(getPower(angle));
            }
            telemetry.addData("Angle", angle);
            telemetry.update();
        }
    }

    double getPower(double currentPosition) {
       /*
        If under halfway to the goal, have the robot speed up by .01 for every angle until it is
        over halfway there
         */

        if (currentPosition < goal / 2) {

            return (.01 * currentPosition + (Math.signum(currentPosition) * .075));
        } else {
// Starts to slow down by .01 per angle closer to the goal.
            return (.01 * (goal - currentPosition + (Math.signum(currentPosition) * .075)));
        }
    }
}