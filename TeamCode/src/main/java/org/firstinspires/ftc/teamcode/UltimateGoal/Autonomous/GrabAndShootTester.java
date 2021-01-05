package org.firstinspires.ftc.teamcode.UltimateGoal.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Shooting test auto", group = "Red Team")
public class GrabAndShootTester extends BaseUltimateGoalAuto {
    public GrabAndShootTester() {
        super(TeamColor.RED_TEAM);
    }

    @Override
    public void runOpMode(){
        init(false);
        ElapsedTime timer = new ElapsedTime();
        waitForStart();
        robot.shooterMotor.setPower(1);
        robot.shooterMotor2.setPower(1);
        robot.intakeMotor.setPower(0.5);
        moveInchesForward(-6, false);

        timer = new ElapsedTime();
        while(timer.seconds()<= 2 && opModeIsActive()) {
        }
        moveInchesForward(5, false);

        timer = new ElapsedTime();
        robot.transferMotor.setPower(-0.5);
        while(timer.seconds()<= 2 && opModeIsActive()) {
        }
        robot.transferMotor.setPower(0);
        moveInchesForward(-12, false);
        moveInchesForward(11, false);

        timer = new ElapsedTime();
        while(timer.seconds()<= 2 && opModeIsActive()) {
        }

        timer = new ElapsedTime();
        robot.transferMotor.setPower(-0.5);
        while(timer.seconds()<= 2 && opModeIsActive()) {
        }
        robot.shooterMotor.setPower(0);
        robot.shooterMotor2.setPower(0);
        robot.transferMotor.setPower(0);
        robot.intakeMotor.setPower(0);
    }
}
