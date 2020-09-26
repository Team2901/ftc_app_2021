package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities.INCHES_TO_MM;

public class VuforiaCamera extends BaseCamera {

    public VuforiaTrackables vuforiaTrackables;

    public String initBackCamera(final HardwareMap hardwareMap) {
        return super.initBackCamera(hardwareMap, true);
    }

    public String initWebCamera(final HardwareMap hardwareMap,
                                          final String configName) {
        return super.initWebCamera(hardwareMap, configName, true);
    }

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

    public VuforiaTrackables loadVuforiaTrackablesSkystone() {

        loadVuforiaTrackables("Skystone");

        if (vuforiaTrackables != null) {

            // the height of the center of the target image above the floor
            final float mmTargetHeight = (6) * INCHES_TO_MM;

            // Constant for Stone Target
            final float stoneZ = 2.00f * INCHES_TO_MM;

            // Constants for the center support targets
            final float bridgeZ = 6.42f * INCHES_TO_MM;
            final float bridgeY = 23 * INCHES_TO_MM;
            final float bridgeX = 5.18f * INCHES_TO_MM;
            final float bridgeRotY = 59; // Units are degrees
            final float bridgeRotZ = 180;

            // Constants for perimeter targets
            final float halfField = 72 * INCHES_TO_MM;
            final float quadField  = 36 * INCHES_TO_MM;

            VuforiaTrackable stoneTarget = vuforiaTrackables.get(0);
            stoneTarget.setName("Stone Target");
            VuforiaTrackable blueRearBridge = vuforiaTrackables.get(1);
            blueRearBridge.setName("Blue Rear Bridge");
            VuforiaTrackable redRearBridge = vuforiaTrackables.get(2);
            redRearBridge.setName("Red Rear Bridge");
            VuforiaTrackable redFrontBridge = vuforiaTrackables.get(3);
            redFrontBridge.setName("Red Front Bridge");
            VuforiaTrackable blueFrontBridge = vuforiaTrackables.get(4);
            blueFrontBridge.setName("Blue Front Bridge");
            VuforiaTrackable red1 = vuforiaTrackables.get(5);
            red1.setName("Red Perimeter 1");
            VuforiaTrackable red2 = vuforiaTrackables.get(6);
            red2.setName("Red Perimeter 2");
            VuforiaTrackable front1 = vuforiaTrackables.get(7);
            front1.setName("Front Perimeter 1");
            VuforiaTrackable front2 = vuforiaTrackables.get(8);
            front2.setName("Front Perimeter 2");
            VuforiaTrackable blue1 = vuforiaTrackables.get(9);
            blue1.setName("Blue Perimeter 1");
            VuforiaTrackable blue2 = vuforiaTrackables.get(10);
            blue2.setName("Blue Perimeter 2");
            VuforiaTrackable rear1 = vuforiaTrackables.get(11);
            rear1.setName("Rear Perimeter 1");
            VuforiaTrackable rear2 = vuforiaTrackables.get(12);
            rear2.setName("Rear Perimeter 2");

            // Set the position of the Stone Target.  Since it's not fixed in position, assume it's at the field origin.
            // Rotated it to to face forward, and raised it to sit on the ground correctly.
            // This can be used for generic target-centric approach algorithms
            stoneTarget.setLocation(OpenGLMatrix
                    .translation(0, 0, stoneZ)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

            //Set the position of the bridge support targets with relation to origin (center of field)
            blueFrontBridge.setLocation(OpenGLMatrix
                    .translation(-bridgeX, bridgeY, bridgeZ)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, bridgeRotZ)));

            blueRearBridge.setLocation(OpenGLMatrix
                    .translation(-bridgeX, bridgeY, bridgeZ)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, bridgeRotZ)));

            redFrontBridge.setLocation(OpenGLMatrix
                    .translation(-bridgeX, -bridgeY, bridgeZ)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, 0)));

            redRearBridge.setLocation(OpenGLMatrix
                    .translation(bridgeX, -bridgeY, bridgeZ)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, 0)));

            //Set the position of the perimeter targets with relation to origin (center of field)
            red1.setLocation(OpenGLMatrix
                    .translation(quadField, -halfField, mmTargetHeight)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

            red2.setLocation(OpenGLMatrix
                    .translation(-quadField, -halfField, mmTargetHeight)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

            front1.setLocation(OpenGLMatrix
                    .translation(-halfField, -quadField, mmTargetHeight)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90)));

            front2.setLocation(OpenGLMatrix
                    .translation(-halfField, quadField, mmTargetHeight)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

            blue1.setLocation(OpenGLMatrix
                    .translation(-quadField, halfField, mmTargetHeight)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

            blue2.setLocation(OpenGLMatrix
                    .translation(quadField, halfField, mmTargetHeight)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

            rear1.setLocation(OpenGLMatrix
                    .translation(halfField, quadField, mmTargetHeight)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , -90)));

            rear2.setLocation(OpenGLMatrix
                    .translation(halfField, -quadField, mmTargetHeight)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));
        }

        return vuforiaTrackables;
    }
}
