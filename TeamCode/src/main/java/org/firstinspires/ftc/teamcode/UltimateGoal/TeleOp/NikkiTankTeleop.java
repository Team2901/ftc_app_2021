package org.firstinspires.ftc.teamcode.UltimateGoal.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.UltimateGoal.Autonomous.BaseUltimateGoalAuto;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.BaseUltimateGoalHardware;

@TeleOp(name = "NikkiTank", group = "2021_UltimateGoal")
public class NikkiTankTeleop extends OpMode {
    public BaseUltimateGoalHardware robot = new BaseUltimateGoalHardware();
    public BaseUltimateGoalAuto auto = new BaseUltimateGoalAuto(BaseUltimateGoalAuto.TeamColor.RED_TEAM);
    //variables for use in PID loop
    //float angleAfterTurn = robot.getAngle();
    double angleCorrection = 0.0;
    int driveChoice = 0;

    @Override
    public void init() {
        robot.init(this.hardwareMap);
    }

    @Override
    public void loop() {

        telemetry.addData("Enter Mode", "A for Will's H-Drive, B for Tank Drive, and X for Nikki's H-Drive");

        if(gamepad1.a) {
            driveChoice = 1;
        }
        if(gamepad1.b) {
            driveChoice = 2;
        }

        if(gamepad1.x) {
            driveChoice = 3;
        }

        if(driveChoice == 1) {
            hLoop();
            telemetry.addData("Active Mode", "H-Drive");
        }
        if(driveChoice == 2) {
            tankLoop();
            telemetry.addData("Active Mode", "Tank Drive");
        }
        if(driveChoice == 3) {
            QOLHLoop();
            telemetry.addData("Active Mode", "QOL H-Drive");
        }

        telemetry.update();
    }

    public void hLoop() {
        float angleAfterTurn = robot.getAngle();

        // Declare variables that will be used later in this method.
        float rightStickX = gamepad1.right_stick_x;
        float rightStickY = -1 * gamepad1.right_stick_y;
        float leftStickX = gamepad1.left_stick_x;
        float leftStickY = -1 * gamepad1.left_stick_y;
        double rightStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(rightStickY, rightStickX));
        double leftMotorPower = 0;
        double rightMotorPower = 0;
        float robotAngle = robot.getAngle();
        boolean isLetterOnGamepad1Pressed = gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y;

        // Determine radius of right stick through Pythagorean Theorem.
        double rightStickXSquared = Math.pow(rightStickX,2);
        double rightStickYSquared = Math.pow(rightStickY,2);
        double rightStickRadiusSquared = rightStickXSquared + rightStickYSquared;
        double rightStickRadius = Math.sqrt(rightStickRadiusSquared);

        if(rightStickX > 0.1 || rightStickX < -.1){
            leftMotorPower = rightStickX;
            rightMotorPower = -rightStickX;
            angleAfterTurn = robotAngle;
        } else {
            //PID loop to keep going straight
            angleCorrection = auto.pidTune(angleAfterTurn, robotAngle);
            leftMotorPower = leftStickY + angleCorrection;
            rightMotorPower = leftStickY - angleCorrection;
            if(leftMotorPower > 1){
                leftMotorPower = 1;
            }
            if(rightMotorPower > 1){
                rightMotorPower = 1;
            }
        }

        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);
        robot.middleMotor.setPower(leftStickX);
    }

    public void tankLoop() {

        // Declare variables that will be used later in this method.
        float rightStickX = gamepad1.right_stick_x;
        float rightStickY = -1 * gamepad1.right_stick_y;
        float leftStickX = gamepad1.left_stick_x;
        float leftStickY = -1 * gamepad1.left_stick_y;
        double rightStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(rightStickY, rightStickX));
        double leftMotorPower = 0;
        double rightMotorPower = 0;
        float robotAngle = robot.getAngle();
        boolean isLetterOnGamepad1Pressed = gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y;

        // Determine radius of right stick through Pythagorean Theorem.
        double rightStickXSquared = Math.pow(rightStickX,2);
        double rightStickYSquared = Math.pow(rightStickY,2);
        double rightStickRadiusSquared = rightStickXSquared + rightStickYSquared;
        double rightStickRadius = Math.sqrt(rightStickRadiusSquared);

        leftMotorPower = leftStickY;
        rightMotorPower = -rightStickY;

        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);
    }
    public void QOLHLoop() {
        float angleAfterTurn = robot.getAngle();

        // Declare variables that will be used later in this method.
        float rightStickX = gamepad1.right_stick_x;
        float rightStickY = -1 * gamepad1.right_stick_y;
        float leftStickX = gamepad1.left_stick_x;
        float leftStickY = -1 * gamepad1.left_stick_y;
        double leftStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(leftStickY, leftStickX));
        double leftMotorPower = 0;
        double rightMotorPower = 0;
        float robotAngle = robot.getAngle();
        boolean isLetterOnGamepad1Pressed = gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y;

        // Determine radius of right stick through Pythagorean Theorem.
        double leftStickXSquared = Math.pow(leftStickX,2);
        double leftStickYSquared = Math.pow(rightStickY,2);
        double leftStickRadiusSquared = leftStickXSquared + leftStickYSquared;
        double leftStickRadius = Math.sqrt(leftStickRadiusSquared);

        if(leftStickY > 0.1 || leftStickY < -.1){
            leftMotorPower = leftStickY;
            rightMotorPower = -leftStickY;
            angleAfterTurn = robotAngle;
        } else {
            //PID loop to keep going straight
            angleCorrection = auto.pidTune(angleAfterTurn, robotAngle);
            leftMotorPower = rightStickY + angleCorrection;
            rightMotorPower = rightStickY - angleCorrection;
            if(leftMotorPower > 1){
                leftMotorPower = 1;
            }
            if(rightMotorPower > 1){
                rightMotorPower = 1;
            }
        }

        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);
        robot.middleMotor.setPower(rightStickY);
    }
}
