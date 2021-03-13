package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Shared.Hardware.ClawbotHardware;

@Disabled
@TeleOp(name = "Potentiometer Soft Stops Tester", group = "Shared Test")
public class TestPotentiometerSoftStops extends OpMode {

    AnalogInput potentiometer;
    double maxOgVoltage;
    double floorMin = 1;
    DcMotor arm;

    @Override
    public void init() {
        potentiometer = hardwareMap.analogInput.get("potentiometer");
        arm = hardwareMap.dcMotor.get("arm");
        maxOgVoltage = potentiometer.getMaxVoltage();
    }

    @Override
    public void loop() {
        /*
         * Use joystick y to control moving arm up and down
         * Use a to move the arm up off the floor
         * 3 voltage = min voltage, max voltage, and min off the floor voltage
         */
        double armPower = 0;
        if(gamepad1.a && potentiometer.getVoltage() < floorMin){
            armPower = 0.3;
        }
        if(gamepad1.left_stick_y > 0.5 && potentiometer.getVoltage() < ClawbotHardware.MAX_ARM_VOLTAGE){
            armPower = gamepad1.left_stick_y;
        } else if(gamepad1.left_stick_y < -0.5 && potentiometer.getVoltage() > ClawbotHardware.MIN_ARM_VOLTAGE){
            armPower = gamepad1.left_stick_y;
        }
        arm.setPower(armPower);

        double angle = (potentiometer.getVoltage() / potentiometer.getMaxVoltage()) * 270;
        double badAngle = ((potentiometer.getVoltage() / maxOgVoltage) * 270);
        telemetry.addData("get Voltage", potentiometer.getVoltage());
        telemetry.addData("get Max Voltage", potentiometer.getMaxVoltage());
        telemetry.addData("Angle", angle);
        telemetry.addData("Angle with regards to the original max voltage", badAngle);
        telemetry.addData("Connection Info", potentiometer.getConnectionInfo());
        telemetry.addData("Arm Power", armPower);
        telemetry.update();


    }
}
