//TODO Delete
package org.firstinspires.ftc.teamcode.NewProgrammers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Disabled
@TeleOp(name = "WillsTeleopV2", group = "new_programmer")
public class WillsTeleop extends OpMode {

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_BUTTER = "Stone";
    private static final String LABEL_SKY_BUTTER = "Skystone";
    private float skyStonePosition;
    private double skyStoneGridLocation;
    private static final double BLOCK_OFFSET = 30.0;
    public static final int LEFT_POSITION = 33;
    public static final int RIGHT_POSITION = 66;
    public static final int CENTER_POSITION = 50;
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public Servo leftGrabber;
    public Servo rightGrabber;
    public double leftGrabberOffset = 1;
    public double rightGrabberOffset = 0;
    public double GRABBER_SPEED = 0.01;

    private static final String VUFORIA_KEY ="AYhwTMH/////AAABmR7oFvU9lEJTryl5O3jDSusAPmWSAx5CHlcB/" +
            "IUoT+t7S1pJqTo7n3OwM4f2vVULA0T1uZVl9i61kWldhVqxK2+kyBNI4Uld8cYgHaNIQFsL/NsyBrb3Zl+1ZFBR" +
            "tpI5BjPnJkivkDsGU0rAFd+vPkyZt0p3/Uz+50eEwMZrZh499IsfooWkGX1wobjOFeA7DYQU+5ulhc1Rdp4mqjj" +
            "uKrS24Eop0MKJ+PwvNJhnN4LqIWQSfSABmcw9ogaeEsCzJdowrpXAcSo9d+ykJFZuB92iKN16lC9dRG3PABt26o" +
            "lSUCeXJrC4g6bEldHlmTc51nRpix6i1sGfvNuxlATzuRf5dtX/YlQm2WvvG9TilHbz";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;
    @Override
    public void init() {
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        leftDrive .setDirection(DcMotorSimple.Direction.REVERSE);
        leftGrabber = hardwareMap.get(Servo.class, "Left_grabber");
        rightGrabber = hardwareMap.get(Servo.class, "Right_grabber");

        initVuforia();

        initTfod();

        if (tfod != null) {
            tfod.activate();
        } else {
            telemetry.addData("We Regret to Inform You", "Tensor flow has failed to initialize");
        }
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
    }

    @Override
    public void loop(){

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
        double left;
        double right;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        left = -gamepad1.left_stick_y;
        right = -gamepad1.right_stick_y;

        leftDrive.setPower(left);
        rightDrive.setPower(right);
        skyStonePosition = findSkyStone();
        telemetry.update();

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
        tfodParameters.minimumConfidence = 0.5;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_BUTTER, LABEL_SKY_BUTTER);
    }

    private float findSkyStone() {

        float centerPercentDifference = 0;
        float stonePercentLocation = 0;
        List<Recognition> updatedRecognitions = tfod.getRecognitions();
        if (updatedRecognitions != null) {

            Recognition highestConfidence = null;
            telemetry.addData("# Object Detected", updatedRecognitions.size());
            // step through the list of recognitions and display boundary info.
            int i = 0;
            for (Recognition recognition : updatedRecognitions) {
                if (recognition.getLabel().equals(LABEL_SKY_BUTTER)) {
                    if(highestConfidence == null || highestConfidence.getConfidence() < recognition.getConfidence()){
                        highestConfidence = recognition;
                    }
                }
            }
            if(highestConfidence != null) {
                //If x > 380, the skystone is in position three. (Three away from the edge) If x > 620 it is at position 2, and if x > 350 it is in position 1
                telemetry.addData(String.format("label (%d)", i), highestConfidence.getLabel());
                telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                        highestConfidence.getLeft(), highestConfidence.getTop());
                telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                        highestConfidence.getRight(), highestConfidence.getBottom());
                int centerFrame = highestConfidence.getImageWidth() / 2;
                float centerStone = (highestConfidence.getRight() + highestConfidence.getLeft()) / 2;
                telemetry.addData("Center Frame", centerFrame);
                telemetry.addData("Center Stone", centerStone);
                float centerDifference = centerStone - centerFrame;
                telemetry.addData("Difference", centerDifference);
                centerPercentDifference = (centerDifference / centerFrame) * 100;
                telemetry.addData("Percent Difference", centerPercentDifference);
                stonePercentLocation = (centerStone / highestConfidence.getImageWidth() * 100);
                telemetry.addData("percent location", stonePercentLocation);
            }else{
                telemetry.addData("we didn't find anything", "");
            }
        } else{
            telemetry.addData("# Object Detected", 0);
        }
        return stonePercentLocation;
    }

    private double convertPositionToGridOffset (int position){
        return 54.4;
    }

    private void moveForward (double inches){
        //telemetry.addData("Moved to a position in front of oneself", inches);
    }
    private void moveLeft (double inches) {
        //telemetry.addData("Moved to a position to the left of oneself", inches);
    }
    private void moveRight (double inches) {
        //telemetry.addData("Moved to a position to the right of oneself", inches);
    }
}
