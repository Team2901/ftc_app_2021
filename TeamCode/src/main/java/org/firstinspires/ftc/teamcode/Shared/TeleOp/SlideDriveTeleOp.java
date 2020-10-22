package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Shared.Hardware.SlideDriveHardware;

/**
 * Created by Kearneyg20428 on 2/7/2017.
 */
@Disabled
@TeleOp(name = "Slide Drive", group = "Shared")
public class SlideDriveTeleOp extends OpMode {

    SlideDriveHardware robot = new SlideDriveHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        double xJoystick = gamepad1.right_stick_x;
        double yJoystick = -gamepad1.right_stick_y;

        robot.leftFrontMotor.setPower(yJoystick);
        robot.leftBackMotor.setPower(yJoystick);
        robot.rightFrontMotor.setPower(yJoystick);
        robot.rightBackMotor.setPower(yJoystick);

        robot.centerMotor.setPower(xJoystick);

    }
}
