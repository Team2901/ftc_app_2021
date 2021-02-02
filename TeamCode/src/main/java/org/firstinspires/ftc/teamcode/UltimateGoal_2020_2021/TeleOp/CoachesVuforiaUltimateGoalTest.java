package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.TeleOp;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.BaseUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;
import org.firstinspires.ftc.teamcode.Utility.MatrixHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities.MM_TO_INCHES;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Coaches Vuforia UltimateGoal Test", group = "2021_UltimateGoal")
public class CoachesVuforiaUltimateGoalTest extends OpMode {
    public static final double MAX_TURN_POWER = 0.5;
    public BaseUltimateGoalHardware robot = BaseUltimateGoalHardware.create();
    List<String> logMessages = new ArrayList<>();
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime timestampTimer = new ElapsedTime();


    ImprovedGamepad improvedGamepad;

    @Override
    public void init() {
        robot.init(this.hardwareMap);

        improvedGamepad = new ImprovedGamepad(gamepad1, timestampTimer, "g1");

        int cameraRotationX = 0;
        String errorMessage = robot.initWebCamera(this.hardwareMap);

        if (errorMessage != null) {
            robot.webCamera.errorMessage = null;
            cameraRotationX = 90;
            robot.initPhoneCamera(this.hardwareMap);
        }

        // Loading the Vuforia trackables.
        robot.webCamera.loadVuforiaTrackables("UltimateGoal");

        // Sets up the position of the Vuforia web image and web camera.
        OpenGLMatrix webcamLocation = OpenGLMatrix.translation(-6 / MM_TO_INCHES, -0.5f / MM_TO_INCHES, 0).multiplied(
                Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZY, AngleUnit.DEGREES,
                        90, 90, 0));

        // Sets up vuforia trackables.
        robot.webCamera.ultimateGoalSetupTrackables(webcamLocation);

        // We are ready to use the trackables and this gives us the opportunity to deactivate them later.
        robot.webCamera.vuforiaTrackables.activate();
    }

    @Override
    public void loop() {

        improvedGamepad.update();

        OpenGLMatrix robotLocation = robot.webCamera.getRobotLocation();

        double leftMotorPower = 0;
        double rightMotorPower = 0;

        boolean isVisible = robotLocation != null;
        telemetry.addData("Is visible", isVisible);

        telemetry.addData("robot position", MatrixHelper.getInchesPositionString(robotLocation));
        telemetry.addData("robot angle", MatrixHelper.getAngleString(robotLocation));

        double angleDifference = 0;
        double velocity = 0;

        // Get what this tracker thinks the robot's position and orientation is
        Float x = MatrixHelper.getXPositionInches(robotLocation);
        Float y = MatrixHelper.getYPositionInches(robotLocation);
        Float zAngle = MatrixHelper.getZAngle(robotLocation);

        Double targetFieldAngle = robot.webCamera.getRobotTurnAngle(robotLocation, false);

        if (targetFieldAngle != null) {

            if (zAngle == null) {
                zAngle = robot.getAngle();
            }

            // Print angle relative to the robot and the angle relative to the field.
            telemetry.addData("Angle relative to the field", "%.1f", targetFieldAngle);

            // Determine the angle difference between targetFieldAngle and the robot's angle.
            angleDifference = AngleUnit.normalizeDegrees(targetFieldAngle - zAngle);
            telemetry.addData("Angle difference", "%.1f", angleDifference);

            // Determine the speed that the motors should be set to.
            velocity = robot.getMotorTurnSpeed(targetFieldAngle, zAngle);
            telemetry.addData("Velocity", "%.1f", velocity);

            // Only make the robot turn relative to the field if a on the first gamepad is pressed.
            if (gamepad1.a) {
                // Make the robot turn counterclockwise.
                leftMotorPower = -velocity * MAX_TURN_POWER;
                rightMotorPower = velocity * MAX_TURN_POWER;
            }
        }

        // When pressing the left bumper, the robot will turn counterclockwise.
        if (gamepad1.left_bumper) {
            leftMotorPower = -MAX_TURN_POWER;
            rightMotorPower = MAX_TURN_POWER;
        }
        // When pressing the right bumper, the robot will turn clockwise.
        else if (gamepad1.right_bumper) {
            leftMotorPower = MAX_TURN_POWER;
            rightMotorPower = -MAX_TURN_POWER;
        }

        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);

        // Find where the trackers are.

        if (timer.milliseconds() >= 1) {
             /*
             Log information every millisecond
             1) timestamp
             2) robot angle
             3) visible?
             4) angleDiff
             5) velocity
             6) x diff
             7) y diff
             */
            String msg = String.format("%f, %f, %b, %f, %f, %f, %f", timestampTimer.milliseconds(), zAngle, isVisible, angleDifference, velocity, x, y);
            logMessages.add(msg);
            timer.reset();
        }

        if (improvedGamepad.x.isInitialPress()) {
            int time = (int) (System.currentTimeMillis());
            try {
                FileUtilities.writeConfigFile("vuforiaLogFile_" + time + "_.csv", logMessages);
                logMessages.clear();
            } catch (IOException e) {
                telemetry.addData("Error writing to file", e.getMessage());
            }
        }

        telemetry.update();

    }
}
