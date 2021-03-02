package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.QualifierUltimateGoalHardware.SHOOTER_VELOCITY;


@Autonomous(name = "Experimental Autonomous")
public class ProperDiagonalAuto extends BaseUltimateGoalAuto{
    private ElapsedTime runtime = new ElapsedTime();
    boolean debugging = false;
    private static final double TAKE_A_LOOKSIE = 12;
    boolean shootRings = true;

    public ProperDiagonalAuto() {
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
        moveInchesDiagonal(12, -16, true);
        releaseWobble();
        extendWobbleArm(false);
    }

    public void goToB() {
        moveInchesForward(36, true);
        releaseWobble(); // 15 points
        //park on launch line, 5 points
        extendWobbleArm(false);
        moveInchesForward(-24, false);
    }

    public void goToC() {
        moveInchesDiagonal(60, -16, true);
        releaseWobble();
        extendWobbleArm(false);
        moveInchesForward(-48, true);
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
        moveInchesCenter(-TAKE_A_LOOKSIE);

        // Reads the starter stack and returns stackID
        starterStackResult = starterStackSensor();

        // Move back to starting position
        moveInchesCenter(TAKE_A_LOOKSIE);

        // Starts extending wobble arm
        extendWobbleArm(true);

        /*
        robot.shooterMotor.setPower(.8);
        robot.shooterMotor2.setPower(.8);
         */

        robot.shooterMotor.setVelocity(SHOOTER_VELOCITY);
        robot.shooterMotor2.setVelocity(SHOOTER_VELOCITY);

        // Move forward by 60 inches
        moveInchesForward(64, true);

        // Straighten up to face angle 0
        turnToDesiredAngle(0);

        // Shoots 3 rings
        if(shootRings){
            ringShot(3);
        }

        robot.shooterMotor.setVelocity(0);
        robot.shooterMotor2.setVelocity(0);


        // Runs toward the different blocks depending on number of rings in starter stack
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


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
        }
    }
}
