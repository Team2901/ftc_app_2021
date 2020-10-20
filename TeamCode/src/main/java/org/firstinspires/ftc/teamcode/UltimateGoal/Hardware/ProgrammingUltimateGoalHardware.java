package org.firstinspires.ftc.teamcode.UltimateGoal.Hardware;

import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.BaseUltimateGoalHardware;

public class ProgrammingUltimateGoalHardware extends BaseUltimateGoalHardware {
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