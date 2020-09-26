package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.Hardware.BuilderSkystoneHardware;
import org.firstinspires.ftc.teamcode.Utility.AngleUtilities;

@Disabled
@TeleOp(name = "Builder Skystone - One Controller", group = "SKYSTONE")
public class OneControllerSkystoneTeleOp extends OpMode {


    public static final double WHEEL_POWER_RATIO = .35;

    public final static double WHEEL_SERVO_GEAR_RATIO = .3;
    public final static double WIDTH_OF_ROBOT = 13.5;
    public final static double LENGTH_OF_ROBOT = 13.5;
    //This is the angle Phi that we defined in the math done before this
    public final static double TURN_ANGLE = Math.atan(WIDTH_OF_ROBOT / LENGTH_OF_ROBOT);
    public final static int SERVO_MAX_ANGLE = 2727;
    public final static int SERVO_MIN_ANGLE = 0;
    public final static double FRONT_LEFT_OFFSET = .11;
    public final static double FRONT_RIGHT_OFFSET = .13;
    public final static double BACK_LEFT_OFFSET = .1;
    public final static double BACK_RIGHT_OFFSET = .11;
    public double maxCrane = .68;

    double power = 0;
    int step = 0;
    int topStep = 0;

    public ElapsedTime timer = new ElapsedTime();
    public ImprovedGamepad improvedGamepad1;
    public ImprovedGamepad improvedGamepad2;

    public final static double WHEEL_MAX_ANGLE = SERVO_MAX_ANGLE * WHEEL_SERVO_GEAR_RATIO;

    BuilderSkystoneHardware robot = new BuilderSkystoneHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);
        //The y position is -1 to correct the joystick directions
      //  robot.swerveStraight(0, 0);

        this.improvedGamepad1 = new ImprovedGamepad(gamepad1, timer, "g1");
        this.improvedGamepad2 = new ImprovedGamepad(gamepad2, timer, "g2");
    }

    @Override
    public void loop() {

        improvedGamepad1.update();
        improvedGamepad2.update();

        double joystickPositionX = gamepad1.left_stick_x;
        double joystickPositionY = -gamepad1.left_stick_y;

        double radius = Math.sqrt(Math.pow(joystickPositionX, 2) + Math.pow(joystickPositionY, 2));

        telemetry.addData("X", joystickPositionX);
        telemetry.addData("Y", joystickPositionY);

        // WHHEL CONTROL
        if (improvedGamepad1.right_stick_x.isPressed()) {
            double power = getWheelPower(improvedGamepad1.right_stick.x.getValue(), gamepad1.left_bumper);
            robot.swerveTurn(power);
        } else if (improvedGamepad1.left_stick.isPressed()) {
            double power = getWheelPower(improvedGamepad1.left_stick.getValue(), gamepad1.left_bumper);
            double joyWheelAngle = improvedGamepad1.left_stick.getAngel();
            robot.swerveStraight(joyWheelAngle, power);
        } else {
            robot.setWheelMotorPower(0,0,0,0);
        }

        //makes lift go up by levels
        if (gamepad1.left_bumper) {
            liftStepper();
            // WRIST > .25 DON'T MOVE LIFT
            // CLAW >.25 DON'T MOVE LIFT
        }

        if(gamepad1.left_bumper) {
            //GOT THIS OVERLOADED DONT FORGET
            if (gamepad1.right_bumper) {
                if(robot.crane.getPosition() + .001< maxCrane)
                {
                    robot.crane.setPosition(robot.crane.getPosition() + .001);
                }

            } else if (gamepad1.left_bumper) {

                    robot.crane.setPosition(robot.crane.getPosition() - .001);
            }

            if (gamepad1.right_trigger >.3) {
                robot.wrist.setPosition(robot.wrist.getPosition() + .01);

            } else if (gamepad1.left_trigger>.3) {
                robot.wrist.setPosition(robot.wrist.getPosition() - .01);
            }
            telemetry.addData("wrist Position:", robot.wrist.getPosition());

            if (robot.isOkayToOpen() == true) {

                if (gamepad1.a) {
                    robot.jaw.setPosition(robot.jaw.getPosition() + .01);
                } else if (gamepad1.b) {
                    robot.jaw.setPosition(robot.jaw.getPosition() - .01);
                }
            }
        }
        telemetry.addData("jaw Position: ", robot.jaw.getPosition());

        telemetry.addData("frontLeft", String.format("tarAngle: %.2f   mod:%d",
                robot.frontLeftSwerveWheel.targetAngle, robot.frontLeftSwerveWheel.modifier));
        telemetry.addData("frontRight", String.format("tarAngle: %.2f   mod:%d",
                robot.frontRightSwerveWheel.targetAngle, robot.frontRightSwerveWheel.modifier));
        telemetry.addData("backLeft", String.format("tarAngle: %.2f   mod:%d",
                robot.backLeftSwerveWheel.targetAngle, robot.backLeftSwerveWheel.modifier));
        telemetry.addData("backRight", String.format("tarAngle: %.2f   mod:%d",
                robot.backRightSwerveWheel.targetAngle, robot.backRightSwerveWheel.modifier));

        telemetry.addData("right_stick_x", improvedGamepad1.right_stick.x.getValue());

        telemetry.update();
    }

    public double getWheelPower(double radius, boolean pause) {
        if (pause) {
            return 0;
        } else {
            return radius * WHEEL_POWER_RATIO;
        }
    }

    /*This method finds our desired angle based on the joysticks. We want out robot's wheels to
    follow the position of our joystick, so we find the angle of our joysticks position like it is
    a position on the coordinate plane
     */
    public double joystickPositionToWheelAngle(double joystickPositionX, double joystickPositionY) {
        double wheelAngleRad = Math.atan2(joystickPositionY, joystickPositionX);
        double wheelAngle = radiansDegreesTranslation(wheelAngleRad) - 90;
        double wheelAngleStandarized = AngleUtilities.getPositiveNormalizedAngle(wheelAngle);
        return wheelAngleStandarized;
    }

    public double radiansDegreesTranslation(double radians) {
        double degrees = radians * 180 / Math.PI;
        return degrees;

    }

    public int liftStepper() {
        // WRIST > .25 DON'T MOVE LIFT
        // CLAW >.25 DON'T MOVE LIFT
        if(robot.wrist.getPosition()> .25)
        {
            if(robot.jaw.getPosition()> .25)
            {
                telemetry.addLine("I could self damage.");
            }
        }
        else{
            if (this.improvedGamepad1.y.isInitialPress()) {
                step = step + 1;
                if (step > 5) {
                    step = 5;
                }
                power = .5;
                //up by one
            }
            if (this.improvedGamepad1.a.isInitialPress()) {
                step = step - 1;
                if (step < 0) {
                    step = 0;
                }
                power = .5;
                //down by one
            }

            if (this.improvedGamepad1.b.isInitialPress()) {
                topStep = step;
                step = 0;
                power = .7;
                //to bottom
            }

            if (this.improvedGamepad1.x.isInitialPress()) {
                step = topStep;
                power = .7;
                //to top
            }

        }

        telemetry.addData("Step", step);
        telemetry.addData("Top Step", topStep);
        telemetry.update();


        //DcMotor lift = this.hardwareMap.dcMotor.get("lift");
        int targetPosition = step * 750;

        robot.lift.setTargetPosition(targetPosition);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(power);

        telemetry.update();
        return step;

    }

}


