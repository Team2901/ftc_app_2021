package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.NewProgrammers.TestingTensorFlowWebcam;

import java.util.List;

@Disabled
@Autonomous(name = "SkyStoneLoadingAuto", group = "new_programmer")

public class SkyStoneLoadingAuto extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_BUTTER = "Stone";
    private static final String LABEL_SKY_BUTTER = "Skystone";
    private float skyStonePosition;
    private double skyStoneGridLocation;
    private static final double BLOCK_OFFSET = 30.0;
    public static final int LEFT_POSITION = 33;
    public static final int RIGHT_POSITION = 66;
    public static final int CENTER_POSITION = 50;

    private static final String VUFORIA_KEY ="AYhwTMH/////AAABmR7oFvU9lEJTryl5O3jDSusAPmWSAx5CHlcB/" +
            "IUoT+t7S1pJqTo7n3OwM4f2vVULA0T1uZVl9i61kWldhVqxK2+kyBNI4Uld8cYgHaNIQFsL/NsyBrb3Zl+1ZFBR" +
            "tpI5BjPnJkivkDsGU0rAFd+vPkyZt0p3/Uz+50eEwMZrZh499IsfooWkGX1wobjOFeA7DYQU+5ulhc1Rdp4mqjj" +
            "uKrS24Eop0MKJ+PwvNJhnN4LqIWQSfSABmcw9ogaeEsCzJdowrpXAcSo9d+ykJFZuB92iKN16lC9dRG3PABt26o" +
            "lSUCeXJrC4g6bEldHlmTc51nRpix6i1sGfvNuxlATzuRf5dtX/YlQm2WvvG9TilHbz";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    @Override
    public void runOpMode() throws InterruptedException {
        /*
        1.) Find skystone
        2.) Get skystone
            Move forward, activate intake
        3.) Move right forward direction
        4.) Turn 90 degrees counterclockwise
            Find the waffle using the camera
        5.) If the waffle is not in the corner, put it in the corner
        6.) If the waffle is already in the corner
            Move to waffle, place block
         7.) Place Capstone on block
         */
        // Step 1 Find Skystone

        initVuforia();

        initTfod();

        if (tfod != null) {
            tfod.activate();
        } else {
            telemetry.addData("We Regret to Inform You", "Tensor flow has failed to initialize");
        }
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        skyStonePosition = findSkyStone();

       // skyStoneGridLocation = convertPositionToGridOffset(skyStonePosition);


        //Step 2 Get Skystone

        moveForward(BLOCK_OFFSET);
        if(skyStonePosition <= LEFT_POSITION){
            telemetry.addData("stone is ", "to the left");
            moveLeft(5.0);
        }else if(skyStonePosition >= RIGHT_POSITION){
            telemetry.addData("stone is ", "to the right");
            moveRight(5.0);
        }else{
            telemetry.addData("stone is ", "in the center");
        }

        /*
        1.) Move waffle to the middle position
            Move forward, grab, drag back
        2.) Move right
            To go underneath the bridge
        3.) Find skystone
            Identify if there are three stones, if not, go to next location
        4.) Get skystone
        5.) Move right forward direction
        6.) Turn 180 degrees clockwise
        7.) Place block
        8.) Place Capstone on block
        9.) Return to starting position
            Go left, forward, and then left, turn 180 degrees
        10.) Go to step 3
         */
        while (opModeIsActive()){}
    }
    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_BUTTER, LABEL_SKY_BUTTER);
    }

    private float findSkyStone() {

        float centerPercentDifference = 0;
        float stonePercentLocation = 0;
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("# Object Detected", updatedRecognitions.size());
            // step through the list of recognitions and display boundary info.
            int i = 0;
            for (Recognition recognition : updatedRecognitions) {
                //If x > 380, the skystone is in position three. (Three away from the edge) If x > 620 it is at position 2, and if x > 350 it is in position 1
                telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                        recognition.getLeft(), recognition.getTop());
                telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                        recognition.getRight(), recognition.getBottom());
                int centerFrame = recognition.getImageWidth()/2;
                float centerStone = (recognition.getRight() + recognition.getLeft()) / 2;
                telemetry.addData("Center Frame", centerFrame);
                telemetry.addData("Center Stone", centerStone);
                float centerDifference = centerStone - centerFrame;
                telemetry.addData("Difference", centerDifference);
                centerPercentDifference = (centerDifference / centerFrame) * 100;
                telemetry.addData("Percent Difference", centerPercentDifference);
                stonePercentLocation = (centerStone / recognition.getImageWidth() * 100);
                if (recognition.getLabel().equals(LABEL_SKY_BUTTER))
                    break;
            }
            telemetry.update();
        }
        return stonePercentLocation;
    }

    private double convertPositionToGridOffset (int position){
        return 54.4;
    }

    private void moveForward (double inches){
        telemetry.addData("Moved to a position in front of oneself", inches);
    }
    private void moveLeft (double inches) {
        telemetry.addData("Moved to a position to the left of oneself", inches);
    }
    private void moveRight (double inches) {
        telemetry.addData("Moved to a position to the right of oneself", inches);
    }
}
