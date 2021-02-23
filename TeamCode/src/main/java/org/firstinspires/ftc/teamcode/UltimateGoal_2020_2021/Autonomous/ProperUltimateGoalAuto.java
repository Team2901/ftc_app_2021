package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

@Autonomous(name = "Proper Autonomous")
public class ProperUltimateGoalAuto extends BaseUltimateGoalAuto {

    private ElapsedTime runtime = new ElapsedTime();
    final double SHOOTER_MAX_SPEED = (4800 * 28) / 60;

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

        nextStep("Moving to Starter Stack Acquisition Position");

        grabWobble();

        moveInchesForward(6, true);

        turnToDesiredAngle(30);

        safeWait(500);

        nextStep("Read Starter Stack");

        starterStackResult = starterStackSensor();

        nextStep("Starter Stack Result = " + starterStackResult);

        turnToDesiredAngle(0);

        safeWait(250);

        robot.shooterMotor.setVelocity(.5*SHOOTER_MAX_SPEED);
        robot.shooterMotor2.setVelocity(.5*SHOOTER_MAX_SPEED);

        nextStep("Move to Shooting Position");

        moveInchesForward(54, true);

        turnToDesiredAngle(10);

        nextStep("Shoot the rings");

        ringShot(3);

        //#GP #WINNING #QUACKTASTIC DUCKUMENTARY

        robot.shooterMotor.setVelocity(0);
        robot.shooterMotor2.setVelocity(0);

        safeWait(250);

        extendWobbleArm(true);

        if(starterStackResult == 0) {
            nextStep("Move to dropzone A");
            //Destination A
            turnToDesiredAngle(45);
            moveInchesForward(15.5, true);
            turnToDesiredAngle(0);
            nextStep("Drop Wobble");
            releaseWobble();
            safeWait(500);
            extendWobbleArm(false);
        } else if(starterStackResult == 1) {
            nextStep("Move to dropzone B");
            //Destination B
            turnToDesiredAngle(0);
            moveInchesForward(32, true);
            nextStep("Drop Wobble");
            releaseWobble();
            safeWait(500);
            extendWobbleArm(false);
            nextStep("Move to park position");
            turnToDesiredAngle(180);
            moveInchesForward(20, false);
        } else if(starterStackResult == 2) {
            nextStep("Move to dropzone C");
            //Destination C
            moveInchesForward(61, true);
            turnToDesiredAngle(0);
            nextStep("Drop Wobble");
            releaseWobble();
            safeWait(500);
            extendWobbleArm(false);
            nextStep("Move to park position");
            turnToDesiredAngle(180);
            moveInchesForward(48, false);
        } else {
            telemetry.addData("How is this not working", "something is very very wrong");
        }

        nextStep("Done");

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
        }
    }
}
