package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Shared.Hardware.MecanumHardware;


@TeleOp(name = "Mecanum", group = "Shared")
public class MecanumTeleOp extends OpMode {

    MecanumHardware robot = new MecanumHardware();

    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {

        double leftY = Math.abs(gamepad1.left_stick_y) > 0.1 ? -gamepad1.left_stick_y : 0;

        double leftX = Math.abs(gamepad1.left_stick_x) > 0.1 ? gamepad1.left_stick_x : 0;

        double rightX = Math.abs(gamepad1.right_stick_x) > 0.1 ? gamepad1.right_stick_x : 0;

        double[] wheelPower = wheelPower(leftX, leftY, rightX);

        robot.fLeft.setPower(wheelPower[0]);
        robot.fRight.setPower(wheelPower[1]);
        robot.bLeft.setPower(wheelPower[2]);
        robot.bRight.setPower(wheelPower[3]);

        telemetry.addData("speed: ", speed(leftX, leftY));
        telemetry.addData("angle: ", angle(leftX, leftY));
        telemetry.addData("left joystick y-value: ", leftY);
        telemetry.addData("left joystick x-value: ", leftX);
        telemetry.addData("right joystick x-value: ", rightX);
        telemetry.addData("front left motor: ", wheelPower[0]);
        telemetry.addData("front right motor: ", wheelPower[1]);
        telemetry.addData("back left motor: ", wheelPower[2]);
        telemetry.addData("back right motor: ", wheelPower[3]);
        telemetry.update();
    }

    public double[] wheelPower(double x, double y, double r) {

        double speed = 4*(speed(x, y) / 3);
        double angle = angle(x, y);

        double pFL = speed * (Math.sin(angle + Math.PI / 4)) + .75*r;
        double pFR = speed * (Math.cos(angle + Math.PI / 4)) - .75*r;
        double pBL = speed * (Math.cos(angle + Math.PI / 4)) + .75*r;
        double pBR = speed * (Math.sin(angle + Math.PI / 4)) - .75*r;

        if(pFL > 1){
            pFL = 1;
        } else if(pFL < -1){
            pFL = -1;
        }
        if(pFR > 1){
            pFR = 1;
        }else if(pFR < -1){
            pFR = -1;
        }
        if(pBL > 1){
            pBL = 1;
        }else if(pBL < -1){
            pBL = -1;
        }
        if(pBR > 1){
            pBR = 1;
        }else if(pBR < -1){
            pBR = -1;
        }

        return new double[]{pFL, pFR, pBL, pBR};
    }

    public double speed(double x, double y) {
        return Math.sqrt((Math.pow(x, 2)) + (Math.pow(y, 2)));
    }

    public double angle(double x, double y) {
        if ((y == 0) && (x == 0))
            return 0;
        else
            return Math.atan2(x, y);
    }
}
