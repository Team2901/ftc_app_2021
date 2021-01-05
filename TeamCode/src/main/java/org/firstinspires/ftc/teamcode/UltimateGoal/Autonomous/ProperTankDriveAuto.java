package org.firstinspires.ftc.teamcode.UltimateGoal.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Tank Drive Proper Autonomous")
public class ProperTankDriveAuto extends BaseUltimateGoalAuto{

    private ElapsedTime runtime = new ElapsedTime();

    public ProperTankDriveAuto(TeamColor teamColor) { super(TeamColor.BLUE_TEAM); }

    private void moveInchesCenter(int inches){
        float startAngle = robot.getAngle();
        if(inches >= 0) {
            turnToDesiredAngle(startAngle-90);
        } else {
            turnToDesiredAngle(startAngle+90);
        }
        moveInchesForward(inches, true);
        turnToDesiredAngle(startAngle);
    }

    public void goToA() {
        moveInchesCenter(24);
        moveInchesForward(9, true);
        turnToDesiredAngle(180);
        releaseWobble();
    }
    public void goToB() {
        moveInchesCenter(24);
        moveInchesForward(99, true);
        releaseWobble();
        moveInchesForward(90, false);
        turnToDesiredAngle(180);
    }
    public void goToC() {
        moveInchesCenter(24);
        moveInchesForward(123, true);
        turnToDesiredAngle(180);
        releaseWobble();
        moveInchesForward(115, true);
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

        moveInchesForward(6, true);

        moveInchesCenter(-12);

        ElapsedTime timer = new ElapsedTime();
        while(timer.seconds() < 1){}

        starterStackResult = starterStackSensor();

        moveInchesCenter(12);

        moveInchesForward(54, true);

        ringShot(3);

        if (starterStackResult == 0) {
            goToA();
            moveInchesForward(48, true);
            turnToDesiredAngle(-90);
            grabWobble();
            turnToDesiredAngle(0);
            moveInchesForward(40, true);
            moveInchesCenter(-30);
            goToA();
        } else if (starterStackResult == 1) {
            goToB();
            moveInchesCenter(12);
            turnToDesiredAngle(0);
            robot.intakeMotor.setPower(0.5);
            moveInchesForward(-36, false);
            robot.intakeMotor.setPower(0);
            moveInchesCenter(12);
            moveInchesForward(-12, false);
            turnToDesiredAngle(-90);
            grabWobble();
            turnToDesiredAngle(0);
            moveInchesForward(40, true);
            moveInchesCenter(-30);
            ringShot(1);
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
