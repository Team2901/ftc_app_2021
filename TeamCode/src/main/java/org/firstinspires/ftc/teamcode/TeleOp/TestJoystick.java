package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.SkystoneHardware;

@Disabled
@TeleOp(name = "Joystick Test", group = "TEST")
public class TestJoystick extends OpMode {

    @Override
    public void init() {
    }

    @Override
    public void loop() {
        /*
        Is this button pressed? If this button is pressed buttonBlink is pressed. The end goal
        is to print that the button has been pressed (this is to be done for one controller).
         */
        if(gamepad1.a){
            telemetry.addLine("button a is pressed");
        }
        if(gamepad1.b){
            telemetry.addLine("button b is pressed");
        }
        if(gamepad1.x){
            telemetry.addLine("button x is pressed");
        }
        if(gamepad1.y){
            telemetry.addLine("button y is pressed");
        }
        if(gamepad1.left_bumper){
            telemetry.addLine("left bumper is pressed");
        }
        if(gamepad1.right_bumper){
            telemetry.addLine("right bumper is pressed");
        }
        if(gamepad1.left_trigger>.25){
            telemetry.addLine("left trigger is pressed");
        }
        if(gamepad1.right_trigger >.25){
            telemetry.addLine("right trigger is pressed");
        }
        if(gamepad1.left_stick_x>0){
            telemetry.addLine("left joystick is pointed right");
        }
        if(gamepad1.left_stick_x<0){
            telemetry.addLine("left joystick is pointed left");
        }
        if(gamepad1.right_stick_x>0){
            telemetry.addLine("right joystick is pointed right");
        }
        if(gamepad1.right_stick_x<0){
            telemetry.addLine("right joystick is pointed left");
        }
        if(gamepad1.left_stick_y>0){
            telemetry.addLine("left joystick is pointed down");
        }
        if(gamepad1.left_stick_y<0){
            telemetry.addLine("left joystick is pointed up");
        }
        if(gamepad1.right_stick_y>0){
            telemetry.addLine("right joystick is pointed down");
        }
        if(gamepad1.right_stick_y<0){
            telemetry.addLine("right joystick is pointed up");
        }
        telemetry.update();
    }
}
