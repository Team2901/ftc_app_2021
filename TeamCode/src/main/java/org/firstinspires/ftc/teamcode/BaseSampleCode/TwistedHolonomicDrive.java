package org.firstinspires.ftc.teamcode.BaseSampleCode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kearneyg20428 on 2/7/2017.
 */

public class TwistedHolonomicDrive {


    public DcMotor leftFrontMotor = null;
    public DcMotor leftBackMotor = null;

    public DcMotor rightFrontMotor = null;
    public DcMotor rightBackMotor = null;



    private ElapsedTime period = new ElapsedTime();
    private HardwareMap hwMap = null;

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftFrontMotor = hwMap.dcMotor.get("leftFrontMotor");
        leftBackMotor = hwMap.dcMotor.get("leftBackMotor");
        rightFrontMotor = hwMap.dcMotor.get("rightFrontMotor");
        rightBackMotor = hwMap.dcMotor.get("rightBackMotor");


        leftFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        leftBackMotor.setDirection(DcMotor.Direction.FORWARD);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);


        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightBackMotor.setPower(0);


    }
}