package org.firstinspires.ftc.teamcode.UltimateGoal.TeleOp;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.TankUltimateGoalHardware;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Vuforia UltimateGoal Test", group = "2021_UltimateGoal")
public class VuforiaUltimateGoalTest extends OpMode {
    public TankUltimateGoalHardware robot = new TankUltimateGoalHardware();
    @Override
    public void init() {
        robot.init(this.hardwareMap);
        robot.initWebCamera(this.hardwareMap);

        // Loading the Vuforia trackables.
        robot.webCamera.loadVuforiaTrackables("UltimateGoal");

        // Saves blue tower trackable.
        VuforiaTrackable vuforiaBlueTower = robot.webCamera.vuforiaTrackables.get(0);

        // This is used for telemetry purposes for identifying that the camera is seeing the blue tower trackable.
        vuforiaBlueTower.setName("Blue Tower");

        // This props up the blue tower, it is currently in the middle of the field.
        OpenGLMatrix blueTowerLocation = OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90,0,-90);
        vuforiaBlueTower.setLocation(blueTowerLocation);

        // Saves red tower trackable.
        VuforiaTrackable vuforiaRedTower = robot.webCamera.vuforiaTrackables.get(1);

        // This is used for telemetry purposes for identifying that the camera is seeing the red tower trackable.
        vuforiaRedTower.setName("Red Tower");

        // This props up the red tower, it is currently in the middle of the field.
        OpenGLMatrix redTowerLocation = OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90,0,-90);
        vuforiaRedTower.setLocation(redTowerLocation);

        // Sets up the position of the Vuforia web image and web camera.
        OpenGLMatrix webcamLocation = OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 0,-90,0);

        // We are telling the blue tower image where the camera is on the robot.
        VuforiaTrackable.Listener webcamListenerBlue = vuforiaBlueTower.getListener();
        VuforiaTrackableDefaultListener webcamDefaultListenerBlue = (VuforiaTrackableDefaultListener) webcamListenerBlue;
        webcamDefaultListenerBlue.setPhoneInformation(webcamLocation, VuforiaLocalizer.CameraDirection.BACK);

        // We are telling the red tower image where the camera is on the robot.
        VuforiaTrackable.Listener webcamListenerRed = vuforiaRedTower.getListener();
        VuforiaTrackableDefaultListener webcamDefaultListenerRed = (VuforiaTrackableDefaultListener) webcamListenerRed;
        webcamDefaultListenerRed.setPhoneInformation(webcamLocation, VuforiaLocalizer.CameraDirection.BACK);

        // We are ready to use the trackables and this gives us the opportunity to deactivate them later.
        robot.webCamera.vuforiaTrackables.activate();
    }

    @Override
    public void loop() {
        // Check if trackers are visible.
        for(int i = 0; i < robot.webCamera.vuforiaTrackables.size(); i++){
            // Store current element in variable.
            VuforiaTrackable currentTrackable = robot.webCamera.vuforiaTrackables.get(i);

            // Gets listener before checking if current tracker is visible.
            VuforiaTrackableDefaultListener currentTrackableDefaultListener = (VuforiaTrackableDefaultListener) currentTrackable.getListener();

            // Determines whether current trackable is visible.
            boolean isTrackableVisible = currentTrackableDefaultListener.isVisible();

            // Prints out whether the current trackable is visible.
            telemetry.addData(currentTrackable.getName() + " is visible", isTrackableVisible);

            // Gets the robot's location.
            OpenGLMatrix robotLocation = currentTrackableDefaultListener.getRobotLocation();

            /*
            * If the robot location is not null, we translate the robot's location. In other words,
            * if the tracker image is visible, we translate the robot's location.
             */
            if(robotLocation != null){
                // This gets what this trackable thinks that the robot's position is.
                VectorF robotLocationTranslation = robotLocation.getTranslation();
                float x = robotLocationTranslation.get(0);
                float y = robotLocationTranslation.get(1);
                float z = robotLocationTranslation.get(2);

                // This gets what this trackable thinks that the robot's orientation is.
                Orientation orientation = Orientation.getOrientation(robotLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                float xAngle = orientation.firstAngle;
                float yAngle = orientation.secondAngle;
                float zAngle = orientation.thirdAngle;

                // x, y, z positions (x, y, z)
                telemetry.addData("x, y, z positions", "(" + x + ", " + y +", " + z + ")");

                // x, y, z rotations (x, y, z)
                telemetry.addData("x, y, z rotations", String.format("(%f, %f, %f)", xAngle, yAngle, zAngle));
            }
        }

        // Find where the trackers are.

        telemetry.update();
    }
}
