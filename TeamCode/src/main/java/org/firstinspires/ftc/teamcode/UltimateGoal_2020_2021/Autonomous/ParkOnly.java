package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Park to not run into wall")
public class ParkOnly extends BaseUltimateGoalAuto {

    private ElapsedTime runtime = new ElapsedTime();
    boolean debugging = false;
    private static final double TAKE_A_LOOKSIE = 15;
    boolean shootRings = true;

    public ParkOnly() {
        super(TeamColor.BLUE_TEAM);
    }

    public void extendWobbleArm(boolean extending) {
        if (extending && opModeIsActive()) {
            robot.wobbleElbow.setTargetPosition(5626);
            robot.wobbleElbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.wobbleElbow.setPower(1);
        }
        if (!extending && opModeIsActive()) {
            robot.wobbleElbow.setTargetPosition(0);
            robot.wobbleElbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.wobbleElbow.setPower(-1);
        }
    }

    /**
     * If debugging is true, waits for the driver to press the a button.
     * While a has not been pressed, dpad up sets the forwardMotorPower to 0.75 while dpad down sts the forwardMotorPower to 0.5
     */
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
        moveInchesCenter(-20);
        moveInchesForward(18, true);
        releaseWobble();
        extendWobbleArm(false);
        moveInchesForward(-6, true);
        moveInchesCenter(12);
    }

    public void goToB() {
        moveInchesForward(45, true);
        releaseWobble(); // 15 points
        //park on launch line, 5 points
        turnToDesiredAngle(0);
        extendWobbleArm(false);
        moveInchesForward(-24, false);
    }

    public void goToC() {
        moveInchesCenter(-20);
        moveInchesForward(69, true);
        releaseWobble();
        extendWobbleArm(false);
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

    public void unknownBCode(){
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

        // Closes the wobble grabber
        grabWobble();

        moveInchesForward(.5, false);

        // Moves 12 inches to be in front of the starter stack
        //oveInchesCenter(-TAKE_A_LOOKSIE);

        // Reads the starter stack and returns stackID
        starterStackResult = starterStackSensor();

        // Move back to starting position
        //moveInchesCenter(TAKE_A_LOOKSIE);

        // Starts extending wobble arm
        extendWobbleArm(true);

        /*
        robot.shooterMotor.setPower(.8);
        robot.shooterMotor2.setPower(.8);
         */

        robot.shooterMotor.setPower(.8);
        robot.shooterMotor2.setPower(.8);

        // Move forward by 60 inches
        moveInchesForward(60, true);

        // Straighten up to face angle 0
        turnToDesiredAngle(0);

        // Shoots 3 rings
        if(shootRings){
            ringShot(3);
        }

        robot.shooterMotor.setPower(0);
        robot.shooterMotor2.setPower(0);


        // Runs toward the different blocks depending on number of rings in starter stack
        /*
        waitForContinue();
        if (starterStackResult == 0) {
            goToA();
        } else if (starterStackResult == 1) {
            goToB();
        } else if (starterStackResult == 2) {
            goToC();
        } else {
            telemetry.addData("error", "How did this happen");
            telemetry.update();
        }

         */

        moveInchesForward(20, true);


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
        }
    }
}
