package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Shared.Hardware.FlagBotHardware;

/**
 * Created by Kearneyg20428 on 2/7/2017.
 */

@Disabled
@TeleOp(name = "Flagbot", group = "TeleOp")
public class FlagBotTeleOp extends OpMode {

    final double CLAW_SPEED = 0.05;
    double clawOffset = 0.0;
    boolean turbo = false;
    final FlagBotHardware robot = new FlagBotHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {

        if(gamepad1.right_trigger > .1) {
            turbo = !turbo;
        }

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        double left = -gamepad1.left_stick_y;
        double right = -gamepad1.right_stick_y;

        double speedRatio = turbo ? 1 : .5;

        robot.leftMotor.setPower(speedRatio * left);
        robot.rightMotor.setPower(speedRatio * right);

        // Do not let the servo position go too wide or the plastic gears will come unhinged.

        // Move both servos to new position.

        // Use gamepad buttons to move the arm up (Y) and down (A)
        if (gamepad1.a)
            robot.spinMotor.setPower(.75);
        else if (gamepad1.b)
            robot.spinMotor.setPower(-.75);
        else
            robot.spinMotor.setPower(0.0);

        // Send telemetry message to signify robot running;
    }
}
