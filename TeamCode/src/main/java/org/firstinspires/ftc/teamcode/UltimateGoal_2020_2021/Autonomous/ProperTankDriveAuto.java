package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Tank Drive Proper Autonomous V7")
public class ProperTankDriveAuto extends BaseUltimateGoalAuto{

    private ElapsedTime runtime = new ElapsedTime();

    final double SHOOTER_MAX_SPEED = (4800 * 28) / 60;

    public ProperTankDriveAuto() { super(TeamColor.BLUE_TEAM); }

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

        grabWobble();

        robot.shooterMotor.setVelocity(.2*SHOOTER_MAX_SPEED);
        robot.shooterMotor2.setVelocity(.2*SHOOTER_MAX_SPEED);

        moveInchesForward(60, true);

        ringShot(1);

        turnToDesiredAngle((float) -6.75);

        ringShot(1);

        turnToDesiredAngle((float)-11.75);

        ringShot(1);

        turnToDesiredAngle(1);

        robot.shooterMotor.setVelocity(0);
        robot.shooterMotor2.setVelocity(0);

        extendWobbleArm(true);
        moveInchesForward(32, true);
        releaseWobble();
        safeWait(1000);
        turnToDesiredAngle(180);
        extendWobbleArm(false);
        moveInchesForward(24, false);



        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
        }
    }
}
