package org.firstinspires.ftc.teamcode.Utility;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.io.IOException;
import java.util.List;


public class RoverRuckusUtilities {

    public static final String JEWEL_CONFIG_FILE_FORMAT = "jewelConfig%s.txt";
    public static final String JEWEL_BITMAP_FILE_FORMAT = "jewelBitmap%s.png";
    public static final String JEWEL_BITMAP_BW_FILE_FORMAT = "jewelBitmapBW%s.png";
    public static final String JEWEL_HUE_FILE_FORMAT = "jewelHues%s.txt";

    public static final float MM_TO_INCHES = 0.0393701f;
    public static final float INCHES_TO_MM = 25.4f;
    public static final double FIELD_RADIUS = 1828.8;

    public static OpenGLMatrix phoneLocation = getMatrix(90, -0, -90,
            (int) (-6 * INCHES_TO_MM), (int) (1 * INCHES_TO_MM), (int) (18 * INCHES_TO_MM));


    public static int[] getJewelHueCount(Bitmap bitmap,
                                         String name,
                                         LinearOpMode opMode) throws RuntimeException, InterruptedException {
        return getJewelHueCount(bitmap, name, opMode, true);
    }

    public static int[] getJewelHueCount(Bitmap bitmap,
                                         String name,
                                         LinearOpMode opMode,
                                         boolean writeFiles) throws RuntimeException, InterruptedException {
        try {
                final String configFilename = String.format(JEWEL_CONFIG_FILE_FORMAT, name);
                final List<Integer> config = FileUtilities.readIntegerConfigFile(configFilename);
                final Bitmap babyBitmap = BitmapUtilities.getBabyBitmap(bitmap, config);

                if (writeFiles) {
                    final Bitmap babyBitmapBW = ColorUtilities.blackWhiteColorDecider(babyBitmap, 25, 40, opMode);

                final String bitmapFilename = String.format(JEWEL_BITMAP_FILE_FORMAT, name);
                final String hueFilename = String.format(JEWEL_HUE_FILE_FORMAT, name);
                final String bwFileName = String.format(JEWEL_BITMAP_BW_FILE_FORMAT, name);

                FileUtilities.writeBitmapFile(bitmapFilename, babyBitmap);
                FileUtilities.writeHueFile(hueFilename, babyBitmap, opMode);
                FileUtilities.writeBitmapFile(bwFileName, babyBitmapBW);
            }

            return ColorUtilities.getColorCount(babyBitmap, 25, 55, opMode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static VuforiaTrackables setUpTrackables(VuforiaLocalizer vuforia,
                                                    VuforiaLocalizer.Parameters parameters) {

        VuforiaTrackables roverRuckus = vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blue = roverRuckus.get(0);
        VuforiaTrackable red = roverRuckus.get(1);
        VuforiaTrackable front = roverRuckus.get(2);
        VuforiaTrackable back = roverRuckus.get(3);

        blue.setName("blue");
        red.setName("red");
        front.setName("front");
        back.setName("back");

        OpenGLMatrix blueTrackablePosition = getMatrix(90, 0, -90, (float) FIELD_RADIUS, 0, (float) 152.4);
        OpenGLMatrix frontTrackablePosition = getMatrix(90, 0, 0, 0, (float) FIELD_RADIUS, (float) 152.4);
        OpenGLMatrix redTrackablePosition = getMatrix(90, 0, 90, (float) -FIELD_RADIUS, 0, (float) 152.4);
        OpenGLMatrix backTrackablePosition = getMatrix(90, 0, 180, 0, (float) -FIELD_RADIUS, (float) 152.4);

        blue.setLocation(blueTrackablePosition);
        red.setLocation(redTrackablePosition);
        front.setLocation(frontTrackablePosition);
        back.setLocation(backTrackablePosition);

        ((VuforiaTrackableDefaultListener) blue.getListener()).setPhoneInformation(phoneLocation, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener) red.getListener()).setPhoneInformation(phoneLocation, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener) front.getListener()).setPhoneInformation(phoneLocation, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener) back.getListener()).setPhoneInformation(phoneLocation, parameters.cameraDirection);

        return roverRuckus;
    }

    public static OpenGLMatrix getLocation(VuforiaTrackables roverRuckus) {
        return getLocation(roverRuckus.get(0), roverRuckus.get(1), roverRuckus.get(2), roverRuckus.get(3));
    }

    public static OpenGLMatrix getLocation(VuforiaTrackable blue, VuforiaTrackable red,
                                           VuforiaTrackable front, VuforiaTrackable back) {
        OpenGLMatrix location = null;
        OpenGLMatrix blueLocation = ((VuforiaTrackableDefaultListener)
                blue.getListener()).getUpdatedRobotLocation();
        OpenGLMatrix redLocation = ((VuforiaTrackableDefaultListener)
                red.getListener()).getUpdatedRobotLocation();
        OpenGLMatrix backLocation = ((VuforiaTrackableDefaultListener)
                back.getListener()).getUpdatedRobotLocation();
        OpenGLMatrix frontLocation = ((VuforiaTrackableDefaultListener)
                front.getListener()).getUpdatedRobotLocation();

        if (blueLocation != null) {
            location = blueLocation;
        } else if (redLocation != null) {
            location = redLocation;
        } else if (backLocation != null) {
            location = backLocation;
        } else if (frontLocation != null) {
            location = frontLocation;
        }

        return location;
    }

    public static OpenGLMatrix getMatrix(float ax, float ay, float az, float dx, float dy, float dz) {

        return OpenGLMatrix.translation(dx, dy, dz).multiplied
                (Orientation.getRotationMatrix(AxesReference.EXTRINSIC,
                        AxesOrder.XYZ, AngleUnit.DEGREES, ax, ay, az));
    }
}
