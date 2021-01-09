package org.firstinspires.ftc.teamcode.SkyStone_2019_2020.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.SkyStone_2019_2020.Hardware.BuilderSkystoneHardware;

import java.util.ArrayList;

@Disabled
@TeleOp(name = "SkyStone Hardware Test", group = "2019_SkyStone")
public class SkystoneHardwareTestTeleOp extends OpMode {

    BuilderSkystoneHardware robot = new BuilderSkystoneHardware();
    ImprovedGamepad impGamepad;
    ElapsedTime timer = new ElapsedTime();

    DcMotor motorUnderTest;
    Servo servoUnderTest;

    // List of all of the motor names.
    String[] motorNames = {"frontLeft", "frontRight", "backLeft", "backRight", "lift"};

    // List of all of the servo names.
    String[] servoNames = {"servoFrontLeft", "servoFrontRight", "servoBackLeft", "servoBackRight", "jaw"
            , "crane", "wrist", "leftGrabber", "rightGrabber"};

    int motorIndex;

    int servoIndex;

    ArrayList<DcMotor> motorArrayList = new ArrayList<>();
    ArrayList<Servo> servoArrayList = new ArrayList<>();

    @Override
    public void init() {
        // Instantiating gamepad.
        impGamepad = new ImprovedGamepad(this.gamepad1, this.timer, "GP");

        // Calling the init method.
        robot.init(hardwareMap);

        // Adding all of the motor names to the motorArrayList.
        for (int i = 0; i < motorNames.length; i++) {
            String motorName = motorNames[i];
            DcMotor motor = hardwareMap.dcMotor.get(motorName);
            motorArrayList.add(motor);
            telemetry.addData("Motor" + i, motorName, motor);

        }

        // Adding all of the servo names to the servoArrayList.
        for (int i = 0; i < servoNames.length; i++) {
            String servoName = servoNames[i];
            Servo servo = hardwareMap.servo.get(servoName);
            servoArrayList.add(servo);
            telemetry.addData("Servo" + i, servoArrayList.get(i));

        }

        telemetry.update();
    }

    @Override
    public void loop() {
        impGamepad.update();
        // Use dpad up and dpad down button to either increment or decrement the motorIndex when
        // necessary.
        if (this.impGamepad.dpad_up.isInitialPress()) {
            motorIndex++;
            if (motorIndex >= motorArrayList.size()) {
                motorIndex = 0;
            }
        } else if (this.impGamepad.dpad_down.isInitialPress()) {
            motorIndex--;
            if (motorIndex < 0) {
                motorIndex = motorArrayList.size() - 1;
            }
        }
        // Identifying our desired motor to test.
        motorUnderTest = motorArrayList.get(motorIndex);

        // Use dpad right and dpad left button to either increment or decrement the servoIndex when
        // necessary.
        if (this.impGamepad.dpad_right.isInitialPress()) {
            servoIndex++;
            if (servoIndex >= servoArrayList.size()) {
                servoIndex = 0;
            }
        } else if (this.impGamepad.dpad_left.isInitialPress()) {
            servoIndex--;
            if (servoIndex < 0) {
                servoIndex = servoArrayList.size() - 1;
            }
        }

        // Identifying our desired servo to test.
        servoUnderTest = servoArrayList.get(servoIndex);


        if (motorUnderTest != null) {
            // Setting buffer zone for left stick to 0.25.
            if (Math.abs(this.impGamepad.left_stick_x.getValue()) > 0.25) {
                motorUnderTest.setPower(this.impGamepad.left_stick_x.getValue());
            }
            // If the left stick x is not at distance that is greater than the buffer zone, the
            // motor will not do anything.
            else {
                motorUnderTest.setPower(0);
            }

            // Resets encoder count for motor if a is pressed on the gamepad.
            if (this.impGamepad.a.isPressed()) {
                motorUnderTest.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorUnderTest.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            // Prints out information about the appropriate buttons and the motor we are testing.
            telemetry.addData("Left X Stick", "Set Power");
            telemetry.addData("A", "Resetting encoders");
            telemetry.addData("Current position", motorUnderTest.getCurrentPosition());
            telemetry.addData("Power", motorUnderTest.getPower());
            telemetry.addData("Motor name", motorNames[motorIndex]);
        }

        telemetry.addData("D Pad Up/Down", "Increment/decrement motors");

        if (servoUnderTest != null) {
            // Depending on the button that is pressed, the servo will turn by a certain amount either
            // in the positive direction or the negative direction.
            if (this.impGamepad.left_bumper.isInitialPress()) {
                servoUnderTest.setPosition(servoUnderTest.getPosition() - 0.1);
            } else if (this.impGamepad.right_bumper.isInitialPress()) {
                servoUnderTest.setPosition(servoUnderTest.getPosition() + 0.1);
            } else if (this.impGamepad.left_trigger.isInitialPress()) {
                servoUnderTest.setPosition(servoUnderTest.getPosition() - 0.02);
            } else if (this.impGamepad.right_trigger.isInitialPress()) {
                servoUnderTest.setPosition(servoUnderTest.getPosition() + 0.02);
            }

            // Prints out information about the appropriate buttons and the servo we are testing.
            telemetry.addData("Left bumper", "-0.1");
            telemetry.addData("Right bumper", "+0.1");
            telemetry.addData("Left trigger", "-0.2");
            telemetry.addData("Right trigger", "+0.2");
            telemetry.addData("Position", servoUnderTest.getPosition());
            telemetry.addData("Servo name", servoNames[servoIndex]);
        }

        telemetry.addData("D Pad Right/Left", "Increment/decrement servos");

        telemetry.update();

    }
}
