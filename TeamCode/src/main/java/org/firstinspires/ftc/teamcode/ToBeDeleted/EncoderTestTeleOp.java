package org.firstinspires.ftc.teamcode.ToBeDeleted;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@TeleOp(name = "encoder test count")
public class EncoderTestTeleOp extends OpMode {
    public DcMotor motor;
    @Override
    public void init() {
        motor = hardwareMap.dcMotor.get("testMotor");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void loop() {
        int encoderCount = motor.getCurrentPosition();
        telemetry.addData("encoder count: ", encoderCount);
        telemetry.update();
    }
}
