package org.firstinspires.ftc.teamcode.UltimateGoal.TeleOp;

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
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.GrantHardware;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.TankUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Vuforia UltimateGoal Test", group = "2021_UltimateGoal")
public class VuforiaUltimateGoalTest extends OpMode {
    public GrantHardware robot = new GrantHardware();

    public static final float MM_TO_INCHES = 0.0393701f;

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

        // Saves blue tower trackable.
        VuforiaTrackable vuforiaBlueTower = robot.webCamera.vuforiaTrackables.get(0);

        // This is used for telemetry purposes for identifying that the camera is seeing the blue tower trackable.
        vuforiaBlueTower.setName("Blue Tower");

        // This props up the blue tower, it is currently in the middle of the field.
        OpenGLMatrix blueTowerLocation = OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90,0,-90);
        vuforiaBlueTower.setLocation(blueTowerLocation);

        OpenGLMatrix blueTowerLocationTwo = OpenGLMatrix.translation(0, 0, 0).multiplied
                (Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES,
                        90,0, -90));
        vuforiaBlueTower.setLocation(blueTowerLocationTwo);

        // Saves red tower trackable.
        VuforiaTrackable vuforiaRedTower = robot.webCamera.vuforiaTrackables.get(1);

        // This is used for telemetry purposes for identifying that the camera is seeing the red tower trackable.
        vuforiaRedTower.setName("Red Tower");

        // This props up the red tower, it is currently in the middle of the field.
        OpenGLMatrix redTowerLocation = OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90,0,-90);
        vuforiaRedTower.setLocation(redTowerLocation);

        OpenGLMatrix redTowerLocationTwo = OpenGLMatrix.translation(0, 0, 0).multiplied(
                Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES,
                        90,0, -90));
        vuforiaRedTower.setLocation(redTowerLocationTwo);

        // Sets up the position of the Vuforia web image and web camera.

        OpenGLMatrix webcamLocation = OpenGLMatrix.translation(0, 0, 0).multiplied(
                Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZY, AngleUnit.DEGREES,
                        90, 90, 0));

        // We are telling the blue tower image where the camera is on the robot.
        VuforiaTrackable.Listener webcamListenerBlue = vuforiaBlueTower.getListener();
        VuforiaTrackableDefaultListener webcamDefaultListenerBlue = (VuforiaTrackableDefaultListener) webcamListenerBlue;
        webcamDefaultListenerBlue.setCameraLocationOnRobot(robot.webCamera.parameters.cameraName, webcamLocation);

        // We are telling the red tower image where the camera is on the robot.
        VuforiaTrackable.Listener webcamListenerRed = vuforiaRedTower.getListener();
        VuforiaTrackableDefaultListener webcamDefaultListenerRed = (VuforiaTrackableDefaultListener) webcamListenerRed;
        webcamDefaultListenerRed.setCameraLocationOnRobot(robot.webCamera.parameters.cameraName, webcamLocation);

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

        double relativeFieldAngle;

        // When pressing the left bumper, the robot will turn counterclockwise.
        if(gamepad1.left_bumper)
        {
            robot.leftMotor.setPower(-0.5);
            robot.rightMotor.setPower(0.5);
        }
        // When pressing the right bumper, the robot will turn clockwise.
        else if(gamepad1.right_bumper)
        {
            robot.leftMotor.setPower(0.5);
            robot.rightMotor.setPower(-0.5);
        }

        boolean isVisible = robotLocation != null;

        float x = 0;
        float y = 0;
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
            float zAngle = orientation.thirdAngle;

            // x, y, z positions (x, y, z)
            telemetry.addData("x, y, z positions", String.format("(%f, %f, %f)", x * MM_TO_INCHES, y * MM_TO_INCHES, z * MM_TO_INCHES));

            // x, y, z rotations (x, y, z)
            telemetry.addData("x, y, z rotations", String.format("(%f, %f, %f)", xAngle, yAngle, zAngle));

            // Calculate the angle relative to the robot and print it out.
            double relativeRobotAngle = Math.toDegrees(Math.atan(y/x));
            telemetry.addData("Angle relative to the robot", relativeRobotAngle);

            // Calculate angle relative to the field.
            relativeFieldAngle = relativeRobotAngle + robot.getAngle();

            // Print angle relative to the robot and the angle relative to the field.
            telemetry.addData("Angle relative to the field", relativeFieldAngle);

            // Determine the angle difference between relativeFieldAngle and the robot's angle.
            angleDifference = AngleUnit.normalizeDegrees(relativeFieldAngle - robot.getAngle());
            telemetry.addData("Angle difference", angleDifference);

            // Determine the speed that the motors should be set to.
            velocity = robot.getMotorTurnSpeed(relativeFieldAngle, robot.getAngle());
            telemetry.addData("Velocity", velocity);


            // Only make the robot turn relative to the field if a on the first gamepad is pressed.
            if(gamepad1.a){
                // Make the robot only turn at 50% speed.
                velocity *= 0.5;

                // Make the robot turn counterclockwise.
                robot.leftMotor.setPower(-velocity);
                robot.rightMotor.setPower(velocity);
            }
            else
            {
                // Make the robot stop.
                robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(0);
            }

        }
        else
        {
            // We want to face the tower goal, so set the angle to 0.
            relativeFieldAngle = 0;

            // Sets right and left motor powers to zero.
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);
        }
        // Find where the trackers are.

        if (timer.seconds() >= 1) {
             /*
             Log information every second
             1) timestamp
             2) robot angle
             3) visible?
             4) angleDiff
             5) velocity
             6) x diff
             7) y diff
             */
            String msg = String.format("%f, %f, %b, %f, %f, %f, %f", timestampTimer.seconds(), robot.getAngle(), isVisible, angleDifference, velocity, x,  y);

            logMessages.add(msg);

            timer.reset();
        }

        if (improvedGamepad.x.isInitialPress()) {

            try {
                FileUtilities.writeConfigFile("vuforiaLogFile.csv", logMessages);
            } catch (IOException e) {
                telemetry.addData("Error writing to file", e.getMessage());
            }
        }

        telemetry.update();

    }
}
