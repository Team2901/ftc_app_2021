package org.firstinspires.ftc.teamcode.BaseSampleCode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kearneyg20428 on 2/7/2017.
 */

public class SlideDrive {


    public DcMotor leftFrontMotor = null;
    public DcMotor leftBackMotor = null;

    public DcMotor rightFrontMotor = null;
    public DcMotor rightBackMotor = null;

    public DcMotor centerMotor = null;

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
        centerMotor = hwMap.dcMotor.get("centerMotor");


        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        centerMotor.setDirection(DcMotor.Direction.FORWARD);


        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightBackMotor.setPower(0);
        centerMotor.setPower(0);


    }
}