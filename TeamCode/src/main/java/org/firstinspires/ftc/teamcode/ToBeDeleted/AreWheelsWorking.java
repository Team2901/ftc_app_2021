package org.firstinspires.ftc.teamcode.ToBeDeleted;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@TeleOp (name= "AreWheelsWorking")
public class AreWheelsWorking extends LinearOpMode {
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    @Override
    public void runOpMode() throws InterruptedException {
        double g1LeftStick = -gamepad1.left_stick_y;
        double g2LeftStick = -gamepad2.left_stick_y;

        if(g1LeftStick>0)
        {
            frontLeft.setPower(1);
            frontRight.setPower(1);
            backLeft.setPower(1);
            backRight.setPower(1);
        }
        if(g2LeftStick<0)
        {
            frontRight.setPower(-1);
            frontRight.setPower(-1);
            backLeft.setPower(-1);
            backRight.setPower(-1);
        }
    }
}
