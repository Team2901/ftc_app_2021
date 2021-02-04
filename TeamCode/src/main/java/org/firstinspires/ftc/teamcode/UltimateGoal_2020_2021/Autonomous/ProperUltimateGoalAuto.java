package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Proper Autonomous")
public class ProperUltimateGoalAuto extends BaseUltimateGoalAuto {

    private ElapsedTime runtime = new ElapsedTime();
    boolean debugging = false;

    public ProperUltimateGoalAuto() {
        super(TeamColor.BLUE_TEAM);
    }

    public void extendWobbleArm(boolean extending) {
        if (extending && opModeIsActive()) {
            robot.wobbleElbow.setTargetPosition(13500);
            robot.wobbleElbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.wobbleElbow.setPower(1);
        }
        if (!extending && opModeIsActive()) {
            robot.wobbleElbow.setTargetPosition(0);
            robot.wobbleElbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.wobbleElbow.setPower(-1);
        }
    }

    public void waitForContinue() {
        if (debugging) {
            while (!gamepad1.a && opModeIsActive()) {
                if (gamepad1.dpad_up) {
                    forwardMotorPower = .75;
                } else if (gamepad1.dpad_down) {
                    forwardMotorPower = .5;
                }
            }
        }
    }

    public void goToA() {
        moveInchesForward(12, true);
        releaseWobble();
        moveInchesCenter(12);
    }

    public void goToB() {
        moveInchesForward(45, true);
        turnToDesiredAngle(180);
        moveInchesCenter(12);
        releaseWobble();
        moveInchesForward(12, true);
        moveInchesCenter(-24);
        turnToDesiredAngle(0);
        moveInchesForward(24, false);
    }

    public void goToC() {
        moveInchesForward(69, true);
        releaseWobble();
        moveInchesCenter(12);
        moveInchesForward(-60, false);
    }

    public void afterA(){
        waitForContinue();
        moveInchesForward(-42, false);
        turnToDesiredAngle(90);
        waitForContinue();
        moveInchesCenter(-6);
        grabWobble();
        turnToDesiredAngle(0);
        waitForContinue();
        moveInchesForward(48, true);
        waitForContinue();
        goToA();
    }

    public void afterB(){
        moveInchesCenter(-34);
        waitForContinue();
        moveInchesForward(-42, false);
        turnToDesiredAngle(90);
        moveInchesCenter(-6);
        grabWobble();
        waitForContinue();
        turnToDesiredAngle(0);
        moveInchesForward(48, true);
        waitForContinue();
        goToB();
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

        grabWobble();

        moveInchesCenter(-12);

        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() < 1 && opModeIsActive()) {
        }

        starterStackResult = starterStackSensor();

        moveInchesCenter(12);

        extendWobbleArm(true);

        moveInchesForward(60, true);

        turnToDesiredAngle(0);

        ringShot(3);

        moveInchesCenter(-20);

        if (starterStackResult == 0) {
            waitForContinue();
            goToA();

        } else if (starterStackResult == 1) {
            waitForContinue();
            goToB();
            moveInchesForward(-24, false);
            waitForContinue();
            robot.intakeMotor.setPower(.5);
            robot.intakeMotor.setPower(-.5);
            moveInchesForward(-15, false);
            moveInchesForward(3, false);
            robot.intakeMotor.setPower(0);
            robot.intakeMotor.setPower(0);
            waitForContinue();
            moveInchesCenter(22);
            moveInchesForward(24, true);
            waitForContinue();
            ringShot(1);
            moveInchesForward(10, true);
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
