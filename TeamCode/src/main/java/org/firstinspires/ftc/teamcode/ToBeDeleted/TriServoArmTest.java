package org.firstinspires.ftc.teamcode.ToBeDeleted;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp (name = "Tri Servo Arm Test")
public class TriServoArmTest extends OpMode {

    public DcMotor winchMotor;
    public DcMotor wormMotor;

    public Servo rotatingServo;
    public Servo pinchServo;

    @Override
    public void init() {
        winchMotor = hardwareMap.dcMotor.get("winchMotor");
        wormMotor = hardwareMap.dcMotor.get("wormMotor");

        rotatingServo = hardwareMap.servo.get("rotatingServo");
        pinchServo = hardwareMap.servo.get("pinchServo");
        rotatingServo.setPosition(0);
        pinchServo.setPosition(0);

    }

    @Override
    public void loop() {
        double leftStickY = -gamepad1.left_stick_y;
        if (leftStickY > 0.1) {
            winchMotor.setPower(0.75);
        } else if (leftStickY < -0.1) {
            winchMotor.setPower(-0.75);
        } else{
            winchMotor.setPower(0);
        }

        double rightStickY = -gamepad1.right_stick_y;
        if (rightStickY > 0.1) {
            wormMotor.setPower(1);
        } else if (rightStickY < -0.1) {
            wormMotor.setPower(-1);
        }else{
            wormMotor.setPower(0);
        }

        double positionRotatingServo = rotatingServo.getPosition();
        if (gamepad1.right_bumper){
            rotatingServo.setPosition(positionRotatingServo + 0.1);
        } else if (gamepad1.right_trigger > 0.1){
            rotatingServo.setPosition(positionRotatingServo - 0.1);
        } else if(gamepad1.right_trigger == 1 && gamepad1.right_bumper == false){
            rotatingServo.setPosition(positionRotatingServo);
        }
        double positionpinchServo = pinchServo.getPosition();
        if (gamepad1.left_bumper) {
            pinchServo.setPosition(positionpinchServo + 0.1);
        }else if (gamepad1.left_trigger > 0.1){
            pinchServo.setPosition((positionpinchServo - 0.1));
        }else if(gamepad1.left_trigger == 1 && gamepad1.left_bumper == false) {
            pinchServo.setPosition(positionRotatingServo);
        }
        telemetry.addData("winchMotor", winchMotor.getCurrentPosition());
        telemetry.addData("wormMotor", wormMotor.getCurrentPosition());

        telemetry.addData("rotatingServo", positionRotatingServo);
        telemetry.addData("pinchServo", positionpinchServo);
        telemetry.update();
    }
}
