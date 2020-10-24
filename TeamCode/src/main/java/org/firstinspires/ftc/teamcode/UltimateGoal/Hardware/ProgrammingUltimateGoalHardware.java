package org.firstinspires.ftc.teamcode.UltimateGoal.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.BaseUltimateGoalHardware;

public class ProgrammingUltimateGoalHardware extends BaseUltimateGoalHardware {
    public DcMotor middleMotor = null;
    public Servo wobbleGrabber;
    public DcMotor wobbleElbow;

    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);
        middleMotor = hwMap.dcMotor.get("middle_drive");
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

    /**
     * This helper method configures the wobble elbow to either keep extending the wobble elbow forward
     * while we are pressing the dpad up button or keep retracting the wobble elbow while we are
     * pressing the dpad down button.
     * @param isForward determines whether the wobble elbow should extend forward or backward.
     */
    public void configureWobbleElbow(boolean isForward){
        // If we are pressing the dpad up button, while we are pressing it, the wobble elbow will
        // keep extending forward.
        if(isForward){
            while(isForward){
                wobbleElbow.setPower(1);
            }
        }
        // Otherwise, while we are pressing the dpad down button, the wobble elbow will keep retracting.
        else{
            while(isForward){
                wobbleElbow.setPower(-1);
            }
        }
    }
}