package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Hardware.ProgrammingUltimateGoalHardware;

@TeleOp(name = "ProgrammingUltimateGoalTeleOp")
public class ProgrammingUltimateGoalTeleOp extends OpMode {
    public ProgrammingUltimateGoalHardware robot = new ProgrammingUltimateGoalHardware();

    @Override
    public void init() {
        robot.init(this.hardwareMap);
    }

    @Override
    public void loop() {
        float rightStickX = gamepad1.right_stick_x;
        float rightStickY = -1 * gamepad1.right_stick_y;
        float leftStickX = gamepad1.left_stick_x;
        float leftStickY = -1 * gamepad1.left_stick_y;
        double rightStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(rightStickY, rightStickX));
        double leftMotorPower = 0;
        double rightMotorPower = 0;
        float robotAngle = robot.getAngle();

        // Determine radius of right stick through Pythagorean Theorem.
        double rightStickXSquared = Math.pow(rightStickX,2);
        double rightStickYSquared = Math.pow(rightStickY,2);
        double rightStickRadiusSquared = rightStickXSquared + rightStickYSquared;
        double rightStickRadius = Math.sqrt(rightStickRadiusSquared);

        if(gamepad1.left_bumper){
            // When pressing the left bumper, the robot will turn counterclockwise.
            leftMotorPower = -1;
            rightMotorPower = 1;
        } else if(gamepad1.right_bumper){
            // When pressing the right bumper, the robot will turn clockwise.
            leftMotorPower = 1;
            rightMotorPower = -1;
        } else {
            // This sets the motor's power to however far the left joystick is pushed.
            leftMotorPower = leftStickY;
            rightMotorPower = leftStickY;
        }

        // We are setting a circle of radius 0.8 to be our dead zone.
        if(rightStickRadius > 0.8 || gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y) {
            if(gamepad1.y){
                rightStickAngle = angleToTurnTo(1);
            }
            if(gamepad1.x){
                rightStickAngle = angleToTurnTo(2);
            }
            if(gamepad1.a){
                rightStickAngle = angleToTurnTo(3);
            }
            if(gamepad1.b){
                rightStickAngle = angleToTurnTo(4);
            }

            // Calculate the angle difference between our desired angle and the actual angle of
            // the robot.
            double angleDifference = AngleUnit.normalizeDegrees(rightStickAngle - robotAngle);

            // This prints out what the angle difference is.
            telemetry.addData("Angle difference", angleDifference);

            double speed = getMotorSpeed(rightStickAngle, robotAngle);

            leftMotorPower = -speed;
            rightMotorPower = speed;
            telemetry.addData("Speed",speed);
        }

        // Program sets left, middle, and right motors to their respective powers.
        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);
        robot.middleMotor.setPower(leftStickX);

        /*
        * This code below prints out the robot angle, right stick angle, right motor power, the
        * left motor power, and performs a telemetry update.
         */
        telemetry.addData("Robot angle", robotAngle);
        telemetry.addData("Right Stick Angle", rightStickAngle);
        telemetry.addData("Right Motor Power", rightMotorPower);
        telemetry.addData("Left Motor Power", leftMotorPower);
        telemetry.update();
    }

    public double getMotorSpeed(double rightStickAngle, double robotAngle){
        // Calculate the angle difference between our desired angle and the actual angle of
        // the robot.
        double angleDifference = AngleUnit.normalizeDegrees(rightStickAngle - robotAngle);

        // This prints out what the angle difference is.
        telemetry.addData("Angle difference", angleDifference);

        double speed;

        // If the angle difference is greater than 10 the robot will turn counterclockwise.
        // Otherwise if the angle difference is less than -10 the robot will turn clockwise.
        if (Math.abs(angleDifference) > 10) {
            //Equation for determining target speed: Speed = angle / 45.
            speed = angleDifference/45;
        }
        else {
            speed = 0;
        }

        return speed;
    }

    public int angleToTurnTo(int buttonDeterminer){
        int angle = 0;

        if(buttonDeterminer == 1){
            angle = 90;
        }
        else if(buttonDeterminer == 2){
            angle = 180;
        }
        else if(buttonDeterminer == 3){
            angle = 270;
        }
        // Subject to deleting.
        else if(buttonDeterminer == 4){
            angle = 360;
        }

        return angle;
    }
}
