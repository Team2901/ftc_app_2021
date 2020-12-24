package org.firstinspires.ftc.teamcode.UltimateGoal.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.System.currentTimeMillis;

public class DecScrimUltimateAuto extends BaseUltimateGoalAuto {

    private ElapsedTime runtime = new ElapsedTime();

    public DecScrimUltimateAuto() {
        super(TeamColor.BLUE_TEAM);
    }

    public void goToA() {
        moveInchesCenter(24);
        moveInchesForward(9, true);
        //TODO add 90 degree turn
        releaseWobble();
    }
    public void goToB() {
        moveInchesCenter(24);
        moveInchesForward(99, true);
        releaseWobble();
    }
    public void goToC() {
        moveInchesCenter(24);
        moveInchesForward(123, true);
        //TODO add 90 degree turn
        releaseWobble();
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

        //grabWobble();

        /*
        if(target == "A") {
            goToA();
        } else if (target == "B") {
            goToB();
        } else if (target == "C") {
            goToC();
        } else {
            telemetry.addData("error", "Invalid Target");
            telemetry.update();
        }
         */

        moveInchesCenter(-12);

        long targetTime = currentTimeMillis() + 1000;

        while (currentTimeMillis() < targetTime && opModeIsActive()){

        }

        starterStackResult = starterStackSensor();

        moveInchesCenter(12);

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

        //Parks back at the line
        if(starterStackResult == 1) {
            moveInchesForward(-25, false);
        } else if(starterStackResult == 2){
            moveInchesForward(-50, false);
        }


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
        }
    }
}
