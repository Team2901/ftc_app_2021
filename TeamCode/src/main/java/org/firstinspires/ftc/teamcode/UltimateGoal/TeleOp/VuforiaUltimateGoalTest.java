package org.firstinspires.ftc.teamcode.UltimateGoal.TeleOp;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.GrantHardware;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.TankUltimateGoalHardware;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Vuforia UltimateGoal Test", group = "2021_UltimateGoal")
public class VuforiaUltimateGoalTest extends OpMode {
    public GrantHardware robot = new GrantHardware();
    @Override
    public void init() {
        robot.init(this.hardwareMap);

        int cameraRotationX = 0;
        String errorMessage = robot.initWebCamera(this.hardwareMap);

        if(errorMessage != null) {
            robot.webCamera.errorMessage = null;
            cameraRotationX = 90;
            robot.initPhoneCamera(this.hardwareMap);
        }

        // Loading the Vuforia trackables.
        robot.webCamera.loadVuforiaTrackables("UltimateGoal");

        // Saves blue tower trackable.
        VuforiaTrackable vuforiaBlueTower = robot.webCamera.vuforiaTrackables.get(0);

        // This is used for telemetry purposes for identifying that the camera is seeing the blue tower trackable.
        vuforiaBlueTower.setName("Blue Tower");

        // This props up the blue tower, it is currently in the middle of the field.
        OpenGLMatrix blueTowerLocation = OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90,0,-90);
        vuforiaBlueTower.setLocation(blueTowerLocation);

        OpenGLMatrix blueTowerLocationTwo = OpenGLMatrix.translation(0, 0, 0).multiplied
                (Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES,
                        90,0, -90));
        vuforiaBlueTower.setLocation(blueTowerLocationTwo);

        // Saves red tower trackable.
        VuforiaTrackable vuforiaRedTower = robot.webCamera.vuforiaTrackables.get(1);

        // This is used for telemetry purposes for identifying that the camera is seeing the red tower trackable.
        vuforiaRedTower.setName("Red Tower");

        // This props up the red tower, it is currently in the middle of the field.
        OpenGLMatrix redTowerLocation = OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90,0,-90);
        vuforiaRedTower.setLocation(redTowerLocation);

        OpenGLMatrix redTowerLocationTwo = OpenGLMatrix.translation(0, 0, 0).multiplied(
                Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES,
                        90,0, -90));
        vuforiaRedTower.setLocation(redTowerLocationTwo);

        // Sets up the position of the Vuforia web image and web camera.

        OpenGLMatrix webcamLocation = OpenGLMatrix.translation(0, 0, 0).multiplied(
                Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZY, AngleUnit.DEGREES,
                        90, 90, 0));

        // We are telling the blue tower image where the camera is on the robot.
        VuforiaTrackable.Listener webcamListenerBlue = vuforiaBlueTower.getListener();
        VuforiaTrackableDefaultListener webcamDefaultListenerBlue = (VuforiaTrackableDefaultListener) webcamListenerBlue;
        webcamDefaultListenerBlue.setCameraLocationOnRobot(robot.webCamera.parameters.cameraName, webcamLocation);

        // We are telling the red tower image where the camera is on the robot.
        VuforiaTrackable.Listener webcamListenerRed = vuforiaRedTower.getListener();
        VuforiaTrackableDefaultListener webcamDefaultListenerRed = (VuforiaTrackableDefaultListener) webcamListenerRed;
        webcamDefaultListenerRed.setCameraLocationOnRobot(robot.webCamera.parameters.cameraName, webcamLocation);

        // We are ready to use the trackables and this gives us the opportunity to deactivate them later.
        robot.webCamera.vuforiaTrackables.activate();
    }

    @Override
    public void loop() {
        OpenGLMatrix robotLocation = null;

        // Check if trackers are visible.
        for(int i = 0; i < robot.webCamera.vuforiaTrackables.size(); i++) {
            // Store current element in variable.
            VuforiaTrackable currentTrackable = robot.webCamera.vuforiaTrackables.get(i);

            // Gets listener before checking if current tracker is visible.
            VuforiaTrackableDefaultListener currentTrackableDefaultListener = (VuforiaTrackableDefaultListener) currentTrackable.getListener();

            // Determines whether current trackable is visible.
            boolean isTrackableVisible = currentTrackableDefaultListener.isVisible();

            // Prints out whether the current trackable is visible.
            telemetry.addData(currentTrackable.getName() + " is visible", isTrackableVisible);

            if(isTrackableVisible){
                // Gets the robot's location.
                robotLocation = currentTrackableDefaultListener.getRobotLocation();
            }
        }

        double relativeFieldAngle;

        /*
        * If the robot location is not null, we translate the robot's location. In other words,
        * if the tracker image is visible, we translate the robot's location.
         */
        if(robotLocation != null){
            // This gets what this trackable thinks that the robot's position is.
            VectorF robotLocationTranslation = robotLocation.getTranslation();
            float x = robotLocationTranslation.get(0);
            float y = robotLocationTranslation.get(1);
            float z = robotLocationTranslation.get(2);

            // This gets what this trackable thinks that the robot's orientation is.
            Orientation orientation = Orientation.getOrientation(robotLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
            float xAngle = orientation.firstAngle;
            float yAngle = orientation.secondAngle;
            float zAngle = orientation.thirdAngle;

            // x, y, z positions (x, y, z)
            telemetry.addData("x, y, z positions", "(" + x + ", " + y +", " + z + ")");

            // x, y, z rotations (x, y, z)
            telemetry.addData("x, y, z rotations", String.format("(%f, %f, %f)", xAngle, yAngle, zAngle));

            // Calculate the angle relative to the robot and print it out.
            double relativeRobotAngle = Math.toDegrees(Math.atan(y/x));
            telemetry.addData("Angle relative to the robot", relativeRobotAngle);

            // Calculate angle relative to the field.
            relativeFieldAngle = relativeRobotAngle + robot.getAngle();

            // Print angle relative to the robot and the angle relative to the field.
            telemetry.addData("Angle relative to the field", relativeFieldAngle);

            // Only make the robot turn relative to the field if a on the first gamepad is pressed.
            if(gamepad1.a){
                // Determine the speed that the motors should be set to.
                double velocity = robot.getMotorTurnSpeed(relativeFieldAngle, robot.getAngle());

                // Make the robot only turn at 50% speed.
                velocity *= 0.5;

                // Make the robot turn counterclockwise.
                robot.leftMotor.setPower(-velocity);
                robot.rightMotor.setPower(velocity);
            }
            else
            {
                // Make the robot stop.
                robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(0);
            }

        }
        // When pressing the left bumper, the robot will turn counterclockwise.
        else if(gamepad1.left_bumper)
        {
            robot.leftMotor.setPower(-0.5);
            robot.rightMotor.setPower(0.5);
        }
        // When pressing the right bumper, the robot will turn clockwise.
        else if(gamepad1.right_bumper)
        {
            robot.leftMotor.setPower(0.5);
            robot.rightMotor.setPower(-0.5);
        }
        else
        {
            // We want to face the tower goal, so set the angle to 0.
            relativeFieldAngle = 0;

            // Sets right and left motor powers to zero.
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);
        }
        // Find where the trackers are.
        telemetry.update();
    }
}
