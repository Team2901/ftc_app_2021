package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.TeleOp;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.BaseUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;
import org.firstinspires.ftc.teamcode.Utility.MatrixHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Initialize the vuforia camera
 * Use auto aim
 * Find the x and y distance to image
 * Use pythagorean theorem to find distance to image
 * Adjust speed according
 */

@SuppressLint("DefaultLocale")
@TeleOp(name = "Speed Alteration", group = "2021_UltimateGoal")
public class SpeedAlterationUlimateGoalTeleOp extends VuforiaUltimateGoalTest {
    public BaseUltimateGoalHardware robot = BaseUltimateGoalHardware.create();

    public static final float MM_TO_INCHES = 0.0393701f;
    public static final double MAX_TURN_POWER = 0.5;

    List<String> logMessages = new ArrayList<>();
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime timestampTimer = new ElapsedTime();

    ImprovedGamepad improvedGamepad;

    @Override
    public void init() {
        robot.init(this.hardwareMap);

        improvedGamepad = new ImprovedGamepad(gamepad1, timestampTimer, "g1");

        int cameraRotationX = 0;
        String errorMessage = robot.initWebCamera(this.hardwareMap);

        if(errorMessage != null) {
            robot.webCamera.errorMessage = null;
            cameraRotationX = 90;
            robot.initPhoneCamera(this.hardwareMap);
        }

        // Loading the Vuforia trackables.
        robot.webCamera.loadVuforiaTrackables("UltimateGoal");

        // Sets up the position of the Vuforia web image and web camera.
        OpenGLMatrix webcamLocation = OpenGLMatrix.translation(-6/MM_TO_INCHES, -0.5f/MM_TO_INCHES, 0).multiplied(
                Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZY, AngleUnit.DEGREES,
                        90, 90, 0));

        // Sets up vuforia trackables.
        robot.webCamera.ultimateGoalSetupTrackables(webcamLocation);

        // We are ready to use the trackables and this gives us the opportunity to deactivate them later.
        robot.webCamera.vuforiaTrackables.activate();
    }

    @Override
    public void loop() {
        double leftMotorPower = 0;
        double rightMotorPower = 0;

        // Returns robot location and prints whether a trackable is visible or not.
        OpenGLMatrix robotLocation = robot.webCamera.getRobotLocation();
        boolean isVisible = robotLocation != null;
        telemetry.addData("Is visible", isVisible);

        telemetry.addData("robot position", MatrixHelper.getInchesPositionString(robotLocation));
        telemetry.addData("robot angle", MatrixHelper.getAngleString(robotLocation));

        // Gets the x and y position of the robot.
        Float xPositionInches = MatrixHelper.getXPositionInches(robotLocation);
        Float yPositionInches = MatrixHelper.getYPositionInches(robotLocation);

        if(xPositionInches != null && yPositionInches != null) {
            // Uses pythagorean theorem to find the distance to the image.
            double distanceToImage = Math.sqrt(Math.pow(xPositionInches, 2) + Math.pow(yPositionInches, 2));

            telemetry.addData("X position", xPositionInches);
            telemetry.addData("Y Position", yPositionInches);
            telemetry.addData("Distance to Image", distanceToImage);

            // Alters shooter power speed based on the distance from the image.
            if(distanceToImage >= 132){
                robot.shooterMotor.setPower(1);
            }else if(distanceToImage >= 108){
                robot.shooterMotor.setPower(0.9);
            }else if(distanceToImage >= 84){
                robot.shooterMotor.setPower(0.8);
            }else{
                robot.shooterMotor.setPower(0.7);
            }
            telemetry.addData("Shooter power", robot.shooterMotor.getPower());
        }

        // Gets the z angle of the robot's location.
        Float zAngle = MatrixHelper.getZAngle(robotLocation);

        double angleDifference = 0;
        double velocity = 0;

        Double targetFieldAngle = robot.webCamera.getRobotTurnAngle(robotLocation, false);

        /*
         * If the robot location is not null, we translate the robot's location. In other words,
         * if the tracker image is visible, we translate the robot's location.
         */
        if(isVisible){
            // Print angle relative to the robot and the angle relative to the field.
            telemetry.addData("Angle relative to the field", "%.1f", targetFieldAngle);

            // Determine the angle difference between targetFieldAngle and the robot's angle.
            angleDifference = AngleUnit.normalizeDegrees(targetFieldAngle - zAngle);
            telemetry.addData("Angle difference", "%.1f", angleDifference);

            // Determine the speed that the motors should be set to.
            velocity = robot.getMotorTurnSpeed(targetFieldAngle, zAngle);
            telemetry.addData("Velocity", "%.1f", velocity);

            // Only make the robot turn relative to the field if a on the first gamepad is pressed.
            if(gamepad1.a){
                // Make the robot turn counterclockwise.
                leftMotorPower = -velocity * MAX_TURN_POWER;
                rightMotorPower = velocity * MAX_TURN_POWER;
            }
        }

        // When pressing the left bumper, the robot will turn counterclockwise.
        if(gamepad1.left_bumper)
        {
            leftMotorPower = -MAX_TURN_POWER;
            rightMotorPower = MAX_TURN_POWER;
        }
        // When pressing the right bumper, the robot will turn clockwise.
        else if(gamepad1.right_bumper)
        {
            leftMotorPower = MAX_TURN_POWER;
            rightMotorPower = -MAX_TURN_POWER;
        }

        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);

        // Find where the trackers are.

        if (timer.milliseconds() >= 1) {
             /*
             Log information every millisecond
             1) timestamp
             2) robot angle
             3) visible?
             4) angleDiff
             5) velocity
             6) x diff
             7) y diff
             */
            String msg = String.format("%f, %f, %b, %f, %f, %f, %f", timestampTimer.milliseconds(),
                    zAngle, isVisible, angleDifference, velocity, MatrixHelper.getXPositionInches(robotLocation),
                    MatrixHelper.getYPositionInches(robotLocation));

            logMessages.add(msg);

            timer.reset();
        }

        if (improvedGamepad.x.isInitialPress()) {

            int time = (int)(System.currentTimeMillis());

            try {
                FileUtilities.writeConfigFile("vuforiaLogFile_" + time + "_.csv", logMessages);
                logMessages.clear();
            } catch (IOException e) {
                telemetry.addData("Error writing to file", e.getMessage());
            }
        }

        telemetry.update();
    }
}
