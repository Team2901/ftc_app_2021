package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Tank Drive Proper Autonomous")
public class ProperTankDriveAuto extends BaseUltimateGoalAuto{

    private ElapsedTime runtime = new ElapsedTime();

    public ProperTankDriveAuto(TeamColor teamColor) { super(TeamColor.BLUE_TEAM); }

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


        moveInchesForward(12, true);

        turnToDesiredAngle(90);

        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() < 1){}

        starterStackResult = starterStackSensor();

        turnToDesiredAngle(0);

        moveInchesForward(48, true);

        ringShot(3);

        if (starterStackResult == 1) {
            extendWobbleArm(true);
            moveInchesForward(36, true);
            releaseWobble();
            extendWobbleArm(false);
            moveInchesForward(24, false);

        } else {
            moveInchesForward(6, false);
        }



        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
        }
    }
}
