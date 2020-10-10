package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Hardware.ProgrammingUltimateGoalHardware;

import java.util.List;

import static org.firstinspires.ftc.teamcode.Hardware.ProgrammingUltimateGoalHardware.ELEMENT_SINGLE;
import static org.firstinspires.ftc.teamcode.Hardware.ProgrammingUltimateGoalHardware.ELEMENT_QUAD;

public class BaseUltimateGoalAuto extends LinearOpMode {
    public ProgrammingUltimateGoalHardware robot = new ProgrammingUltimateGoalHardware();
    private TFObjectDetector tfod;
    @Override
    public void runOpMode() throws InterruptedException {

    }
    public void init(boolean camera) {
        robot.init(this.hardwareMap);
        if(camera){
            initAndActivateWebCameraWithTensorFlow();
        }
    }
    public int starterStackSensor() {
        int stackID = 0;
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());

                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                    if(stackID != 2 && recognition.getLabel().equals(ELEMENT_SINGLE)) {
                        stackID = 1;
                    } else if(recognition.getLabel().equals(ELEMENT_QUAD)) {
                        stackID = 2;
                    }
                }
                telemetry.update();
            }
        }
        return stackID;
    }
    public void initAndActivateWebCameraWithTensorFlow() {

        // Init the web camera with TensorFlow
        robot.initWebCamera(hardwareMap);

        // Activate TensorFlow
        robot.webCamera.activateTfod();

        // Check for errors
        if (robot.webCamera.hasError()) {
            telemetry.addData("Failed!", robot.webCamera.errorMessage);
        } else {
            telemetry.addData("Successful!", "");
        }
    }

}
