package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Disabled
@Autonomous (name = "Moving Forward", group = "new_programmer")
public class RoundAndRound extends LinearOpMode {
    DcMotor leftDrive;
    DcMotor rightDrive;

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY ="AYhwTMH/////AAABmR7oFvU9lEJTryl5O3jDSusAPmWSAx5CHlcB/" +
            "IUoT+t7S1pJqTo7n3OwM4f2vVULA0T1uZVl9i61kWldhVqxK2+kyBNI4Uld8cYgHaNIQFsL/NsyBrb3Zl+1ZFBR" +
            "tpI5BjPnJkivkDsGU0rAFd+vPkyZt0p3/Uz+50eEwMZrZh499IsfooWkGX1wobjOFeA7DYQU+5ulhc1Rdp4mqjj" +
            "uKrS24Eop0MKJ+PwvNJhnN4LqIWQSfSABmcw9ogaeEsCzJdowrpXAcSo9d+ykJFZuB92iKN16lC9dRG3PABt26o" +
            "lSUCeXJrC4g6bEldHlmTc51nRpix6i1sGfvNuxlATzuRf5dtX/YlQm2WvvG9TilHbz";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        initVuforia();

        initTfod();

        if (tfod != null) {
            tfod.activate();
        }

        this.waitForStart();


        while (opModeIsActive()){

            Recognition recognition = getFirstRecognition();

            if (recognition != null) {
                int centerFrame = recognition.getImageWidth() / 2;
                float centerStone = (recognition.getRight() + recognition.getLeft()) / 2;
                float centerDifference = centerStone - centerFrame;
                float centerPercentDifference = (centerDifference / centerFrame) * 100;

                //If x > 380, the skystone is in position three. (Three away from the edge) If x > 620 it is at position 2, and if x > 350 it is in position 1
                telemetry.addData(String.format("label"), recognition.getLabel());
                telemetry.addData(String.format("left,top"), "%.03f , %.03f",
                        recognition.getLeft(), recognition.getTop());
                telemetry.addData(String.format("right,bottom"), "%.03f , %.03f",
                        recognition.getRight(), recognition.getBottom());

                telemetry.addData("Center Frame", centerFrame);
                telemetry.addData("Center Stone", centerStone);
                telemetry.addData("Difference", centerDifference);
                telemetry.addData("Percent Difference", centerPercentDifference);

                goForward((int) centerDifference);

                telemetry.update();
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    public void goForward(int direction) {
        int motorTicksPerRev = 1120;
        int desiredDistance = 4;
        double wheelCircumference = Math.PI*4;
        double desiredEncoderTicks = Math.signum(direction)* motorTicksPerRev*(3*desiredDistance/wheelCircumference);

        telemetry.addData("Desired Encoder Ticks",desiredEncoderTicks);
        telemetry.update();


        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition((int) desiredEncoderTicks);
        rightDrive.setTargetPosition((int) desiredEncoderTicks);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(0.25);
        rightDrive.setPower(0.25);

        while (leftDrive.isBusy()&& rightDrive.isBusy()){

        }
    }

    public Recognition getFirstRecognition() {
        if (tfod != null) {
            List<Recognition> recognitions = tfod.getRecognitions();
            if (recognitions != null && recognitions.size() > 0) {
                telemetry.addData("# Object Detected", recognitions.size());
                return recognitions.get(0);
            }
        }

        return null;
    }

    public void initRobot() {
        leftDrive = this.hardwareMap.dcMotor.get("left_drive");
        rightDrive = this.hardwareMap.dcMotor.get("right_drive");

        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);


        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}
