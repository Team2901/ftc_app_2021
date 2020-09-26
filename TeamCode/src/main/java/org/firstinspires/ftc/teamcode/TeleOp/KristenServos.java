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

package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Disabled
@TeleOp(name="KristenServos V8 : Teleop Tank", group="Kristen")
public class KristenServos extends OpMode{
  public DcMotor leftDrive;
  public DcMotor rightDrive;
  public Servo leftGrabber;
  public Servo rightGrabber;
  public Servo centerGrabber;
  public static final double LEFT_GRABBER_MAX = 1.00;
  public static final double RIGHT_GRABBER_MIN = 0;
  public double leftGrabberOffset = LEFT_GRABBER_MAX;
  public double rightGrabberOffset = RIGHT_GRABBER_MIN;
  public double centerGrabberOffset = 0;
  public double GRABBER_SPEED = 0.01;
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        leftDrive .setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        leftGrabber = hardwareMap.get(Servo.class, "Left_grabber");
        rightGrabber = hardwareMap.get(Servo.class, "Right_grabber");
        centerGrabber = hardwareMap.get(Servo.class, "Center_grabber");

        leftGrabber.setPosition(LEFT_GRABBER_MAX);
        rightGrabber.setPosition(RIGHT_GRABBER_MIN);
        centerGrabber.setPosition(0);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double left;
        double right;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        left = -gamepad1.left_stick_y;
        right = -gamepad1.right_stick_y;


        leftDrive.setPower(left);
        rightDrive.setPower(right);



        if (gamepad1.right_bumper)
            rightGrabberOffset += GRABBER_SPEED;
        else if (gamepad1.right_trigger > 0)
            rightGrabberOffset -= GRABBER_SPEED;
        rightGrabberOffset = Range.clip(rightGrabberOffset, RIGHT_GRABBER_MIN, 0.6);
        rightGrabber.setPosition (rightGrabberOffset);

        // Send telemetry message to signify robot running;
       // telemetry.addData("left",  "%.2f", left);
        //telemetry.addData("right", "%.2f", right);
        telemetry.addData ("Right Grabber", "%.2f",rightGrabberOffset);


        if (gamepad1.left_bumper)
            leftGrabberOffset -= GRABBER_SPEED;
        else if (gamepad1.left_trigger > 0)
            leftGrabberOffset += GRABBER_SPEED;
        leftGrabberOffset = Range.clip(leftGrabberOffset, 0.4, LEFT_GRABBER_MAX);
        leftGrabber.setPosition (leftGrabberOffset);

        // Send telemetry message to signify robot running;
        telemetry.addData ("Left Grabber", "%.2f",leftGrabberOffset);

        if(gamepad1.a)
            centerGrabberOffset -= GRABBER_SPEED;
        else if (gamepad1.y)
            centerGrabberOffset += GRABBER_SPEED;
        centerGrabberOffset = Range.clip(centerGrabberOffset, 0 , 1 );
        centerGrabber.setPosition(centerGrabberOffset);

        telemetry.addData("Center Grabber", "%.2f", centerGrabberOffset);


    }


    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
