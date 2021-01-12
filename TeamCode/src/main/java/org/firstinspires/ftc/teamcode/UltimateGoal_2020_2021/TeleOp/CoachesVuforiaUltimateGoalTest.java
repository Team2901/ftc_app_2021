package org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.TeleOp;

import android.annotation.SuppressLint;

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
import org.firstinspires.ftc.teamcode.UltimateGoal_2020_2021.Hardware.BaseUltimateGoalHardware;
import org.firstinspires.ftc.teamcode.Utility.RobotFactory;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YXZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

@SuppressLint("DefaultLocale")
@TeleOp(name = "Coaches Vuforia UltimateGoal Test", group = "2021_UltimateGoal")
public class CoachesVuforiaUltimateGoalTest extends OpMode {
    private static final float mmPerInch = 25.4f;
    private static final double RATIO_INC = .1;

    public final BaseUltimateGoalHardware robot = (BaseUltimateGoalHardware) RobotFactory.create(this.telemetry);

    VuforiaTrackables targetsUltimateGoal;

    double movePowerRatio = .3;
    double turnPowerRatio = .3;

    @Override
    public void init() {
        robot.init(this.hardwareMap);

        String errorMessage = robot.initWebCamera(this.hardwareMap);

        if(errorMessage != null) {
            telemetry.addData("init error", errorMessage);
            //robot.webCamera.errorMessage = null;
            //robot.initPhoneCamera(this.hardwareMap);
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

        double robotAngle = robot.getAngle();

        VuforiaTrackable vuforiaTrackable = getVisibleTrackable();
        VuforiaTrackableDefaultListener listener = vuforiaTrackable != null ? ((VuforiaTrackableDefaultListener) vuforiaTrackable.getListener()) : null;
        OpenGLMatrix robotLocation = listener != null ? listener.getRobotLocation() : null;
        OpenGLMatrix imageLocation = vuforiaTrackable != null ? vuforiaTrackable.getLocation() : null;

        matrixTelemetry(robotLocation, imageLocation);


        if (gamepad1.b) {
            extraTelemetery(robotLocation, imageLocation);
        }
        if (gamepad1.a) {
            targetsUltimateGoal.deactivate();
        }
        if (gamepad1.y) {
            targetsUltimateGoal.activate();
        }

        float rightStickX = gamepad1.right_stick_x;
        float rightStickY = -1 * gamepad1.right_stick_y;
        float leftStickX = gamepad1.left_stick_x;
        float leftStickY = -1 * gamepad1.left_stick_y;
        double rightStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(rightStickY, rightStickX));
        double leftStickAngle = AngleUnit.DEGREES.fromRadians(Math.atan2(leftStickY, leftStickX));
        double rightStickRadius = Math.hypot(rightStickX, rightStickY);
        double leftStickRadius = Math.hypot(leftStickX, leftStickY);

        double leftMotorPower = 0;
        double rightMotorPower = 0;
        double middleMotorPower = 0;

        if (gamepad1.left_bumper) {
            turnPowerRatio = Math.max(0, turnPowerRatio - RATIO_INC);
        } else if (gamepad1.right_bumper) {
            turnPowerRatio = Math.min(1, turnPowerRatio + RATIO_INC);
        }

        if (gamepad1.left_trigger > .1) {
            movePowerRatio = Math.max(0, movePowerRatio - RATIO_INC);
        } else if (gamepad1.right_trigger > .1) {
            movePowerRatio = Math.min(1, movePowerRatio + RATIO_INC);
        }

        if (leftStickRadius > .1) {

            double xToMoveTo = Math.cos(Math.toRadians(leftStickAngle-90));
            double yToMoveTo = Math.sin(Math.toRadians(leftStickAngle-90));
            // Step 4: Calculate forwards/sideways powers to move at
            leftMotorPower = leftStickRadius * xToMoveTo * movePowerRatio;
            rightMotorPower = leftStickRadius * xToMoveTo * movePowerRatio;
            middleMotorPower = leftStickRadius * yToMoveTo * movePowerRatio;

            telemetry.addData("x To Move To", xToMoveTo);
            telemetry.addData("y To Move To", yToMoveTo);
            telemetry.addData("Angle To Move To", leftStickAngle);
        }

        if (rightStickRadius > .1) {
            leftMotorPower = -turnPowerRatio * rightStickX;
            rightMotorPower = turnPowerRatio * rightStickX;
        }

        // Find where the trackers are.

        robot.leftMotor.setPower(leftMotorPower);
        robot.rightMotor.setPower(rightMotorPower);
        robot.middleMotor.setPower(middleMotorPower);

        telemetry.addData("Robot angle", robotAngle);
        telemetry.addData("Right Stick Angle", rightStickAngle);
        telemetry.addData("Left Stick Angle", leftStickAngle);
        telemetry.addData("Right Motor Power", rightMotorPower);
        telemetry.addData("Left Motor Power", leftMotorPower);
        telemetry.addData("Middle Motor Power", middleMotorPower);
        telemetry.addData("Turn Power Ratio", turnPowerRatio);
        telemetry.addData("Move Power Ratio", movePowerRatio);

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

    void matrixTelemetry(OpenGLMatrix robotLocation, OpenGLMatrix imageLocation) {

        telemetry.addData("Visible", robotLocation != null);

        if (robotLocation != null) {

            VectorF translation = robotLocation.getTranslation();

            telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f", translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);
            Orientation rotation = Orientation.getOrientation(robotLocation, EXTRINSIC, XYZ, DEGREES);
            telemetry.addData("Rot (deg)", "{X, Y, Z} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);

        } else {

            telemetry.addData("Pos (in)", "N/A");
            telemetry.addData("Rot (deg)", "N/A");
        }
    }

    void extraTelemetery(OpenGLMatrix robotLocation, OpenGLMatrix imageLocation) {

        if (robotLocation != null && imageLocation != null) {

            VectorF robotTranslation = robotLocation.getTranslation();

            float robotX = robotTranslation.get(0);
            float robotY = robotTranslation.get(1);

            VectorF imageTranslation = imageLocation.getTranslation();

            float imageX = imageTranslation.get(0);
            float imageY = imageTranslation.get(1);

            float dX = robotX - imageX;
            float dY = robotY - imageY;

            Orientation rot = Orientation.getOrientation(robotLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

            double targetRange = Math.hypot(dX, dY);

            // target bearing is based on angle formed between the X axis to the target range line
            double targetBearing = Math.toDegrees(Math.atan(dY/dX));
            float bearing = rot.thirdAngle;

            // Target relative bearing is the target Heading relative to the direction the robot is pointing.

            telemetry.addData("robotBearing", bearing);
            telemetry.addData("targetBearing", targetBearing);
            //telemetry.addData("targetRange", targetRange);

            telemetry.addData("robot {X, Y}", "%.0f, %.0f", robotX, robotY);
            telemetry.addData("image {X, Y}", "%.0f, %.0f", imageX, imageY);
            telemetry.addData("delta {X, Y}", "%.0f, %.0f", dX, dY);
        }
    }


}
