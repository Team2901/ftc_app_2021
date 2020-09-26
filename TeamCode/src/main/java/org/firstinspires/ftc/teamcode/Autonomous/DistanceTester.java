package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Disabled
@Autonomous (name = "DistanceTester", group = "new_programmer")
public class DistanceTester extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    DistanceSensor distanceSensor;

    @Override
    public void runOpMode() throws InterruptedException {

        leftMotor = hardwareMap.dcMotor.get("left_drive");
        leftMotor = hardwareMap.get(DcMotor.class, "left_drive");

        rightMotor = hardwareMap.dcMotor.get("right_drive");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "distance_sensor");

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (distanceSensor.getDistance(DistanceUnit.INCH) >2){
            leftMotor.setPower(.25);
            rightMotor.setPower(.25);
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftMotor.setTargetPosition(1140);
        rightMotor.setTargetPosition(-1140);

        leftMotor.setPower(.25);
        rightMotor.setPower(.25);
        while (leftMotor.isBusy()){

        }
    }
}
