package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.SkystoneHardware;

@Disabled
@TeleOp(name="TestBrigitteScissorsLiftStep", group = "TEST")
public class TestBrigitteScissorsLiftStep extends OpMode{
    private boolean isYPressed;
    private boolean isXPressed;
    private boolean isBPressed;
    private boolean isAPressed;
    double power=0;
    int step = 0;
    int topStep = 0;

    SkystoneHardware robot = new SkystoneHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

    @Override
    public void loop() {
        if(this.gamepad1.y && !isYPressed){
            step = step+1;
            if(step> 5)
            {
             step=5;
            }
            power= .5;
            //up by one
        }
        if(this.gamepad1.a && !isAPressed){
            step = step-1;
            if(step < 0){
                step = 0;
            }
            power= .5;
            //down by one
        }

        if(this.gamepad1.b && !isBPressed){
            topStep = step;
            step = 0;
            power=.7;
            //to bottom
        }

        if(this.gamepad1.x && !isXPressed){
            step = topStep;
            power= .7;
            //to top
        }
        telemetry.addData("Step", step);
        telemetry.addData("Top Step", topStep);
        telemetry.update();

        isYPressed = gamepad1.y;
        isXPressed = gamepad1.x;
        isBPressed = gamepad1.b;
        isAPressed = gamepad1.a;


        //DcMotor lift = this.hardwareMap.dcMotor.get("lift");
        int targetPosition = step*750;

        robot.lift.setTargetPosition(targetPosition);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(power);




        telemetry.update();
    }

}
