package org.firstinspires.ftc.teamcode.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Programmer_MechanumHardware;

@Disabled
@TeleOp(name="ProgrammerMechanum")
public class ProgrammerMechanum extends OpMode {
    Programmer_MechanumHardware robot = new Programmer_MechanumHardware();

    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {

        if(gamepad1.right_trigger>0)
        {
            robot.clawServo.setPosition(.5);
        }
        else if (gamepad1.right_bumper)
        {
            robot.clawServo.setPosition(1);
        }
        

        if(gamepad1.left_trigger>0)
        {
            robot.arm.setPower(0.5);
        }
        else if(gamepad1.left_bumper)
        {
            robot.arm.setPower(-0.5);
        }
        else {
            robot.arm.setPower(0);
        }

        double leftY= Math.abs(gamepad1.left_stick_y) > 0.3? -gamepad1.left_stick_y: 0 ;

        double leftX= Math.abs(gamepad1.left_stick_x) > 0.3? gamepad1.left_stick_x: 0 ;

        double rightX= Math.abs(gamepad1.right_stick_x) > 0.3? gamepad1.right_stick_x: 0;

        // double rihgtY=Math.abs(gamepad1.right_stick_y) > 0.3? -gamepad1.right_stick_y: 0;

        double[] wheelPower = wheelPower(leftX, leftY, rightX);
        robot.fLeft.setPower(wheelPower[0]);
        robot.fRight.setPower(wheelPower[1]);
        robot.bLeft.setPower(wheelPower[2]);
        robot.bRight.setPower(wheelPower[3]);

        telemetry.addData("left joystick y-value: ",leftY);
        telemetry.addData("left joystick x-value: ",leftX);
        telemetry.addData("right joystick x-value: ",rightX);
        telemetry.addData("front left motor: ",wheelPower[0]);
        telemetry.addData("front right motor: ",wheelPower[1]);
        telemetry.addData("back left motor: ",wheelPower[2]);
        telemetry.addData("back right motor: ",wheelPower[3]);
        telemetry.addData("right trigger", gamepad1.right_trigger);
        telemetry.addData("right bumber", gamepad1.right_bumper);
        telemetry.update();
    }

    public double[] wheelPower(double x, double y, double r){

        double speed = speed (x,y);
        double angle = angle (x,y);
        double pFL = (speed * (Math.sin((angle) + ((Math.PI)/4)))) + r;
        double pFR = (speed * (Math.cos((angle) + ((Math.PI)/4)))) - r;
        double pBL = (speed * (Math.cos((angle) + ((Math.PI)/4)))) + r;
        double pBR = (speed * (Math.sin((angle) + ((Math.PI)/4)))) - r;
        double[] wP = {pFL, pFR, pBL, pBR};
        telemetry.addData("speed: ",speed);
        telemetry.addData("angle: ",angle);

        return wP;
    }

    public double speed (double x, double y){
        return Math.sqrt((Math.pow(x,2)) + (Math.pow(y,2)));
    }

    public double angle (double x, double y){
        if ((y == 0)&&(x ==0))
            return 0;
        else
            return Math.atan2(x,y);
    }


}

