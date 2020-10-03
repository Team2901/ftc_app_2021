package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware.ProgrammingUltimateGoalHardware;

public class BaseUltimateGoalAuto extends LinearOpMode {
    public ProgrammingUltimateGoalHardware robot = new ProgrammingUltimateGoalHardware();
    @Override
    public void runOpMode() throws InterruptedException {

    }
    public void init(boolean camera) {
        if(camera){
            initAndActivateWebCameraWithTensorFlow();
        }
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
