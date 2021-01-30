package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.TeleOp;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.BaseUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;
import org.firstinspires.ftc.teamcode.Utility.MatrixHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;

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

        String errorMessage = robot.initWebCamera(this.hardwareMap);

        if (errorMessage != null) {
            robot.webCamera.errorMessage = null;
            robot.initPhoneCamera(this.hardwareMap);
        }

        // Load the Vuforia trackables.
        robot.webCamera.loadVuforiaTrackables("UltimateGoal");

        // Sets up the position of the web camera on the robot (6 inches in front of and .5 inches to the left of center)
        OpenGLMatrix webcamLocation = MatrixHelper.buildMatrixInches(-6.0f, -0.5f, 0, XZY, 90, 90, 0);

        // Set up the blue tower trackable (propped up in the middle of the field.
        VuforiaTrackable vuforiaBlueTower = robot.webCamera.vuforiaTrackables.get(0);
        vuforiaBlueTower.setName("Blue Tower");
        OpenGLMatrix blueTowerLocation = MatrixHelper.buildMatrixInches(0, 0, 0, XYZ, 90, 0, -90);
        vuforiaBlueTower.setLocation(blueTowerLocation);
        ((VuforiaTrackableDefaultListener) vuforiaBlueTower.getListener()).setCameraLocationOnRobot(robot.webCamera.parameters.cameraName, webcamLocation);

        // Set up the red tower trackable (propped up in the middle of the field.
        VuforiaTrackable vuforiaRedTower = robot.webCamera.vuforiaTrackables.get(1);
        vuforiaRedTower.setName("Red Tower");
        OpenGLMatrix redTowerLocation = MatrixHelper.buildMatrixInches(0, 0, 0, XYZ, 90, 0, -90);
        vuforiaRedTower.setLocation(redTowerLocation);
        ((VuforiaTrackableDefaultListener) vuforiaRedTower.getListener()).setCameraLocationOnRobot(robot.webCamera.parameters.cameraName, webcamLocation);

        // We are ready to use the trackables and this gives us the opportunity to deactivate them later.
        robot.webCamera.vuforiaTrackables.activate();
    }

    @Override
    public void loop() {

        improvedGamepad.update();

        OpenGLMatrix robotLocation = null;
        String trackableName = null;

        // Check if trackers are visible.
        for (VuforiaTrackable currentTrackable : robot.webCamera.vuforiaTrackables) {
            VuforiaTrackableDefaultListener currentTrackableDefaultListener = (VuforiaTrackableDefaultListener) currentTrackable.getListener();

            if (currentTrackableDefaultListener.isVisible()) {
                robotLocation = currentTrackableDefaultListener.getRobotLocation();
                trackableName = currentTrackable.getName();
            }
        }

        telemetry.addData("Visible Tracker", trackableName);

        double leftMotorPower = 0;
        double rightMotorPower = 0;

        boolean isVisible = robotLocation != null;

        double angleDifference = 0;
        double velocity = 0;

        Double targetFieldAngle;

        // Get what this tracker thinks the robot's position and orientation is
        Float x = MatrixHelper.getXPositionInches(robotLocation);
        Float y = MatrixHelper.getYPositionInches(robotLocation);
        Float zAngle = MatrixHelper.getZAngle(robotLocation);

        // x, y, z positions (x, y, z) (in inches)
        telemetry.addData("x, y, z positions", MatrixHelper.getInchesPositionString(robotLocation));

        // x, y, z rotations (x, y, z)
        telemetry.addData("x, y, z rotations", MatrixHelper.getAngleString(robotLocation));

        if (isVisible) {
            // Calculate the angle relative to the field
            targetFieldAngle = Math.toDegrees(Math.atan(y / x));
        } else {
            targetFieldAngle = null;
        }

        if (targetFieldAngle != null) {
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
