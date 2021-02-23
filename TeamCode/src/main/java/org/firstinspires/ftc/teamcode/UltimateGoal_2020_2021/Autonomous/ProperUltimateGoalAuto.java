package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.QualifierUltimateGoalHardware.SHOOTER_VELOCITY;

@Autonomous(name = "Proper Autonomous")
public class ProperUltimateGoalAuto extends BaseUltimateGoalAuto {

    private ElapsedTime runtime = new ElapsedTime();
    final double SHOOTER_MAX_SPEED = (4800 * 28) / 60;
    boolean debugging = false;
    private static final double TAKE_A_LOOKSIE = 15;
    boolean shootRings = true;

    public ProperUltimateGoalAuto() {
        super(TeamColor.BLUE_TEAM);
    }

    public void extendWobbleArm(boolean extending) {
        if (extending && opModeIsActive()) {
            robot.wobbleElbow.setTargetPosition(3750);
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
        moveInchesDiagonal(12, -20, true);
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
        moveInchesDiagonal(60, -20, true);
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

        grabWobble();

        moveInchesForward(6, true);

        turnToDesiredAngle(20);

        safeWait(500);

        starterStackResult = starterStackSensor();

        turnToDesiredAngle(0);

        safeWait(250);

        robot.shooterMotor.setVelocity(.5*SHOOTER_MAX_SPEED);
        robot.shooterMotor2.setVelocity(.5*SHOOTER_MAX_SPEED);

        moveInchesForward(54, true);

        turnToDesiredAngle(10);

        ringShot(3);

        //#GP #WINNING #QUACKTASTIC DUCKUMENTARY

        robot.shooterMotor.setVelocity(0);
        robot.shooterMotor2.setVelocity(0);

        safeWait(250);

        extendWobbleArm(true);

        if(starterStackResult == 0) {
            //Destination A
            turnToDesiredAngle(45);
            moveInchesForward(15.5, true);
            turnToDesiredAngle(0);
            releaseWobble();
            safeWait(500);
            extendWobbleArm(false);
        } else if(starterStackResult == 1) {
            //Destination B
            turnToDesiredAngle(0);
            moveInchesForward(32, true);
            releaseWobble();
            safeWait(500);
            extendWobbleArm(false);
            turnToDesiredAngle(180);
            moveInchesForward(20, false);
        } else if(starterStackResult == 2) {
            //Destination C
            moveInchesForward(61, true);
            turnToDesiredAngle(0);
            releaseWobble();
            safeWait(500);
            extendWobbleArm(false);
            turnToDesiredAngle(180);
            moveInchesForward(48, false);
        } else {
            telemetry.addData("How is this not working", "something is very very wrong");
        }



        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
        }
    }
}
