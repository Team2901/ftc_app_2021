package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Shared.Hardware.BaseCamera;

@Disabled
@TeleOp(name = "Camera Test", group = "TEST")
public class TestCamera extends OpMode {

    public ElapsedTime timer = new ElapsedTime();

    public BaseCamera tensorFlowCamera = new BaseCamera();

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    @Override
    public void init() {

        tensorFlowCamera.initBackCamera(hardwareMap);

        tensorFlowCamera.initTfod(.8, TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);

        tensorFlowCamera.activateTfod();
    }

    @Override
    public void loop() {

    }
}
