package org.firstinspires.ftc.teamcode.ToBeDeleted;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Autonomous.BaseSkyStoneAuto;

@Disabled
@Autonomous(name= "BluePlatform2")
public class Platform extends BaseSkyStoneAuto {

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        robot.swerveStraight(0, 0);
        waitForStart();
        //
        robot.setWheelMotorMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        moveInches(0, -12, 0.4);

        //Do swerve turn after turn
        moveInches(90, -34, 0.4);

        robot.setGrabberPositition(0, 0);

        //turnTo(90);
        //turnTo(90);
        while (opModeIsActive()) {

        }
    }
}
