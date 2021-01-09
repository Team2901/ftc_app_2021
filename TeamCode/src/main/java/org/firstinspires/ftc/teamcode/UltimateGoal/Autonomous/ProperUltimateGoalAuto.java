package org.firstinspires.ftc.teamcode.UltimateGoal.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.System.currentTimeMillis;

@Autonomous(name = "Proper Autonomous")
public class ProperUltimateGoalAuto extends BaseUltimateGoalAuto {

    private ElapsedTime runtime = new ElapsedTime();

    public ProperUltimateGoalAuto() {
        super(TeamColor.BLUE_TEAM);
    }

    public void goToA() {
        moveInchesForward(9, true);
        releaseWobble();
        moveInchesCenter(12);
    }
    public void goToB() {
        moveInchesForward(45, true);
        turnToDesiredAngle(180);
        releaseWobble();
        moveInchesForward(12, true);
        moveInchesCenter(-12);
        turnToDesiredAngle(0);
        moveInchesForward(24, false);
    }
    public void goToC() {
        moveInchesForward(69, true);
        releaseWobble();
        moveInchesCenter(12);
        moveInchesForward(-60, false);
    }

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        init(true);

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.middleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //moveInches();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        moveInchesCenter(-12);

        robot.middleMotor.setTargetPosition(15000);

        robot.middleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.middleMotor.setPower(1);

        starterStackResult = starterStackSensor();

        moveInchesCenter(12);

        moveInchesForward(60, true);

        ringShot(3);

        moveInchesCenter(-24);

        if (starterStackResult == 0) {
            while(!gamepad1.a && opModeIsActive()){}
            goToA();
            while(!gamepad1.a && opModeIsActive()){}
            moveInchesForward(-42, false);
            turnToDesiredAngle(90);
            while(!gamepad1.a && opModeIsActive()){}
            moveInchesCenter(-6);
            grabWobble();
            turnToDesiredAngle(0);
            while(!gamepad1.a && opModeIsActive()){}
            moveInchesForward(48, true);
            while(!gamepad1.a && opModeIsActive()){}
            goToA();
        } else if (starterStackResult == 1) {
            goToB();
        } else if (starterStackResult == 2) {
            goToC();
        } else {
            telemetry.addData("error", "How did this happen");
            telemetry.update();
        }



        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
        }
    }
}
