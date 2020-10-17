package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public class TensorFlowCamera extends BaseCamera {

    public TFObjectDetector tfod;

    public String initBackCamera(final HardwareMap hardwareMap,
                                 final double minimumConfidence,
                                 final String assetName,
                                 final String... labels) {

        super.initBackCamera(hardwareMap, false);
        return initTfod(minimumConfidence, assetName, labels);
    }

    public String initWebCamera(final HardwareMap hardwareMap,
                                final String configName,
                                final double minimumConfidence,
                                final String assetName,
                                final String... labels) {

        super.initWebCamera(hardwareMap, configName,false);
        return initTfod(minimumConfidence, assetName, labels);
    }

    private String initTfod(final double minimumConfidence,
                            final String assetName,
                            final String... labels) {

        if (tfod == null && vuforia != null) {
             TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
                tfodParameters.minResultConfidence = (float)minimumConfidence;
                tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
                tfod.loadModelFromAsset(assetName, labels);
                tfod.setZoom(2.5,1.78);
                tfod.setClippingMargins(0,200,200,0);
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
}
