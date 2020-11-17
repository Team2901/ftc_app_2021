package org.firstinspires.ftc.teamcode.UltimateGoal.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Scrimmage Red Shoot and Parking", group = "Red Team")
public class ScrimmageRedShootAndParking extends BaseUltimateGoalAuto {
    public ScrimmageRedShootAndParking() {
        super(TeamColor.RED_TEAM);
    }

    @Override
    public void runOpMode(){
        init(false);
        waitForStart();
        //moveInchesCenter(12);
        //Robots preparing to shoot rings
        robot.shooterMotor.setPower(1);
        robot.shooterMotor2.setPower(1);
        //Move to shooting location
        moveInchesForward(66, false);
        //Feeding rings to shooter
        robot.intakeMotor.setPower(0.5);
        robot.transferMotor.setPower(0.5);
        //Waiting while robot is still feeding rings to shooter
        ElapsedTime timer = new ElapsedTime();
        while(
                timer.seconds()<= 3 && opModeIsActive()
        ) {

        }
        //Reach parking line
        moveInchesForward(6, false);
    }
}
