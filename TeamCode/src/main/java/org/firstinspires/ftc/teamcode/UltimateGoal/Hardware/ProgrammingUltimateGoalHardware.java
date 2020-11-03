package org.firstinspires.ftc.teamcode.UltimateGoal.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.BaseUltimateGoalHardware;

public class ProgrammingUltimateGoalHardware extends BaseUltimateGoalHardware {
    public DcMotor middleMotor = null;
    public DcMotor intakeMotor = null;
    public DcMotor shooterMotor = null;
    public Servo ringServo;
    public Servo wobbleGrabber;
    //public DcMotor wobbleElbow;

    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);
        middleMotor = hwMap.dcMotor.get("middle_drive");
        try {
            intakeMotor = hwMap.dcMotor.get("intake_motor");
            shooterMotor = hwMap.dcMotor.get("shooter_motor");
        }
        catch(Exception e){
        }
        ringServo = hwMap.servo.get("ring_servo");
        wobbleGrabber = hwMap.servo.get("grabber");
        //wobbleElbow = hwMap.dcMotor.get("elbow");
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