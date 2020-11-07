package org.firstinspires.ftc.teamcode.UltimateGoal.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.BaseUltimateGoalHardware;

public class ProgrammingUltimateGoalHardware extends BaseUltimateGoalHardware {
    public DcMotor middleMotor = null;
    public DcMotor intakeMotor = null;
    public DcMotor shooterMotor = null;
    public DcMotor shooterMotor2 = null;
    public DcMotor transferMotor = null;
    public Servo wobbleGrabber;
    public DcMotor wobbleElbow;

    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);
        middleMotor = hwMap.dcMotor.get("middle_drive");
        middleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        middleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        try {
            intakeMotor = hwMap.dcMotor.get("intake_motor");
            shooterMotor = hwMap.dcMotor.get("shooter_motor");
            shooterMotor2 = hwMap.dcMotor.get("shooter_motor_2");
            transferMotor = hwMap.dcMotor.get("transfer_motor");
            wobbleElbow = hwMap.dcMotor.get("elbow");

            intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            shooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            shooterMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            shooterMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            transferMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            transferMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            wobbleElbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wobbleElbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        catch(Exception e){
        }
        wobbleGrabber = hwMap.servo.get("grabber");
    }

    /**
     * This helper method configures the wobble goal grabber to either be opened or closed.
     * @param isClosed determines whether we want the wobble grabber to close or open
     */
    public void configureWobbleGrabber(boolean isClosed){
        // If true is passed as the parameter, the wobble grabber will close.
        if(isClosed){
            wobbleGrabber.setPosition(0.25);
        }
        // Otherwise, the wobble grabber will remain open.
        else{
            wobbleGrabber.setPosition(0.75);
        }
    }
}