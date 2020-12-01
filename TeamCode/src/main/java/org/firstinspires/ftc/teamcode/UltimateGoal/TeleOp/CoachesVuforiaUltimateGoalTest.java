package org.firstinspires.ftc.teamcode.UltimateGoal.TeleOp;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.TankUltimateGoalHardware;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YXZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Coaches Vuforia UltimateGoal Test", group = "2021_UltimateGoal")
public class CoachesVuforiaUltimateGoalTest extends OpMode {
    public TankUltimateGoalHardware robot = new TankUltimateGoalHardware();
    private static final float mmPerInch = 25.4f;

    VuforiaTrackables targetsUltimateGoal;

    VuforiaTrackable vuforiaTrackable;

    @Override
    public void init() {
        robot.init(this.hardwareMap);

        String errorMessage = robot.initWebCamera(this.hardwareMap);

        if(errorMessage != null) {
            robot.webCamera.errorMessage = null;
            robot.initPhoneCamera(this.hardwareMap);
        }

        targetsUltimateGoal = loadAndPlaceTrackables();

        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(0, 0, 0)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YXZ, DEGREES, 90, 90, 0));

        for (VuforiaTrackable trackable : targetsUltimateGoal) {
            VuforiaTrackableDefaultListener listener = ((VuforiaTrackableDefaultListener) trackable.getListener());
            listener.setCameraLocationOnRobot(robot.webCamera.parameters.cameraName, robotFromCamera);
        }

        targetsUltimateGoal.activate();

        vuforiaTrackable = getVisibleTrackable();

        if (vuforiaTrackable != null) {
            VuforiaTrackableDefaultListener listener = ((VuforiaTrackableDefaultListener) vuforiaTrackable.getListener());
            OpenGLMatrix robotLocation = listener.getRobotLocation();
            VectorF robotTranslation = robotLocation.getTranslation();
            Orientation rot = Orientation.getOrientation(robotLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

            float robotAngle = robot.getAngle();

            float bearing = rot.thirdAngle;

            robot.offset = (bearing - robotAngle);

            telemetry.addData("robotAngle", robotAngle);
            telemetry.addData("bearing", bearing);
            telemetry.addData("offset", robot.offset);
        }

        telemetry.update();
    }

    public VuforiaTrackable getVisibleTrackable() {

        for (VuforiaTrackable trackable : targetsUltimateGoal) {

            if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {

                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getRobotLocation();
                if (robotLocationTransform != null) {
                    return trackable;
                }
            }
        }

        return null;
    }

    @Override
    public void loop() {

        if (vuforiaTrackable != null) {
            telemetry.addData("Visible Target", vuforiaTrackable.getName());
            VuforiaTrackableDefaultListener listener = ((VuforiaTrackableDefaultListener) vuforiaTrackable.getListener());
            OpenGLMatrix robotLocation = listener.getRobotLocation();
            matrixTelemetry("Visible Target", robotLocation);
        } else {
            telemetry.addData("Visible Target", "NONE");
        }

        if (gamepad1.a) {
            vuforiaTrackable = getVisibleTrackable();

            if (vuforiaTrackable != null) {
                VuforiaTrackableDefaultListener listener = ((VuforiaTrackableDefaultListener) vuforiaTrackable.getListener());
                OpenGLMatrix robotLocation = listener.getRobotLocation();
                VectorF robotTranslation = robotLocation.getTranslation();
                Orientation rot = Orientation.getOrientation(robotLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                float robotAngle = robot.getAngle();

                float bearing = rot.thirdAngle;

                robot.offset = (bearing - robotAngle);

                telemetry.addData("robotAngle", robotAngle);
                telemetry.addData("bearing", bearing);
                telemetry.addData("offset", robot.offset);
            }
        }

      // else if (gamepad1.b) {
            if (vuforiaTrackable != null) {

                VuforiaTrackableDefaultListener listener = ((VuforiaTrackableDefaultListener) vuforiaTrackable.getListener());

                OpenGLMatrix robotLocation = listener.getRobotLocation();
                OpenGLMatrix imageLocation = vuforiaTrackable.getLocation();

                VectorF robotTranslation = robotLocation.getTranslation();

                float robotX = robotTranslation.get(0);
                float robotY = robotTranslation.get(1);


                VectorF imageTranslation = imageLocation.getTranslation();

                float imageX = imageTranslation.get(0);
                float imageY = imageTranslation.get(1);

                float dX = robotX - imageX;
                float dY = robotY - imageY;

                Orientation rot = Orientation.getOrientation(robotLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                double robotAngle = robot.getAngle();

                double targetRange = Math.hypot(dX, dY);

                // target bearing is based on angle formed between the X axis to the target range line
                double targetBearing = Math.toDegrees(-Math.asin(dY / targetRange));
                float bearing = rot.thirdAngle;

                // Target relative bearing is the target Heading relative to the direction the robot is pointing.

                double angle = Math.toDegrees(Math.atan2(dY, dX));

                telemetry.addData("robotAngle", robotAngle);
                telemetry.addData("bearing", bearing);
                telemetry.addData("offset", robot.offset);
                telemetry.addData("targetRange", targetRange);
                telemetry.addData("targetBearing", targetBearing);

                telemetry.addData("robotX", robotX);
                telemetry.addData("robotY", robotY);
                telemetry.addData("imageX", imageX);
                telemetry.addData("imageY", imageY);

                telemetry.addData("dX", dX);
                telemetry.addData("dY", dY);
                telemetry.addData("angle", angle);
            }
      //  }


        // Find where the trackers are.

        telemetry.update();
    }

    public VuforiaTrackables loadAndPlaceTrackables() {

       robot.webCamera.loadVuforiaTrackables("UltimateGoal");
        VuforiaTrackables targetsUltimateGoal = robot.webCamera.vuforiaTrackables;

        float mmTargetHeight = 0; // (6) * mmPerInch;
        float halfField = 0;// 72 * mmPerInch;
        float quadField = 0;//36 * mmPerInch;

        VuforiaTrackable blueTowerGoalTarget = targetsUltimateGoal.get(0);
        blueTowerGoalTarget.setName("Blue Tower Goal Target");
        blueTowerGoalTarget.setLocation(OpenGLMatrix
                .translation(halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        VuforiaTrackable redTowerGoalTarget = targetsUltimateGoal.get(1);
        redTowerGoalTarget.setName("Red Tower Goal Target");
        redTowerGoalTarget.setLocation(OpenGLMatrix
                .translation(halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        VuforiaTrackable redAllianceTarget = targetsUltimateGoal.get(2);
        redAllianceTarget.setName("Red Alliance Target");
        redAllianceTarget.setLocation(OpenGLMatrix
                .translation(0, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));


        VuforiaTrackable blueAllianceTarget = targetsUltimateGoal.get(3);
        blueAllianceTarget.setName("Blue Alliance Target");
        blueAllianceTarget.setLocation(OpenGLMatrix
                .translation(0, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        VuforiaTrackable frontWallTarget = targetsUltimateGoal.get(4);
        frontWallTarget.setName("Front Wall Target");
        frontWallTarget.setLocation(OpenGLMatrix
                .translation(-halfField, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

        return targetsUltimateGoal;
    }

    void matrixTelemetry(String name, OpenGLMatrix matrix) {
        VectorF translation = matrix.getTranslation();
        telemetry.addData("Name", name);
        telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f", translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);
        Orientation rotation = Orientation.getOrientation(matrix, EXTRINSIC, XYZ, DEGREES);
        telemetry.addData("Rot (deg)", "{X, Y, Z} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
    }

}
