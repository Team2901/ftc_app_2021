/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Autonomous.BaseUltimateGoalAuto;
import org.firstinspires.ftc.teamcode.Hardware.ProgrammingUltimateGoalHardware;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name = "Programming Ultimate Goal Auto", group = "Linear Opmode")
public class ProgrammingUltimateGoalAuto extends BaseUltimateGoalAuto {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    public static final double TICKS_PER_MOTOR_REV = 1140;
    public static final double FORWARD_DRIVE_GEAR_RATIO = 2;
    public static final double CENTER_DRIVE_GEAR_RATIO = 3;
    public static final double FORWARD_TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * FORWARD_DRIVE_GEAR_RATIO;
    public static final double CENTER_TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * CENTER_DRIVE_GEAR_RATIO;
    public static final double WHEEL_CIRCUMFERENCE_INCHES = 4 * Math.PI;
    public static final double FORWARD_TICKS_PER_INCH = FORWARD_TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE_INCHES;
    public static final double CENTER_TICKS_PER_INCH = CENTER_TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE_INCHES;

    public int starterStackResult = -1;
    public void moveInchesCenter(double inches){
        int ticks = (int) (inches * CENTER_TICKS_PER_INCH);

        robot.middleMotor.setTargetPosition(robot.middleMotor.getCurrentPosition() + ticks);

        robot.middleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.middleMotor.setPower(1);

        while (opModeIsActive() && (robot.middleMotor.isBusy())){
            telemetry.addData("Current Middle Position", robot.middleMotor.getCurrentPosition());
            telemetry.update();
        }
        robot.middleMotor.setPower(0);

        robot.middleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public void moveInchesForward(double inches) {
        int ticks = (int) (inches * FORWARD_TICKS_PER_INCH);

        robot.leftMotor.setTargetPosition(robot.leftMotor.getCurrentPosition() + ticks);
        robot.rightMotor.setTargetPosition(robot.rightMotor.getCurrentPosition() + ticks);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.leftMotor.setPower(.75);
        robot.rightMotor.setPower(.75);

        while (opModeIsActive() && (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {
            telemetry.addData("stackID", starterStackResult);
            telemetry.addData("Current Left Position", robot.leftMotor.getCurrentPosition());
            telemetry.addData("Current Right Position", robot.rightMotor.getCurrentPosition());
            telemetry.update();
        }

        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public String target = "C";


    public void goToA() {
        moveInchesCenter(12);
        moveInchesForward(75);
        releaseWobble();
    }
    public void goToB() {
        moveInchesCenter(12);
        moveInchesForward(99);
        moveInchesCenter(-36);
        releaseWobble();
    }
    public void goToC() {
        moveInchesCenter(12);
        moveInchesForward(123);
        releaseWobble();
    }

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        init(true);

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.middleMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //moveInches();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        //grabWobble();

        /*
        if(target == "A") {
            goToA();
        } else if (target == "B") {
            goToB();
        } else if (target == "C") {
            goToC();
        } else {
            telemetry.addData("error", "Invalid Target");
            telemetry.update();
        }
         */

        moveInchesCenter(-12);

        starterStackResult = starterStackSensor();

        /*while (opModeIsActive() && !gamepad1.a){

        }*/

        moveInchesCenter(12);

        if (starterStackResult == 0){
            goToA();
        } else if (starterStackResult == 1) {
            goToB();
        } else if (starterStackResult == 2) {
            goToC();
        } else {
            telemetry.addData("error", "How did this happen");
            telemetry.update();
        }


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
        }
    }
}

