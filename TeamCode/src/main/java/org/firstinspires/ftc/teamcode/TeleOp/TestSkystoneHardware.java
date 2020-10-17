package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.Hardware.BuilderSkystoneHardware;
import org.firstinspires.ftc.teamcode.Hardware.SkystoneHardware;

import java.util.ArrayList;

@Disabled
@TeleOp(name = "Hardware Test", group = "TEST")
public class TestSkystoneHardware extends OpMode {

    BuilderSkystoneHardware robot = new BuilderSkystoneHardware();
    ImprovedGamepad impGamepad;
    ElapsedTime timer = new ElapsedTime();

    DcMotor  motorUnderTest;
    Servo servoUnderTest;

    String[] motorNames = {"frontLeft","frontRight","backLeft","backRight","lift"};
    String[] servoNames = {"servoFrontLeft","servoFrontRight","servoBackLeft","servoBackRight","jaw"
            , "crane", "wrist", "leftGrabber", "rightGrabber"};

    int motorIndex;

    Servo[] servos = {robot.servoFrontLeft,robot.servoFrontRight,robot.servoBackLeft,robot.servoBackRight
    , robot.jaw, robot.crane, robot.wrist, robot.leftGrabber, robot.rightGrabber};
    int servoIndex;

    ArrayList<DcMotor> motorArrayList = new ArrayList<>();
    ArrayList<Servo> servoArrayList = new ArrayList<>();

    @Override
    public void init() {
        impGamepad = new ImprovedGamepad(this.gamepad1, this.timer, "GP");

        robot.init(hardwareMap);
        for(int i = 0; i < motorNames.length; i++){
            String motorName = motorNames[i];
            DcMotor motor = hardwareMap.dcMotor.get(motorName);
            motorArrayList.add(motor);
            telemetry.addData("Motor"+i,motorName,motor);

        }
        for(int i = 0; i < servoNames.length; i++){
            String servoName = servoNames[i];
            Servo servo = hardwareMap.servo.get(servoName);
            servoArrayList.add(servo);
            telemetry.addData("Servo"+i,servoArrayList.get(i));

        }

        telemetry.update();
    }

    @Override
    public void loop() {
        impGamepad.update();
        if(this.impGamepad.dpad_up.isInitialPress()){
            motorIndex++;
            if(motorIndex >= motorArrayList.size()){
                motorIndex = 0;
            }
        } else if(this.impGamepad.dpad_down.isInitialPress()) {
            motorIndex--;
            if(motorIndex < 0){
                motorIndex = motorArrayList.size() - 1;
            }
        }
        motorUnderTest = motorArrayList.get(motorIndex);

        if(this.impGamepad.dpad_right.isInitialPress()){
            servoIndex++;
            if(servoIndex >= servoArrayList.size()){
                servoIndex = 0;
            }
        } else if(this.impGamepad.dpad_left.isInitialPress()) {
            servoIndex--;
            if(servoIndex < 0){
                servoIndex = servoArrayList.size() - 1;
            }
        }

        servoUnderTest = servoArrayList.get(servoIndex);

        if(motorUnderTest != null){
            if(Math.abs(this.impGamepad.left_stick_x.getValue()) > 0.25 ){
                motorUnderTest.setPower(this.impGamepad.left_stick_x.getValue());
            } else {
                motorUnderTest.setPower(0);
            }

            if(this.impGamepad.a.isPressed()){
                motorUnderTest.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorUnderTest.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            telemetry.addData("Left X Stick", "Set Power");
            telemetry.addData("A","Resetting encoders");
            telemetry.addData("Current position", motorUnderTest.getCurrentPosition());
            telemetry.addData("Power", motorUnderTest.getPower());
            telemetry.addData("Motor name", motorNames[motorIndex]);
        }

        telemetry.addData("D Pad Up/Down", "Increment/decrement motors");

        if(servoUnderTest != null){
            if(this.impGamepad.left_bumper.isInitialPress()){
                servoUnderTest.setPosition(servoUnderTest.getPosition()-0.1);
            } else if(this.impGamepad.right_bumper.isInitialPress()){
                servoUnderTest.setPosition(servoUnderTest.getPosition()+0.1);
            } else if(this.impGamepad.left_trigger.isInitialPress()){
                servoUnderTest.setPosition(servoUnderTest.getPosition()-0.02);
            } else if(this.impGamepad.right_trigger.isInitialPress()) {
                servoUnderTest.setPosition(servoUnderTest.getPosition()+0.02);
            }

            telemetry.addData("Left bumper","-0.1");
            telemetry.addData("Right bumper","+0.1");
            telemetry.addData("Left trigger","-0.2");
            telemetry.addData("Right trigger","+0.2");
            telemetry.addData("Position", servoUnderTest.getPosition());
            telemetry.addData("Servo name",servoNames[servoIndex]);
        }

        telemetry.addData("D Pad Right/Left", "Increment/decrement servos");

        telemetry.update();

    }
}
