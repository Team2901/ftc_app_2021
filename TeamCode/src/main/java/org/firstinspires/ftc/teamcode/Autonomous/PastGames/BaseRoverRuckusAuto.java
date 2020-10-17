package org.firstinspires.ftc.teamcode.Autonomous.PastGames;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;
import org.firstinspires.ftc.teamcode.Utility.AngleUtilities;
import org.firstinspires.ftc.teamcode.Utility.BitmapUtilities;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;
import org.firstinspires.ftc.teamcode.Utility.PolarCoord;
import org.firstinspires.ftc.teamcode.Utility.RoverRuckusUtilities;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

import static org.firstinspires.ftc.teamcode.Autonomous.PastGames.BaseRoverRuckusAuto.GoldPosition.MIDDLE;
import static org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities.getWebCameraParameters;

@SuppressLint("DefaultLocale")
public class BaseRoverRuckusAuto extends LinearOpMode {

    public enum StartCorner {
        RED_CRATER, RED_DEPOT, BLUE_CRATER, BLUE_DEPOT
    }

    public enum GoldPosition {
        LEFT, MIDDLE, RIGHT
    }

    public static final GoldPosition DEFAULT_GOLD_POSITION = MIDDLE;
    public static final boolean DEFAULT_IS_DROP_SUPPORTED = true;
    public static final boolean DEFAULT_USE_WEBCAM = true;
    public static final boolean DEFAULT_USE_VUFORIA_NAV = false;

    public static final int GO_TO_ANGLE_BUFFER = 9;
    public static final int GO_TO_POSITION_BUFFER = 2;
    public static final int TARGET_LIFT_TICKS = 5000;
    public static final double P_DRIVE_COEFF = 0.05;

    public final RoverRuckusBotHardware robot = new RoverRuckusBotHardware();

    public VuforiaLocalizer vuforia;
    VuforiaTrackables roverRuckus;

    public boolean writeFiles = false;

    public GoldPosition goldPosition;  // Default goldPosition to use if useWebCam = false
    public final boolean dropSupported;
    public final boolean useWebCam;
    public final boolean useVuforiaNav;

    public double orientation;
    public double tilt;

    public final StartCorner startCorner;
    public final PolarCoord dropPosition;
    public final PolarCoord startPosition;

    public String step;

    PolarCoord currentPosition;

    public BaseRoverRuckusAuto(StartCorner startCorner,
                               GoldPosition goldPosition,
                               boolean dropSupported,
                               boolean useWebCam,
                               boolean useVuforiaNav) {
        this.startCorner = startCorner;
        this.goldPosition = goldPosition;
        this.dropSupported = dropSupported;
        this.useWebCam = useWebCam;
        this.useVuforiaNav = useVuforiaNav;

        this.dropPosition = getDropPosition();
        this.startPosition = getStartPosition();
        this.currentPosition = dropPosition;
        robot.offset = dropPosition.theta;
    }

    public BaseRoverRuckusAuto(StartCorner startCorner) {
        this(startCorner,
                DEFAULT_GOLD_POSITION,
                DEFAULT_IS_DROP_SUPPORTED,
                DEFAULT_USE_WEBCAM,
                DEFAULT_USE_VUFORIA_NAV);
    }

    @Override
    public void waitForStart() {
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("startCorner", startCorner);
            telemetry.addData("goldPosition", goldPosition);
            telemetry.addData("dropSupported", dropSupported);
            telemetry.addData("useWebCam", useWebCam);
            telemetry.addData("useVuforiaNav", useVuforiaNav);
            telemetry.addData("writeFiles", writeFiles);
            telemetry.update();
        }
        composeTelemetry();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        updateStep("init");
        robot.init(hardwareMap);

        // Setup Vuforia (if using the camera)
        if (useWebCam) {
            setupVuforia();
        }

        updateStep("Wait for start");
        waitForStart();

        // Get gold position (if using the camera)
        if (useWebCam) {
            goldPosition = determineGoldPosition();
        }

        // Drop from lander (if supported)
        if (dropSupported) {
            dropFromLander();
        }

        robot.tiltOffset = -robot.getRawTilt();
    }

    public PolarCoord runOpModeDepotCorner() {

        /*setting the major autonomous position.  These positions are what we use as reference points
        and goals during autonomous.*/
        final PolarCoord preJewelPosition = getPreJewelPosition();
        final PolarCoord depotPosition = getDepotPosition();
        final PolarCoord postDepotPosition = getPostDepotPosition();
        final PolarCoord craterPosition = getCraterPosition();

        if (goldPosition != MIDDLE) {
            // Skip going to startPosition/preJewelPosition if jewel is in the middle
            currentPosition = goToPosition(currentPosition, startPosition, true);

            if (useVuforiaNav) {
                PolarCoord vuforiaCurrentPosition = getVuforiaCurrentPosition();
                if (vuforiaCurrentPosition != null) {
                    currentPosition = vuforiaCurrentPosition;
                }
            }

            currentPosition = goToPosition(currentPosition, preJewelPosition);
        }

        currentPosition = goToPosition(currentPosition, depotPosition);
        dropMarker();
        currentPosition = goToPosition(currentPosition, postDepotPosition);
        currentPosition = goToPosition(currentPosition, craterPosition);

        telemetry.addData(String.format("%10s", startPosition.name), formatMovement(dropPosition, startPosition));
        telemetry.addData(String.format("%10s", preJewelPosition.name), formatMovement(startPosition, preJewelPosition));
        telemetry.addData(String.format("%10s", depotPosition.name), formatMovement(preJewelPosition, depotPosition));
        telemetry.addData(String.format("%10s", postDepotPosition.name), formatMovement(depotPosition, postDepotPosition));
        telemetry.addData(String.format("%10s", craterPosition.name), formatMovement(postDepotPosition, craterPosition));
        telemetry.update();

        return currentPosition;
    }

    public PolarCoord runOpModeCraterCorner() {
        final PolarCoord preJewelPosition = getPreJewelPosition();
        final PolarCoord jewelPosition = getJewelPosition();
        final PolarCoord safePosition = getSafePosition();
        final PolarCoord preDepot = getPreDepotPosition();
        final PolarCoord depotPosition = getDepotPosition();
        final PolarCoord craterPosition = getCraterPosition();

        final double jewelToPreJewelDistance = -PolarCoord.getDistanceBetween(jewelPosition, preJewelPosition);

        if (goldPosition != MIDDLE) {
            // Skip going to startPosition if jewel is in the middle
            currentPosition = goToPosition(currentPosition, startPosition, true);
        }

        if (useVuforiaNav) {
            PolarCoord vuforiaCurrentPosition = getVuforiaCurrentPosition();
            if (vuforiaCurrentPosition != null) {
                currentPosition = vuforiaCurrentPosition;
            }
        }

        currentPosition = goToPosition(currentPosition, preJewelPosition);

        goToPosition(preJewelPosition, jewelPosition);

        goToDistance(jewelToPreJewelDistance, 1, preJewelPosition.name);

        currentPosition = goToPosition(currentPosition, safePosition);
        currentPosition = goToPosition(currentPosition, preDepot);

        goToPosition(preDepot, depotPosition);
        dropMarker();
        goToDistance(-PolarCoord.getDistanceBetween(depotPosition, preDepot), 1, preDepot.name);

        currentPosition = goToPosition(currentPosition, craterPosition);

        telemetry.addData(String.format("%10s", startPosition.name), formatMovement(dropPosition, startPosition));
        telemetry.addData(String.format("%10s", preJewelPosition.name), formatMovement(startPosition, preJewelPosition));
        telemetry.addData(String.format("%10s", jewelPosition.name), formatMovement(preJewelPosition, jewelPosition));
        telemetry.addData(String.format("%10s", safePosition.name), formatMovement(preJewelPosition, safePosition));
        telemetry.addData(String.format("%10s", preDepot.name), formatMovement(safePosition, preDepot));
        telemetry.addData(String.format("%10s", depotPosition.name), formatMovement(preDepot, depotPosition));
        telemetry.addData(String.format("%10s", craterPosition.name), formatMovement(preDepot, craterPosition));
        telemetry.update();

        return currentPosition;
    }

    public void setupVuforia() {
        if (robot.webcam != null) {
            updateStep("Setup vuforia");
            VuforiaLocalizer.Parameters parameters = getWebCameraParameters(hardwareMap, robot.webcam);
            vuforia = VuforiaUtilities.getVuforia(parameters);

            if (useVuforiaNav) {
                roverRuckus = RoverRuckusUtilities.setUpTrackables(vuforia, parameters);
            }
        }
    }

    public GoldPosition determineGoldPosition() {

        GoldPosition winner = goldPosition;

        if (!opModeIsActive()) {
            return winner;
        }

        updateStep("determineGoldPosition");

        final Bitmap bitmap = BitmapUtilities.getVuforiaImage(vuforia);

        if (bitmap == null) {
            telemetry.addData("Error reading bitmap", "");
            telemetry.update();
            return winner;
        }

        int[] leftHueTotal = {0, 0};
        int[] middleHueTotal = {0, 0};
        int[] rightHueTotal = {0, 0};

        try {
            leftHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap,"Left",this, writeFiles);
            middleHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap,"Middle", this, writeFiles);
            rightHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap,"Right",this, writeFiles);
        } catch (InterruptedException e) {
            telemetry.addData("Error getJewelHueCounts", e);
            telemetry.update();
            return winner;
        }

        winner = BitmapUtilities.findWinnerLocation(middleHueTotal, rightHueTotal);

        if (writeFiles) {
            try {
                FileUtilities.writeBitmapFile("jewelBitmap.png", bitmap);
                FileUtilities.writeHueFile("jewelHuesBig.txt", bitmap, this);
                FileUtilities.writeWinnerFile(winner, leftHueTotal, middleHueTotal, rightHueTotal);
            } catch (Exception e) {
                telemetry.addData("Error writing jewel files", e);
                telemetry.update();
            }
        }

        return winner;
    }

    public void dropFromLander() {
        if (opModeIsActive()) {
            updateStep("Drop From Lander");
            robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.lift.setTargetPosition(TARGET_LIFT_TICKS);
            robot.lift.setPower(1);
            while (robot.lift.isBusy()) {
                idle();
            }
        }
    }

    public PolarCoord getVuforiaCurrentPosition() {
        if (roverRuckus != null && opModeIsActive()) {
            updateStep("getVuforiaCurrentPosition");
            roverRuckus.activate();
            OpenGLMatrix location = RoverRuckusUtilities.getLocation(roverRuckus);
            roverRuckus.deactivate();
            if (location != null) {
                return new PolarCoord(location);
            }
        }

        return null;
    }

    public void dropMarker() {
        updateStep("drop marker");

        robot.marker.setPosition(robot.markerDropPosition);

        ElapsedTime timer = new ElapsedTime();
        while (timer.seconds() < 1 && opModeIsActive()) {
            idle();
        }
    }

    public PolarCoord goToPosition(PolarCoord startPosition, PolarCoord goalPosition) {
        return goToPosition(startPosition, goalPosition, false);
    }

    public PolarCoord goToPosition(PolarCoord startPosition, PolarCoord goalPosition, boolean override) {

        updateStep(String.format("GoToPosition: %s", goalPosition.name));

        double goalDistance = PolarCoord.getDistanceBetween(startPosition, goalPosition);
        double angleGoal = PolarCoord.getAngleBetween(startPosition, goalPosition);
        double angleStart = robot.getAngle();

        if (goalDistance > GO_TO_POSITION_BUFFER || override) {
            goToAngle(angleStart, angleGoal, goalPosition.name);
            goToDistance(goalDistance, 1, goalPosition.name);
            return goalPosition.withTheta(robot.getAngle());
        } else {
            telemetry.addData("Too close. Skipped turning and moving", "");
            telemetry.update();
            return startPosition.withTheta(robot.getAngle());
        }
    }

    public void goToAngle(double angleStart, double angleGoal, String name) {

        if (name != null) {
            updateStep(String.format("goToAngle: %s", name));
        }

        double angleCurrent = angleStart;

        while (Math.abs(angleGoal - angleCurrent) > GO_TO_ANGLE_BUFFER && !robot.isTiltedToRedCard() && opModeIsActive()) {
            angleCurrent = robot.getAngle();
            double power = getPower(angleCurrent, angleGoal, angleStart);
            robot.turn(-power);

            telemetry.addData("Start Angle ", "%.1f", angleStart);
            telemetry.addData("Goal Angle  ", "%.1f", angleGoal);
            telemetry.addData("Cur Angle   ", "%.1f", angleCurrent);
            telemetry.addData("Remain Angle", "%.1f", AngleUnit.normalizeDegrees(angleGoal - angleCurrent));
            telemetry.addData("Power       ", "%.2f", power);
            telemetry.update();
            idle();
        }

        robot.goStraight(0);
    }

    //TODO return the new position as a PolarCoord
    public void goToDistance(double distance,
                             double speed,
                             String name) {

        if (name != null) {
            updateStep(String.format("goToDistance: %s", name));
        }

        if (!opModeIsActive() || robot.isTiltedToRedCard()) {
            return;
        }

        double angle = robot.getAngle();
        speed = Range.clip(Math.abs(speed), 0.0, 1.0);

        robot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Determine new target position, and pass to motor controller
        int targetTicks = (int) (distance * robot.getInchesToEncoderCounts());

        // Set Target and Turn On RUN_TO_POSITION
        robot.left.setTargetPosition(targetTicks);
        robot.right.setTargetPosition(targetTicks);

        // start motion.
        robot.goStraight(speed);

        // keep looping while we are still active, and BOTH motors are running.
        while ((robot.left.isBusy() && robot.right.isBusy())
                && opModeIsActive() && !robot.isTiltedToRedCard()) {

            // adjust relative speed based on heading error.
            double error = AngleUtilities.getNormalizedAngle(angle - robot.getAngle());
            double steer = Range.clip(error * P_DRIVE_COEFF, -1, 1);

            // if driving in reverse, the motor correction also needs to be reversed
            if (distance < 0)
                steer *= -1.0;

            double leftSpeed = speed - steer;
            double rightSpeed = speed + steer;

            // Normalize speeds if either one exceeds +/- 1.0;
            double max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
            if (max > 1.0) {
                leftSpeed /= max;
                rightSpeed /= max;
            }

            // TODO turn PID control back on
            // robot.left.setPower(leftSpeed);
            // robot.right.setPower(rightSpeed);
            robot.goStraight(speed);

            // Display drive status for the driver.
            telemetry.addData("Error/Steer  ", "%5.1f/%5.1f", error, steer);
            telemetry.addData("Goal Distance", "%5.2f", distance);
            telemetry.addData("Goal    Ticks", "%7d:%7d", targetTicks, targetTicks);
            telemetry.addData("Current Ticks", "%7d:%7d", robot.left.getCurrentPosition(),
                    robot.right.getCurrentPosition());
            telemetry.addData("Speed", "%5.2f:%5.2f", robot.left.getPower(), robot.right.getPower());
            telemetry.update();
        }

        // Stop all motion;
        robot.goStraight(0);

        robot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double getPower(double absCurrent, double absGoal, double absStart) {
        double relCurrent = AngleUtilities.getNormalizedAngle(absCurrent - absStart);
        double relGoal = AngleUtilities.getNormalizedAngle(absGoal - absStart);
        double remainingDistance = AngleUtilities.getNormalizedAngle(relGoal - relCurrent);

        double basePower = 0.01 * remainingDistance;
        double stallPower = 0.1 * Math.signum(remainingDistance);
        return Range.clip(basePower + stallPower, -1, 1);
    }

    public void updateStep(String step) {
        this.step = step;
        telemetry.update();
    }

    public PolarCoord getDropPosition() {
        String name = "Drop";
        switch (startCorner) {
            case BLUE_DEPOT:
                return new PolarCoord(13, 13, 45, name);
            case BLUE_CRATER:
                return new PolarCoord(13, -13, -45, name);
            case RED_DEPOT:
                return new PolarCoord(-13, -13, -135, name);
            case RED_CRATER:
                return new PolarCoord(-13, 13, 135, name);
        }

        return new PolarCoord(0, 0, name);
    }

    public PolarCoord getStartPosition() {
        String name = "Start";
        switch (startCorner) {
            case BLUE_DEPOT:
                return new PolarCoord(18, 18, name);
            case BLUE_CRATER:
                return new PolarCoord(18, -18, name);
            case RED_DEPOT:
                return new PolarCoord(-18, -18, name);
            case RED_CRATER:
                return new PolarCoord(-18, 18, name);
        }
        return new PolarCoord(0, 0, name);
    }

    public PolarCoord getPreJewelPosition() {
        String name = "PreJewel";
        switch (this.startCorner) {
            case BLUE_DEPOT:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(8.02, 42.71, 13.80, name);
                    case MIDDLE:
                        return new PolarCoord(23.52, 23.52, 45.00, name);
                    case RIGHT:
                        return new PolarCoord(42.71, 8.02, 76.20, name);
                }
            case BLUE_CRATER:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(42.71, -8.02, -76.20, name);
                    case MIDDLE:
                        return new PolarCoord(23.52, -23.52, -45.00, name);
                    case RIGHT:
                        return new PolarCoord(8.02, -42.71, -13.80, name);
                }
            case RED_DEPOT:

                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(-8.02, -42.71, -166.20, name);
                    case MIDDLE:
                        return new PolarCoord(-23.52, -23.52, -135.00, name);
                    case RIGHT:
                        return new PolarCoord(-42.71, -8.02, -103.80, name);
                }
            case RED_CRATER:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(-42.71, 8.02, 103.80, name);
                    case MIDDLE:
                        return new PolarCoord(-23.52, 23.52, 135.00, name);
                    case RIGHT:
                        return new PolarCoord(-8.02, 42.71, 166.20, name);
                }
        }
        return new PolarCoord(0, 0, 0, name);
    }

    public PolarCoord getJewelPosition() {
        String name = "Jewel";
        switch (this.startCorner) {
            case BLUE_DEPOT:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(27.0, 48.5, name);
                    case MIDDLE:
                        return new PolarCoord(37.75, 37.75, name);
                    case RIGHT:
                        return new PolarCoord(48.5, 27.0, name);
                }
            case BLUE_CRATER:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(48.5, -27.0, name);
                    case MIDDLE:
                        return new PolarCoord(37.75, -37.75, name);
                    case RIGHT:
                        return new PolarCoord(27.0, -48.5, name);
                }
            case RED_DEPOT:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(-27.0, -48.5, name);
                    case MIDDLE:
                        return new PolarCoord(-37.75, -37.75, name);
                    case RIGHT:
                        return new PolarCoord(-48.5, -27.0, name);
                }
            case RED_CRATER:
                switch (goldPosition) {
                    case LEFT:
                        return new PolarCoord(-48.5, 27.0, name);
                    case MIDDLE:
                        return new PolarCoord(-37.75, 37.75, name);
                    case RIGHT:
                        return new PolarCoord(-27.0, 48.5, name);
                }
        }
        return new PolarCoord(0, 0, name);
    }

    public PolarCoord getDepotPosition() {
        String name = "Depot";
        switch (this.startCorner) {
            case BLUE_DEPOT:
                return new PolarCoord(50, 54, name);
            case BLUE_CRATER:
                switch (goldPosition) {
                    case RIGHT:
                        return new PolarCoord(62, 54, name);
                    case MIDDLE:
                        return new PolarCoord(58, 54, name);
                    case LEFT:
                        return new PolarCoord(58, 54, name);
                }
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-54, -54, name);
        }
        return new PolarCoord(0, 0, name);
    }

    public PolarCoord getCraterPosition() {
        String name = "Crater";
        switch (this.startCorner) {
            case BLUE_DEPOT:
            case BLUE_CRATER:
                return new PolarCoord(60, -28, name);
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-60, 28, name);
        }
        return new PolarCoord(0, 0, name);
    }

    public PolarCoord getSafePosition() {
        String name = "Safe";
        switch (this.startCorner) {
            case BLUE_DEPOT:
            case BLUE_CRATER:
                switch (goldPosition) {
                    case RIGHT:
                        return new PolarCoord(62, 0, name);
                    case MIDDLE:
                    case LEFT:
                        return new PolarCoord(58, 0, name);
                }
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-60, 24, name);
        }
        return new PolarCoord(0, 0, name);
    }

    public PolarCoord getPreDepotPosition() {
        String name = "PreDepot";
        switch (this.startCorner) {
            case BLUE_DEPOT:
            case BLUE_CRATER:
                switch (goldPosition) {
                    case RIGHT:
                        return new PolarCoord(62, 36, name);
                    case MIDDLE:
                    case LEFT:
                        return new PolarCoord(58, 36, name);
                }
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-60, 36, name);
        }
        return new PolarCoord(0, 0, name);
    }

    public PolarCoord getPostDepotPosition() {
        String name = "PostDepot";
        switch (startCorner) {
            case BLUE_DEPOT:
            case BLUE_CRATER:
                return new PolarCoord(61, 36, name);
            case RED_DEPOT:
            case RED_CRATER:
                return new PolarCoord(-61, -36, name);
        }
        return new PolarCoord(0, 0, name);
    }

    public String formatMovement(PolarCoord startPosition, PolarCoord endPosition) {
        double xDiff = endPosition.x - startPosition.x;
        double yDiff = endPosition.y - startPosition.y;

        double angleGoal = Math.atan2(yDiff, xDiff) * (180 / Math.PI);
        double distanceToGoal = Math.sqrt((Math.pow(yDiff, 2) + Math.pow(xDiff, 2)));

        return String.format("(%.1f, %.1f)   %.0f     %.1f",
                endPosition.x, endPosition.y, angleGoal, distanceToGoal);
    }

    public void composeTelemetry() {
        telemetry.addAction(new Runnable() {
            @Override
            public void run() {
                orientation = robot.getAngle();
                tilt = robot.getTilt();
            }
        });
        telemetry.addLine()
                .addData("Orientation:", new Func<String>() {
                    @Override
                    public String value() {
                        return String.valueOf(orientation);
                    }
                });
        telemetry.addLine()
                .addData("Tilt:", new Func<String>() {
                    @Override
                    public String value() {
                        return String.valueOf(tilt);
                    }
                });
        telemetry.addLine()
                .addData("Current Position:", new Func<String>() {
                    @Override
                    public String value() {
                        return String.valueOf(currentPosition);
                    }
                });
        telemetry.addLine()
                .addData("Step:", new Func<String>() {
                    @Override
                    public String value() {
                        return String.valueOf(step);
                    }
                });
        telemetry.addLine()
                .addData("Gold:", new Func<String>() {
                    @Override
                    public String value() {
                        return String.valueOf(goldPosition);
                    }
                });
    }
}