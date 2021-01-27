package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.BaseUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.ScrimmageUltimateGoalHardware;

import java.util.List;

import static org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.ScrimmageUltimateGoalHardware.ELEMENT_SINGLE;
import static org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.ScrimmageUltimateGoalHardware.ELEMENT_QUAD;


public class BaseUltimateGoalAuto extends LinearOpMode {

    public final TeamColor teamColor;

    public BaseUltimateGoalHardware robot = BaseUltimateGoalHardware.create();
    public int starterStackResult = -1;
    public double forwardMotorPower = .75;

    public BaseUltimateGoalAuto(TeamColor teamColor) {
        super();
        this.teamColor = teamColor;
    }

    @Override
    public void runOpMode() throws InterruptedException {

    }
    public void init(boolean camera) {
        robot.init(this.hardwareMap);
        if(camera){
            initAndActivateWebCameraWithTensorFlow();
        }
    }
    public int starterStackSensor() {
        int stackID = 0;
        double confidence = 0.0;
        if (robot.webCamera.tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = robot.webCamera.tfod.getRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());

                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                    if(stackID != 2 && recognition.getLabel().equals(ELEMENT_SINGLE) && recognition.getConfidence() > 0.8) {
                        stackID = 1;
                        confidence = recognition.getConfidence();
                    } else if(recognition.getLabel().equals(ELEMENT_QUAD) && recognition.getConfidence() > 0.8) {
                        stackID = 2;
                        confidence = recognition.getConfidence();
                    }
                }
                telemetry.addData("stackID", stackID);
                telemetry.addData("confidence", confidence);
                telemetry.update();
            }
        }
        robot.webCamera.deactivateTfod();
        return stackID;
    }

    public void grabWobble() {
        robot.configureWobbleGrabber(true);
    }

    public void releaseWobble() {
        robot.configureWobbleGrabber(false);
        sleep(500);
    }

    public void turnToDesiredAngle(float desiredAngle){
        // Robot's current angle
        float robotAngle = robot.getAngle();

        // Determine the speed that the motors should be set to.
        double speed = robot.getMotorTurnSpeed(desiredAngle, robotAngle);

        // The robot should keep on turning until it reaches its desired angle.
        while(speed != 0 && opModeIsActive()){
            // Set the motors to their appropriate powers.
            robot.leftMotor.setPower(-speed);
            robot.rightMotor.setPower(speed);

            // Print out what the speed is.
            telemetry.addData("Speed",speed);

            // This prints out what the angle difference is.
            telemetry.addData("Angle difference", AngleUnit.normalizeDegrees(desiredAngle - robotAngle));

            // This updates our telemetry to continuously print out our telemetry.
            telemetry.update();

            // Update robot angle.
            robotAngle = robot.getAngle();

            // Update speed variable.
            speed = robot.getMotorTurnSpeed(desiredAngle, robotAngle);
        }

        // We don't want the robot to turn anymore; therefore, we set the motors' powers to 0.
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
    }

    public void moveInchesCenter(double inches){
        int direction = (TeamColor.RED_TEAM == this.teamColor) ? 1:-1;

        int ticks = (int) (inches * robot.centerTicksPerInch * direction);

        robot.middleMotor.setTargetPosition(robot.middleMotor.getCurrentPosition() + ticks);

        robot.middleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.middleMotor.setPower(.75);

        while (opModeIsActive() && (robot.middleMotor.isBusy())){
            telemetry.addData("stackID", starterStackResult);
            telemetry.addData("Current Middle Position", robot.middleMotor.getCurrentPosition());
            telemetry.update();
        }
        robot.middleMotor.setPower(0);

        robot.middleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public void moveInchesForward(double inches, boolean correctingRun) {
        int ticks = (int) (inches * robot.forwardTicksPerInch);
        double startAngle = robot.getAngle();
        double toleranceRange = 10.0;
        double angleTuning = 0;

        robot.leftMotor.setTargetPosition(robot.leftMotor.getCurrentPosition() + ticks);
        robot.rightMotor.setTargetPosition(robot.rightMotor.getCurrentPosition() + ticks);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftMotor.setPower(angleTuning + forwardMotorPower);
        robot.rightMotor.setPower(-angleTuning + forwardMotorPower);

        while (opModeIsActive() && (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {
            if(correctingRun) {
                angleTuning = pidTune(startAngle, robot.getAngle());
            }

            telemetry.addData("Adjusting:", angleTuning);
            telemetry.addData("stackID", starterStackResult);
            telemetry.addData("Current Left Position", robot.leftMotor.getCurrentPosition());
            telemetry.addData("Current Right Position", robot.rightMotor.getCurrentPosition());
            telemetry.update();

            robot.leftMotor.setPower(angleTuning + .75);
            robot.rightMotor.setPower(-angleTuning + .75);
        }

        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    //PID tuning to keep straight line driving on track
    public double pidTune(float startingAngle, float currentAngle){
        double correction = pidTuneOverflow((double)startingAngle, (double)currentAngle);
        return correction;
    }

    public double pidTune(double startingAngle, float currentAngle){
        double correction = pidTuneOverflow((double)startingAngle, (double)currentAngle);
        return correction;
    }

    public double pidTune(float startingAngle, double currentAngle){
        double correction = pidTuneOverflow((double)startingAngle, (double)currentAngle);
        return correction;
    }

    public double pidTune(double startingAngle, double currentAngle){
        double correction = pidTuneOverflow((double)startingAngle, (double)currentAngle);
        return correction;
    }

    //a single PID overflow so that we only have to change the value in one place
    public double pidTuneOverflow(double startingAngle, double currentAngle){
        double correction = (currentAngle - startingAngle) / 100;
        return correction;
    }

    public void initAndActivateWebCameraWithTensorFlow() {

        // Init the web camera with TensorFlow
        robot.initWebCamera(hardwareMap);
        robot.initTfod();

        // Activate TensorFlow
        robot.webCamera.activateTfod();

        // Check for errors
        if (robot.webCamera.hasError()) {
            telemetry.addData("Failed!", robot.webCamera.errorMessage);
        } else {
            telemetry.addData("Successful!", "");
        }
    }
    public enum TeamColor {
     RED_TEAM, BLUE_TEAM
    }

    //will this work
    public void ringShot(int num){
        robot.kicker.setPosition(.25);
        //wait(500);
        robot.kicker.setPosition(.75);
        telemetry.addData("Shooting Rings: ", num);
        //TODO Figure out how transfer/shooting works and implement code
    }
}
