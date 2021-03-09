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
@TeleOp(name = "Clawbot", group = "Shared")
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
        double leftPower;
        double rightPower;
        double armPower;

        //Topleft + Up = arc counterclockwise
        //Left power = 0.75, Right power = 1
        if(this.gamepad1.left_bumper && this.gamepad1.dpad_up){
            leftPower = 0.75;
            rightPower = 1;

        } //Topright + Up = arc clockwise
        //Left power = 1, Right power = 0.75
        else if(this.gamepad1.right_bumper && this.gamepad1.dpad_up){
            leftPower = 1;
            rightPower = 0.75;

        } //Topleft = counterclockwise
        //Left power = -0.75, Right power = 0.75
        else if(this.gamepad1.left_bumper){
            leftPower = -0.75;
            rightPower = 0.75;

        } //Topright = clockwise
        //Left power = 0.75, Right power = -0.75
        else if(this.gamepad1.right_bumper){
            leftPower = 0.75;
            rightPower = -0.75;
        } //Up = straight
        //Left and right motors same power 0.75
        else if(this.gamepad1.dpad_up){
            leftPower = 0.75;
            rightPower = 0.75;
        } else {
            leftPower = 0;
            rightPower = 0;
        }

        //Dpad left moves the arm down, dpad right moves the arm up, else, it stays in place
        if(this.gamepad1.dpad_left) {
            armPower = ClawbotHardware.ARM_DOWN_POWER;
        } else if(this.gamepad1.dpad_right){
            armPower = ClawbotHardware.ARM_UP_POWER;
        } else {
            armPower = 0;
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
        power(leftPower, rightPower);
        robot.armMotor.setPower(armPower);
    }

    public void power(double left, double right) {
        robot.leftMotor.setPower(left);
        robot.rightMotor.setPower(right);
    }
}
