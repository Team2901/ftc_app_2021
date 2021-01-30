package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.TeleOp;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.BaseUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Vuforia UltimateGoal Test", group = "2021_UltimateGoal")
public class VuforiaUltimateGoalTest extends OpMode {
    public BaseUltimateGoalHardware robot = BaseUltimateGoalHardware.create();

    public static final float MM_TO_INCHES = 0.0393701f;
    public static final double MAX_TURN_POWER = 0.5;

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

        if(errorMessage != null) {
            robot.webCamera.errorMessage = null;
            cameraRotationX = 90;
            robot.initPhoneCamera(this.hardwareMap);
        }

        // Loading the Vuforia trackables.
        robot.webCamera.loadVuforiaTrackables("UltimateGoal");

        // Sets up the position of the Vuforia web image and web camera.
        OpenGLMatrix webcamLocation = OpenGLMatrix.translation(-6/MM_TO_INCHES, -0.5f/MM_TO_INCHES, 0).multiplied(
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

        OpenGLMatrix robotLocation = null;

        // Check if trackers are visible.
        for(int i = 0; i < robot.webCamera.vuforiaTrackables.size(); i++) {
            // Store current element in variable.
            VuforiaTrackable currentTrackable = robot.webCamera.vuforiaTrackables.get(i);

            // Gets listener before checking if current tracker is visible.
            VuforiaTrackableDefaultListener currentTrackableDefaultListener = (VuforiaTrackableDefaultListener) currentTrackable.getListener();

            // Determines whether current trackable is visible.
            boolean isTrackableVisible = currentTrackableDefaultListener.isVisible();

            // Prints out whether the current trackable is visible.
            telemetry.addData(currentTrackable.getName() + " is visible", isTrackableVisible);

            if(isTrackableVisible){
                // Gets the robot's location.
                robotLocation = currentTrackableDefaultListener.getRobotLocation();
            }
        }

        double leftMotorPower = 0;
        double rightMotorPower = 0;

        double targetFieldAngle;


        boolean isVisible = robotLocation != null;

        float x = 0;
        float y = 0;
        float zAngle = 0;
        double angleDifference = 0;
        double velocity = 0;

        /*
         * If the robot location is not null, we translate the robot's location. In other words,
         * if the tracker image is visible, we translate the robot's location.
         */
        if(isVisible){

            // This gets what this trackable thinks that the robot's position is.
            VectorF robotLocationTranslation = robotLocation.getTranslation();
            x = robotLocationTranslation.get(0);
            y = robotLocationTranslation.get(1);
            float z = robotLocationTranslation.get(2);

            // This gets what this trackable thinks that the robot's orientation is.
            Orientation orientation = Orientation.getOrientation(robotLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
            float xAngle = orientation.firstAngle;
            float yAngle = orientation.secondAngle;
            zAngle = orientation.thirdAngle;

            // x, y, z positions (x, y, z) (in inches)
            telemetry.addData("x, y, z positions", String.format("(%.1f, %.1f, %.1f)", x * MM_TO_INCHES, y * MM_TO_INCHES, z * MM_TO_INCHES));

            // x, y, z rotations (x, y, z)
            telemetry.addData("x, y, z rotations", String.format("(%.1f, %.1f, %.1f)", xAngle, yAngle, zAngle));

            // Calculate the angle relative to the field and print it out.
            targetFieldAngle = Math.toDegrees(Math.atan(y/x));
            // Print angle relative to the robot and the angle relative to the field.
            telemetry.addData("Angle relative to the field", "%.1f", targetFieldAngle);

            // Determine the angle difference between targetFieldAngle and the robot's angle.
            angleDifference = AngleUnit.normalizeDegrees(targetFieldAngle - zAngle);
            telemetry.addData("Angle difference", "%.1f", angleDifference);

            // Determine the speed that the motors should be set to.
            velocity = robot.getMotorTurnSpeed(targetFieldAngle, zAngle);
            telemetry.addData("Velocity", "%.1f", velocity);

            // Only make the robot turn relative to the field if a on the first gamepad is pressed.
            if(gamepad1.a){
                // Make the robot turn counterclockwise.
                leftMotorPower = -velocity * MAX_TURN_POWER;
                rightMotorPower = velocity * MAX_TURN_POWER;
            }
        }
        else
        {
            // We want to face the tower goal, so set the angle to 0.
            targetFieldAngle = 0;
        }

        // When pressing the left bumper, the robot will turn counterclockwise.
        if(gamepad1.left_bumper)
        {
            leftMotorPower = -MAX_TURN_POWER;
            rightMotorPower = MAX_TURN_POWER;
        }
        // When pressing the right bumper, the robot will turn clockwise.
        else if(gamepad1.right_bumper)
        {
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
            String msg = String.format("%f, %f, %b, %f, %f, %f, %f", timestampTimer.milliseconds(), zAngle, isVisible, angleDifference, velocity, x,  y);

            logMessages.add(msg);

            timer.reset();
        }

        if (improvedGamepad.x.isInitialPress()) {

            int time = (int)(System.currentTimeMillis());

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
