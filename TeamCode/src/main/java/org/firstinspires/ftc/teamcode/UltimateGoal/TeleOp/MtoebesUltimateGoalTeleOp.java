package org.firstinspires.ftc.teamcode.UltimateGoal.TeleOp;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.MtoebesUltimateGoalHardware;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Mtoebes UltimateGoal", group = "2021_UltimateGoal")
public class MtoebesUltimateGoalTeleOp extends OpMode {

    public static final int ABSOLUTE_MODE = 0;
    public static final int RELATIVE_MODE = 1;

    public static double MOVE_POWER_INC = .05;
    public static double TURN_POWER_INC = .05;

    public double movePowerRatio = .1;
    public double turnPowerRatio = .1;

    public MtoebesUltimateGoalHardware robot = new MtoebesUltimateGoalHardware();

    public ImprovedGamepad impGamepad1;
    public ImprovedGamepad impGamepad2;
    public ElapsedTime timer = new ElapsedTime();

    public int currentMode = RELATIVE_MODE;
    public boolean pausePower = false;

    @Override
    public void init() {
        impGamepad1 = new ImprovedGamepad(this.gamepad1, this.timer, "GP1");
        impGamepad2 = new ImprovedGamepad(this.gamepad2, this.timer, "GP2");

        robot.init(this.hardwareMap);

        if (robot.missingHardwareNames.size() > 0) {
            telemetry.addData("WARNING THERE DEVICES MISSING FROM THE CONFIG", robot.missingHardwareNames.size());
            for (String deviceName : robot.missingHardwareNames) {
                telemetry.addData("MISSING", deviceName);
            }
        }

        telemetry.update();
    }

    @Override
    public void loop() {
        impGamepad1.update();
        impGamepad2.update();

        float rightStickX = gamepad1.right_stick_x;
        float rightStickY = -1 * gamepad1.right_stick_y;
        double rightStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(rightStickY, rightStickX));
        double rightStickRadius = Math.hypot(rightStickX, rightStickY);

        float leftStickX = gamepad1.left_stick_x;
        float leftStickY = -1 * gamepad1.left_stick_y;
        double leftStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(leftStickY, leftStickX));
        double leftStickRadius = Math.hypot(leftStickX, leftStickY);

        float robotAngle = robot.getAngle();

        double turnPower = 0;
        Double absTurnAngle = null;
        Double relTurnAngle = null;

        double xMovePower = 0;
        double yMovePower = 0;
        Double absMoveAngle = null;
        Double relMoveAngle = null;

        if (gamepad1.dpad_left) {
            currentMode = ABSOLUTE_MODE;
        } else if (gamepad1.dpad_right) {
            currentMode = RELATIVE_MODE;
        }

        if (gamepad1.dpad_up) {
            pausePower = false;
        } else if (gamepad1.dpad_down) {
            pausePower = true;
        }

        if (gamepad1.start) {
            if (impGamepad1.right_trigger.isInitialPress()) {
                turnPowerRatio = Math.min(turnPowerRatio + TURN_POWER_INC, 1);
            } else if (impGamepad1.left_trigger.isInitialPress()) {
                turnPowerRatio = Math.max(turnPowerRatio - TURN_POWER_INC, 0);
            }
        } else if (gamepad1.back) {
            if (impGamepad1.right_trigger.isInitialPress()) {
                movePowerRatio = Math.min(movePowerRatio + MOVE_POWER_INC, 1);
            } else if (impGamepad1.left_trigger.isInitialPress()) {
                movePowerRatio = Math.max(movePowerRatio - MOVE_POWER_INC, 0);
            }
        }

        if (gamepad1.left_bumper) {
            turnPower = turnPowerRatio;
        } else if (gamepad1.right_bumper) {
            turnPower = -turnPowerRatio;
        } else {

            if (gamepad1.y) {
                absTurnAngle = 90.0;
            } else if (gamepad1.x) {
                absTurnAngle = 180.0;
            } else if (gamepad1.a) {
                absTurnAngle = -90.0;
            } else if (gamepad1.b) {
                absTurnAngle = 0.0;
            } else if (rightStickRadius > 0.8) {
                absTurnAngle = rightStickAngle;
            }

            if (absTurnAngle != null) {
                relTurnAngle = AngleUnit.normalizeDegrees(absTurnAngle - robotAngle);
                turnPower = turnPowerRatio * robot.getMotorTurnSpeed(absTurnAngle, robotAngle);
            }
        }

        if (leftStickRadius > 0.25) {
            if (currentMode == ABSOLUTE_MODE) {
                absMoveAngle = leftStickAngle;
            } else {
                absMoveAngle = robotAngle + leftStickAngle - 90;
            }

            relMoveAngle = absMoveAngle - robotAngle;
            double xToMoveTo = Math.cos(Math.toRadians(relMoveAngle));
            double yToMoveTo = Math.sin(Math.toRadians(relMoveAngle));

            xMovePower = movePowerRatio * leftStickRadius * xToMoveTo;
            yMovePower = movePowerRatio * leftStickRadius * yToMoveTo;
        }

        double leftMotorPower = xMovePower - turnPower;
        double rightMotorPower = xMovePower + turnPower;
        double middleMotorPower = yMovePower;

        // Find the max power
        double maxPower = Math.max(Math.max(Math.abs(leftMotorPower), Math.abs(rightMotorPower)), Math.abs(middleMotorPower));

        if (maxPower > 1) {
            double powerRatio = 1 / maxPower;
            leftMotorPower = leftMotorPower * powerRatio;
            rightMotorPower = rightMotorPower * powerRatio;
            middleMotorPower = middleMotorPower * powerRatio;
        }

        robot.leftMotor.setPower(pausePower ? 0 : leftMotorPower);
        robot.rightMotor.setPower(pausePower ? 0 : rightMotorPower);
        robot.middleMotor.setPower(pausePower ? 0 : middleMotorPower);

        telemetry.addData("Mode  ", currentMode == 1 ? "Relative" : "Absolute");
        telemetry.addData("Paused", pausePower);

        telemetry.addData("Right stick angle", "%.0f", rightStickAngle);
        telemetry.addData("Left stick angle ", "%.0f", leftStickAngle);

        telemetry.addData("Robot angle", "%.0f", robotAngle);

        telemetry.addData("~~~ TURNING ~~~", "");
        telemetry.addData("Abs turn angle  ", absTurnAngle != null ? String.format("%.0f", absTurnAngle) : null);
        telemetry.addData("Rel turn angle  ", relTurnAngle != null ? String.format("%.0f", relTurnAngle) : null);
        telemetry.addData("Turn power ratio", "%.2f", turnPowerRatio);
        telemetry.addData("Turn power      ", "%.2f", turnPower);

        telemetry.addData("~~~ MOVING ~~~", "");
        telemetry.addData("Abs move angle  ", absMoveAngle != null ? String.format("%.0f", absMoveAngle) : null);
        telemetry.addData("Rel move angle  ", relMoveAngle != null ? String.format("%.0f", relMoveAngle) : null);
        telemetry.addData("Move power ratio", "%.2f", movePowerRatio);
        telemetry.addData("X move power    ", "%.2f", xMovePower);
        telemetry.addData("Y move power    ", "%.2f", yMovePower);

        telemetry.addData("~~~ POWER ~~~", "");
        telemetry.addData("Left power  ", "%.2f", leftMotorPower);
        telemetry.addData("Right power ", "%.2f", rightMotorPower);
        telemetry.addData("Middle power", "%.2f", middleMotorPower);

        telemetry.update();
    }
}
