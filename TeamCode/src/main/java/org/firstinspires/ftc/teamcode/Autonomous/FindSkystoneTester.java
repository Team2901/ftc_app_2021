package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous(name = "Find Skystone Tester", group = "__TEST")
public class FindSkystoneTester extends BaseSkyStoneAuto {

    final static int CONFIDENCE_PERCENTAGE = 5;

    @Override
    public void runOpMode() throws InterruptedException {

        // Step 0) Initialize robot and web camera with TensorFlow
        //robot.init(hardwareMap);


        initAndActivateWebCameraWithTensorFlow();

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        // Wait for start
        waitForStart();

        while (opModeIsActive()) {

            findSkyStone();
        }




    }


}
