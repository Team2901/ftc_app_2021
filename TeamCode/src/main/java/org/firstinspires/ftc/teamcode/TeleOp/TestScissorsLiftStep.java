package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.Hardware.SkystoneHardware;

@Disabled
@TeleOp(name="ScissorsLiftStep Test", group = "TEST")
public class TestScissorsLiftStep extends OpMode {

    ImprovedGamepad impGamepad;
    ElapsedTime timer = new ElapsedTime();

    double power=0;
    int step = 0;
    int topStep = 0;

    SkystoneHardware robot = new SkystoneHardware();


    @Override
    public void init() {
        impGamepad = new ImprovedGamepad(this.gamepad2, this.timer, "GP2");
        robot.init(hardwareMap);
        DcMotor lift = this.hardwareMap.dcMotor.get("lift");
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);


    }


    @Override
    public void loop() {

        if(this.impGamepad.y.isInitialPress()){
            step = step+1;
           // power= .5;
            //up by one
        }
        if(this.impGamepad.a.isInitialPress()){
            step = step-1;
            if(step < 0){
                step = 0;
            }
            //power= -.5;
            //down by one
        }

        if(this.impGamepad.b.isInitialPress()){
            topStep = step;
            step = 0;
           // power=.7;
            //to bottom
        }

        if(this.impGamepad.x.isInitialPress()){
            step = topStep;
            //power= -.7;
            //to top
        }
        int targetPosition = step*500;

        robot.lift.setTargetPosition(targetPosition);
        robot.lift.setPower(power);

        telemetry.addData("Step", step);
        telemetry.addData("Top Step", topStep);
        telemetry.update();
    }
    /*public int stepByOne (int step)
    {
        if(gamepad1.left_bumper=true && gamepad1.y)
        {
            step = step+1;

        }
        if (gamepad1.right_bumper=true && gamepad1.a){
            step = step-1;
            if(step < 0){
                step = 0;
            }
        }
        telemetry.addLine("level: " + step);
        return step;
    }*/

}
