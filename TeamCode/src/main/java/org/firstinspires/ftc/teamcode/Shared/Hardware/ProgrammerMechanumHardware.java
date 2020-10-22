package org.firstinspires.ftc.teamcode.Shared.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ProgrammerMechanumHardware {


/*
I created this naming system as the letter f or b meaning front or back because
with machanum wheels all 4 wheels need to be motorized
*/

    public DcMotor fLeft = null;
    public DcMotor fRight = null;
    public DcMotor bLeft = null;
    public DcMotor bRight = null;
   // public DcMotor lift = null;
    public DcMotor arm = null;

/*
    public Servo jewelKnockDevice = null;
    public Servo bLeftPincer = null;
    public Servo bRightPincer = null;
    public Servo tLeftPincer = null;
    public Servo tRightPincer = null;
    */
    public Servo clawServo = null;

    HardwareMap hwMap = null;


    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        fLeft = hwMap.dcMotor.get("fLeft");
        fRight = hwMap.dcMotor.get("fRight");
        bLeft = hwMap.dcMotor.get("bLeft");
        bRight = hwMap.dcMotor.get("bRight");
      //  lift = hwMap.dcMotor.get("lift");
        arm = hwMap.dcMotor.get("arm");


       /* jewelKnockDevice = hwMap.servo.get("jewelKnockDevice");
        bLeftPincer = hwMap.servo.get("bLeftPincer");
        bRightPincer = hwMap.servo.get("bRightPincer");
        tLeftPincer = hwMap.servo.get("tLeftPincer");
        tRightPincer = hwMap.servo.get("tRightPincer");
       */
        clawServo = hwMap.servo.get("clawServo");

        fLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        fRight.setDirection(DcMotorSimple.Direction.FORWARD);
        bLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        bRight.setDirection(DcMotorSimple.Direction.FORWARD);
       // lift.setDirection(DcMotorSimple.Direction.REVERSE);


      /*  bLeftPincer.setDirection(Servo.Direction.FORWARD);
        bRightPincer.setDirection(Servo.Direction.REVERSE);
        tLeftPincer.setDirection(Servo.Direction.REVERSE);
        tRightPincer.setDirection(Servo.Direction.REVERSE);
*/
        fLeft.setPower(0);
        fRight.setPower(0);
        bLeft.setPower(0);
        bRight.setPower(0);
        //lift.setPower(0);
        clawServo.setPosition(0);


        fLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void driveForword(double mult, double speed) {
        bLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        bLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        bLeft.setTargetPosition((int) (1140 * mult));
        fLeft.setTargetPosition((int) (1140 * mult));
        bRight.setTargetPosition((int) (1140 * mult));
        fRight.setTargetPosition((int) (1140 * mult));


        while (bLeft.isBusy()) ;

    }

    public void driveBackword(double mult, double speed) {
        bLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        bLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        bLeft.setTargetPosition((int) (-1140 * mult));
        fLeft.setTargetPosition((int) (-1140 * mult));
        bRight.setTargetPosition((int) (-1140 * mult));
        fRight.setTargetPosition((int) (-1140 * mult));

        while (bLeft.isBusy()) ;

    }

    public void strafeLeft(double mult, double speed) {
        bLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        bLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        bLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        bRight.setTargetPosition((int) (-1140 * mult));
        fRight.setTargetPosition((int) (1140 * mult));
        bLeft.setTargetPosition((int) (1140 * mult));
        fLeft.setTargetPosition((int) (-1140 * mult));


        fLeft.setPower(speed);
        fRight.setPower(speed);
        bLeft.setPower(speed);
        bRight.setPower(speed);

        while (bLeft.isBusy()) ;
    }

    public void strafeRight(double mult, double speed) {
        bLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        bLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        bLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        bLeft.setTargetPosition((int) (-1140 * mult));
        fLeft.setTargetPosition((int) (1140 * mult));
        bRight.setTargetPosition((int) (1140 * mult));
        fRight.setTargetPosition((int) (-1140 * mult));

        fLeft.setPower(speed);
        fRight.setPower(speed);
        bLeft.setPower(speed);
        bRight.setPower(speed);

        while (bLeft.isBusy()) ;
    }

}



