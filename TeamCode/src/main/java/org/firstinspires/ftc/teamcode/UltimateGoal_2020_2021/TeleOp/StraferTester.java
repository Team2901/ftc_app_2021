package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.BaseUltimateGoalHardware;

@TeleOp(name = "Strafer Tester")
public class StraferTester extends OpMode {
    public BaseUltimateGoalHardware robot = new BaseUltimateGoalHardware();

    ImprovedGamepad impGamepad;
    ElapsedTime timer = new ElapsedTime();

    double middleMotorPower = .5;
    double pause;

    @Override
    public void init() {
        impGamepad = new ImprovedGamepad(this.gamepad1, this.timer, "GP1");
    }

    @Override
    public void loop() {
        impGamepad.update();

        if (impGamepad.a.isPressed()){
            pause = 1;
        }

        if(impGamepad.b.isPressed()){
            pause = 2;
        }

        if(pause == 1){
            robot.middleMotor.setPower(middleMotorPower);
        }
        else
            robot.middleMotor.setPower(0);

        if(impGamepad.dpad_up.isInitialPress() && middleMotorPower < 1){
            middleMotorPower += 0.1;
        }
        // If the dpad down button is pressed, the shooter power ratio will decrease by 0.1, assuming
        // that shooterPowerRatio is greater than 0.
        else if(impGamepad.dpad_down.isInitialPress() && middleMotorPower > 0){
            middleMotorPower -= 0.1;
        }

        }
    }
