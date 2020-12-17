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
        ElapsedTime timer = new ElapsedTime();
        //moveInchesCenter(12);
        //Robots preparing to shoot rings
        robot.shooterMotor.setPower(1);
        robot.shooterMotor2.setPower(1);
        //Move to shooting location
        moveInchesForward(60, false);
        //Feeding rings to shooter
        //robot.intakeMotor.setPower(0.5);
        while(timer.seconds()<= 6 && opModeIsActive()){}
        robot.transferMotor.setPower(-0.5);
        //Waiting while robot is still feeding rings to shooter
        while(timer.seconds()<= 7 && opModeIsActive()) {

        }
        robot.shooterMotor.setPower(0);
        robot.shooterMotor2.setPower(0);
        robot.transferMotor.setPower(0);
        //Reach parking line
        moveInchesForward(12, false);
    }
}
