package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Hardware.ProgrammingUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

import static org.firstinspires.ftc.teamcode.Hardware.BaseSkyStoneHardware.WEB_CAM_NAME;

@TeleOp(name = "Web Camera Test", group = "TEST")
public class TestWebCamera extends LinearOpMode {

    public ElapsedTime timer = new ElapsedTime();

    public ProgrammingUltimateGoalHardware robot = new ProgrammingUltimateGoalHardware();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.initWebCamera(hardwareMap);

        robot.initTfod();

        robot.webCamera.activateTfod();

        if (robot.webCamera.hasError()) {
            telemetry.addData("Failed!",  robot.webCamera.errorMessage);
        } else {
            telemetry.addData("Successful!", "");
        }

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        waitForStart();

        // Do this to force a crash if the webcam isn't configured correctly
        WebcamName webcamName = hardwareMap.get(WebcamName.class, WEB_CAM_NAME);
        final VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getWebCameraParameters(hardwareMap, webcamName, true);
        robot.webCamera.init(hardwareMap, parameters);

        while (opModeIsActive()) {

        }
    }
}
