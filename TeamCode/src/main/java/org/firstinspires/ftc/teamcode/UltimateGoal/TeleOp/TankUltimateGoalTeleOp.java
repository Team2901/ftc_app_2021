package org.firstinspires.ftc.teamcode.UltimateGoal.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.BaseUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.ProgrammingUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.TankUltimateGoalHardware;

@TeleOp(name = "TankUltimateGoalTeleOp")
public class TankUltimateGoalTeleOp extends OpMode{
    public TankUltimateGoalHardware robot = new TankUltimateGoalHardware();

    @Override
    public void init() {
        robot.init(this.hardwareMap);
    }

    @Override
    public void loop() {
        // Declare variables that will be used later in this method.
        float rightStickX = gamepad1.right_stick_x;
        float rightStickY = -1 * gamepad1.right_stick_y;
        float leftStickX = gamepad1.left_stick_x;
        float leftStickY = -1 * gamepad1.left_stick_y;
        double rightStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(rightStickY, rightStickX));
        double leftMotorPower = 0;
        double rightMotorPower = 0;
        float robotAngle = robot.getAngle();
        double desiredAngle = 0;
        boolean isLetterOnGamepad1Pressed = gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y;

        // Determine radius of right stick through Pythagorean Theorem.
        double rightStickXSquared = Math.pow(rightStickX,2);
        double rightStickYSquared = Math.pow(rightStickY,2);
        double rightStickRadiusSquared = rightStickXSquared + rightStickYSquared;
        double rightStickRadius = Math.sqrt(rightStickRadiusSquared);

        // When pressing the left bumper, the robot will turn counterclockwise.
        if(gamepad1.left_bumper){
            leftMotorPower = -1;
            rightMotorPower = 1;
        }
        // When pressing the right bumper, the robot will turn clockwise.
        else if(gamepad1.right_bumper){
            leftMotorPower = 1;
            rightMotorPower = -1;
        }
        // This sets the motor's power to however far the left joystick is pushed.
        else {
            leftMotorPower = leftStickY;
            rightMotorPower = leftStickY;
        }

        /*
         * If the radius of the right stick is greater than the radius of our circular dead zone (0.8)
         * or if one of the letter buttons on the controller is pressed, the robot will turn to the
         * angle that we want it to be turned to.
         */
        if(rightStickRadius > 0.8 || isLetterOnGamepad1Pressed) {
            // If y is pressed, the robot will turn by 90 degrees.
            if(gamepad1.y){
                desiredAngle = angleToTurnTo(1);
            }
            // If x is pressed, the robot will turn by 180 degrees.
            else if(gamepad1.x){
                desiredAngle = angleToTurnTo(2);
            }
            // If a is pressed, the robot will turn by 270 degrees.
            else if(gamepad1.a){
                desiredAngle = angleToTurnTo(3);
            }
            // If b is pressed, the robot will turn by 360 degrees.
            else if(gamepad1.b){
                desiredAngle = angleToTurnTo(4);
            }
            // Otherwise, the robot will turn to the angle that we point the right stick down in.
            else{
                desiredAngle = rightStickAngle;
            }

            // This prints out what the angle difference is.
            telemetry.addData("Angle difference", AngleUnit.normalizeDegrees(desiredAngle - robotAngle));

            // Determine the speed that the motors should be set to.
            double speed = robot.getMotorTurnSpeed(desiredAngle, robotAngle);

            // Set the motors to their appropriate powers.
            leftMotorPower = -speed;
            rightMotorPower = speed;

            // Print out what the speed is.
            telemetry.addData("Speed",speed);
        }

        // Program sets left and right motors to their respective powers.
        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);

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

    /**
     * This method has the robot turn to a certain angle based on the letter button that is pressed.
     * @param buttonDeterminer the number used to determine how many degrees the robot should turn by
     * @return
     */
    public int angleToTurnTo(int buttonDeterminer){
        // Declare angle variable.
        int angle = 0;

        // If y is pressed, the robot will turn by 90 degrees.
        if(buttonDeterminer == 1){
            angle = 90;
        }
        // If x is pressed, the robot will turn by 180 degrees.
        else if(buttonDeterminer == 2){
            angle = 180;
        }
        // If a is pressed, the robot will turn by 270 degrees.
        else if(buttonDeterminer == 3){
            angle = 270;
        }
        // If b is pressed, the robot will turn by 360 degrees.
        else if(buttonDeterminer == 4){
            angle = 360;
        }

        // Return angle variable.
        return angle;
    }
}
