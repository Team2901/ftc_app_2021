package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware.SkystoneHardware;

@Disabled
@Autonomous (name = "Kristen V5 Autonomous", group = "new_programmer")
public class KristenAutonomous extends LinearOpMode {

    //DcMotor leftMotor;
    //DcMotor rightMotor;
    public static SkystoneHardware robot = new SkystoneHardware();
    public Servo leftGrabber;
    public Servo rightGrabber;
    public Servo  centerGrabber;
    public static final double LEFT_GRABBER_MIN = 1.0;
    public static final double RIGHT_GRABBER_MAX = 0;
    public static final double LEFT_GRABBER_MAX = 0.35;
    public static final double RIGHT_GRABBER_MIN =0.65;
    public static final double WHEEL_CIRCUMFERENCE = 2 * Math.PI;
    public static final double GEAR_RATIO =  2;
    public static final double ENCODER_COUNTS_PER_MOTOR_REV = 1960 ;
    public static final int TARGET_COUNT = 7747;

    public int  getTargetEncoderCounts(int distanceInches){
        return (int)(distanceInches/WHEEL_CIRCUMFERENCE * GEAR_RATIO * ENCODER_COUNTS_PER_MOTOR_REV);
        }


    @Override
    public void runOpMode() throws InterruptedException {

        //rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftGrabber = hardwareMap.get(Servo.class, "Left_grabber");
        rightGrabber = hardwareMap.get(Servo.class, "Right_grabber");
        centerGrabber = hardwareMap.get(Servo.class, "Center_grabber");

        rightGrabber.setPosition (RIGHT_GRABBER_MAX);//0.3
        leftGrabber.setPosition (LEFT_GRABBER_MIN);//0.7
        centerGrabber.setPosition(0);
        telemetry.addData("Right:" , rightGrabber.getPosition());
        telemetry.addData("Left:" , leftGrabber.getPosition());
        telemetry.update();
        robot.init(hardwareMap);


        waitForStart();

        robot.moveStraight(0.5 , TARGET_COUNT);
        while(robot.wheelsAreBusy()){
            idle();
            telemetry.addData("Front Left Position" , robot.frontLeft.getCurrentPosition());
            telemetry.addData("Front Right Position" , robot.frontRight.getCurrentPosition());
            telemetry.addData("Back Left Position" , robot.backLeft.getCurrentPosition());
            telemetry.addData("Back Right Position" , robot.backRight.getCurrentPosition());
            telemetry.update();
        }


        rightGrabber.setPosition (RIGHT_GRABBER_MIN);
        leftGrabber.setPosition (LEFT_GRABBER_MAX);
        telemetry.addData("Right:" , rightGrabber.getPosition());
        telemetry.addData("Left:" , leftGrabber.getPosition());
        telemetry.update();

        /*leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftMotor.setTargetPosition(0);
        rightMotor.setTargetPosition(0);

        leftMotor.setPower(.25);
        rightMotor.setPower(.25);
        */
        while (this.opModeIsActive()){

            idle();

        }

    }

}


