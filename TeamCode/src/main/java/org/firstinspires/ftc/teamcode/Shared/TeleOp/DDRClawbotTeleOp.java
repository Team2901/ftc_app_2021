package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Shared.Hardware.ClawbotHardware;

/**
 * Created by Kearneyg20428 on 2/7/2017.
 */
@Disabled
@TeleOp(name = "DDR Clawbot", group = "Shared")
public class DDRClawbotTeleOp extends OpMode {

    final double CLAW_SPEED = 0.05;
    final ClawbotHardware robot = new ClawbotHardware();
    double clawOffset = 0.0;
    boolean isLastClawPressed = false;
    boolean isClawOpen = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.claw.setPosition(ClawbotHardware.MID_SERVO);
    }

    @Override
    public void loop() {
        double participantLeftPower;
        double participantRightPower;
        double participantArmPower;

        // gm = game master
        double gmLeftPower = 0;
        double gmRightPower = 0;
        double gmArmPower = 0;

        /*if(robot.potentiometer.getVoltage() < ClawbotHardware.MIN_ARM_VOLTAGE){
            robot.armMotor.setPower(0.3);
        } else if(robot.potentiometer.getVoltage() > ClawbotHardware.MAX_ARM_VOLTAGE){
            robot.armMotor.setPower(-0.3);
        }*/

        // Moves robot forward using the left joystick
        if(this.gamepad2.left_stick_y > 0.5){
            gmLeftPower += 1;
            gmRightPower += 1;
        }

        // Moves the robot backward using the left joystick
        if(gamepad2.left_stick_y < -0.5){
            gmLeftPower += -1;
            gmRightPower += -1;
        }

        // Turns the robot counterclockwise using the left bumper.
        if(gamepad2.left_bumper){
            gmLeftPower += -0.5;
            gmRightPower += 0.5;
        }

        // Turns the robot clockwise using the right bumper.
        if(gamepad2.right_bumper){
            gmLeftPower += 0.5;
            gmRightPower += -0.5;
        }

        double maxPower = Math.max(Math.abs(gmLeftPower), Math.abs(gmRightPower));

        if(maxPower > 1){
            gmRightPower /= maxPower;
            gmLeftPower /= maxPower;
        }

        //Topleft + Up = arc counterclockwise
        //Left power = 0.75, Right power = 1
        if(this.gamepad1.left_bumper && this.gamepad1.dpad_up){
            participantLeftPower = 0.75;
            participantRightPower = 1;

        } //Topright + Up = arc clockwise
        //Left power = 1, Right power = 0.75
        else if(this.gamepad1.right_bumper && this.gamepad1.dpad_up){
            participantLeftPower = 1;
            participantRightPower = 0.75;

        } //Topleft = counterclockwise
        //Left power = -0.75, Right power = 0.75
        else if(this.gamepad1.left_bumper){
            participantLeftPower = -0.75;
            participantRightPower = 0.75;

        } //Topright = clockwise
        //Left power = 0.75, Right power = -0.75
        else if(this.gamepad1.right_bumper){
            participantLeftPower = 0.75;
            participantRightPower = -0.75;
        } //Up = straight
        //Left and right motors same power 0.75
        else if(this.gamepad1.dpad_up){
            participantLeftPower = 0.75;
            participantRightPower = 0.75;
        } else {
            participantLeftPower = 0;
            participantRightPower = 0;
        }

        //Dpad left moves the arm down, dpad right moves the arm up, else, it stays in place
        if(this.gamepad1.dpad_left) {
            participantArmPower = ClawbotHardware.ARM_DOWN_POWER;
        } else if(this.gamepad1.dpad_right){
            participantArmPower = ClawbotHardware.ARM_UP_POWER;
        } else {
            participantArmPower = 0;
        }

        //Checks to see if it is the initial press of dpad down
        if(this.gamepad1.dpad_down){
            if(!isLastClawPressed){
                isClawOpen = !isClawOpen;
            }
        }

        //If isClawOpen is true, opens the claw, otherwise it closes the claw
        if(isClawOpen){
            robot.claw.setPosition(ClawbotHardware.MID_SERVO - ClawbotHardware.MIN_SAFE_CLAW_OFFSET);
        } else {
            robot.claw.setPosition(ClawbotHardware.MID_SERVO - ClawbotHardware.MAX_SAFE_CLAW_OFFSET);
        }
        //Updates isLastClawPressed to current state of dpad down
        isLastClawPressed = this.gamepad1.dpad_down;

        //Sets power to motors
        power(participantLeftPower, participantRightPower);
        robot.armMotor.setPower(participantArmPower);
    }

    public void power(double left, double right) {
        robot.leftMotor.setPower(left);
        robot.rightMotor.setPower(right);
    }
}
