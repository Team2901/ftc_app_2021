package org.firstinspires.ftc.teamcode.Shared.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

public class BaseCamera {

    public VuforiaLocalizer.Parameters parameters;
    public VuforiaLocalizer vuforia;
    public WebcamName webcamName;
    public String errorMessage;
    public VuforiaTrackables vuforiaTrackables;
    public TFObjectDetector tfod;
    protected int cameraMonitorViewId;
    protected int tfodMonitorViewId;

    public String initBackCamera(final HardwareMap hardwareMap) {
        return initBackCamera(hardwareMap, true);
    }

    public String initBackCamera(final HardwareMap hardwareMap,
                                 final boolean showView) {

        final VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getBackCameraParameters(hardwareMap, showView);
        return init(hardwareMap, parameters);
    }

    public String initWebCamera(final HardwareMap hardwareMap,
                                final String configName) {
        return initWebCamera(hardwareMap, configName, true);
    }

    public String initWebCamera(final HardwareMap hardwareMap,
                                final String configName,
                                final boolean showView) {

        try {
            webcamName = hardwareMap.get(WebcamName.class, configName);
            final VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getWebCameraParameters(hardwareMap, webcamName, showView);
            return init(hardwareMap, parameters);
        } catch (Exception e) {
            errorMessage = "Failed to init WebCamera. " + e.getMessage();
            return errorMessage;
        }
    }

    public String init(final HardwareMap hardwareMap,
                       final VuforiaLocalizer.Parameters parameters) {

        if (vuforia == null) {
            this.parameters = parameters;
            this.cameraMonitorViewId = VuforiaUtilities.getCameraMonitorViewId(hardwareMap);
            this.tfodMonitorViewId = VuforiaUtilities.getTfodMonitorViewId(hardwareMap);
            this.vuforia = VuforiaUtilities.getVuforia(parameters);
        }

        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }

    // ~~~ Vuforia ~~~

    public String loadVuforiaTrackables(final String assetName) {

        if (this.vuforiaTrackables == null && vuforia != null) {
            this.vuforiaTrackables = this.vuforia.loadTrackablesFromAsset(assetName);
        }

        return errorMessage;
    }

    public void activateVuforiaTrackables() {
        if (this.vuforiaTrackables != null) {
            this.vuforiaTrackables.activate();
        }
    }

    public void deactivateVuforiaTrackables() {
        if (this.vuforiaTrackables != null) {
            this.vuforiaTrackables.deactivate();
        }
    }

    // ~~~ Tensor Flow  ~~~
    public String initTfod(final double minimumConfidence,
                           final String assetName,
                           final String... labels) {

        if (tfod == null && vuforia != null) {
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
            tfodParameters.minResultConfidence = (float) minimumConfidence;
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(assetName, labels);
            //tfod.setZoom(2, 1.7);
            //tfod.setClippingMargins(0, 200, 200, 0);
        }

        return errorMessage;
    }

    public void activateTfod() {
        if (tfod != null) {
            tfod.activate();
        }
    }

    public void deactivateTfod() {
        if (tfod != null) {
            tfod.deactivate();
        }
    }

    public void shutdownTfod() {
        if (tfod != null) {
            tfod.shutdown();
        }
    }

    public void ultimateGoalSetupTrackables(OpenGLMatrix webcamLocation){
        // Checks to see if the trackables are available before we start.
        if(vuforiaTrackables == null){
            return;
        }

        // Saves blue tower trackable.
        VuforiaTrackable vuforiaBlueTower = vuforiaTrackables.get(0);

        // This is used for telemetry purposes for identifying that the camera is seeing the blue tower trackable.
        vuforiaBlueTower.setName("Blue Tower");

        // This props up the blue tower, it is currently in the middle of the field.
        OpenGLMatrix blueTowerLocation = OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90,0,-90);
        vuforiaBlueTower.setLocation(blueTowerLocation);

        // Saves red tower trackable.
        VuforiaTrackable vuforiaRedTower = vuforiaTrackables.get(1);

        // This is used for telemetry purposes for identifying that the camera is seeing the red tower trackable.
        vuforiaRedTower.setName("Red Tower");

        // This props up the red tower, it is currently in the middle of the field.
        OpenGLMatrix redTowerLocation = OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90,0,-90);
        vuforiaRedTower.setLocation(redTowerLocation);

        // We are telling the blue tower image where the camera is on the robot.
        VuforiaTrackable.Listener webcamListenerBlue = vuforiaBlueTower.getListener();
        VuforiaTrackableDefaultListener webcamDefaultListenerBlue = (VuforiaTrackableDefaultListener) webcamListenerBlue;
        webcamDefaultListenerBlue.setCameraLocationOnRobot(parameters.cameraName, webcamLocation);

        // We are telling the red tower image where the camera is on the robot.
        VuforiaTrackable.Listener webcamListenerRed = vuforiaRedTower.getListener();
        VuforiaTrackableDefaultListener webcamDefaultListenerRed = (VuforiaTrackableDefaultListener) webcamListenerRed;
        webcamDefaultListenerRed.setCameraLocationOnRobot(parameters.cameraName, webcamLocation);
    }

    public OpenGLMatrix getRobotLocation(){
        // If they are no trackables, then we return null.
        if(vuforiaTrackables == null){
            return null;
        }

        // Check if trackers are visible.
        for(int i = 0; i < vuforiaTrackables.size(); i++) {
            // Store current element in variable.
            VuforiaTrackable currentTrackable = vuforiaTrackables.get(i);

            // Gets listener before checking if current tracker is visible.
            VuforiaTrackableDefaultListener currentTrackableDefaultListener = (VuforiaTrackableDefaultListener) currentTrackable.getListener();

            // Determines whether current trackable is visible.
            boolean isTrackableVisible = currentTrackableDefaultListener.isVisible();

            if(isTrackableVisible){
                // Gets the robot's location.
                return currentTrackableDefaultListener.getRobotLocation();
            }
        }
        // If we get to this point, we know none of the trackables are visible and we return null.
        return null;
    }

    public Double getRobotTurnAngle(OpenGLMatrix robotLocation, boolean defaultToNull){
        if(robotLocation == null){
            if(defaultToNull){
                return null;
            }else{
                return 0.0;
            }
        }
        // This gets what this trackable thinks that the robot's position is.
        VectorF robotLocationTranslation = robotLocation.getTranslation();
        float x = robotLocationTranslation.get(0);
        float y = robotLocationTranslation.get(1);

        // Calculate the angle relative to the field and print it out.
        return Math.toDegrees(Math.atan(y/x));
    }
}
