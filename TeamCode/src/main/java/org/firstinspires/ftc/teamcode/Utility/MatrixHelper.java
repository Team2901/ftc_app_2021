package org.firstinspires.ftc.teamcode.Utility;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities.INCHES_TO_MM;
import static org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities.MM_TO_INCHES;

@SuppressLint("DefaultLocale")
public class MatrixHelper {

    public static OpenGLMatrix buildMatrixMM(float xPositionMM,
                                             float yPositionMM,
                                             float zPositionMM,
                                             float xAngle,
                                             float yAngle,
                                             float zAngle) {
        return buildMatrix(xPositionMM, yPositionMM, zPositionMM,  xAngle, yAngle, zAngle, XYZ, 1);
    }

    public static OpenGLMatrix buildMatrixMM(float xPositionMM,
                                             float yPositionMM,
                                             float zPositionMM,
                                             float angle1,
                                             float angle2,
                                             float angle3,
                                             AxesOrder axesOrder) {
        return buildMatrix(xPositionMM, yPositionMM, zPositionMM,  angle1, angle2, angle3, axesOrder, 1);
    }

    public static OpenGLMatrix buildMatrixInches(float xPositionInches,
                                                 float yPositionInches,
                                                 float zPositionInches,
                                                 float xAngle,
                                                 float yAngle,
                                                 float zAngle) {
        return buildMatrix(xPositionInches, yPositionInches, zPositionInches, xAngle, yAngle, zAngle, XYZ, INCHES_TO_MM);
    }

    public static OpenGLMatrix buildMatrixInches(float xPositionInches,
                                                 float yPositionInches,
                                                 float zPositionInches,
                                                 float angle1,
                                                 float angle2,
                                                 float angle3,
                                                 AxesOrder axesOrder) {
        return  buildMatrix(xPositionInches, yPositionInches, zPositionInches, angle1, angle2, angle3, axesOrder, INCHES_TO_MM);
    }

    private static OpenGLMatrix buildMatrix(float xPositionInches,
                                            float yPositionInches,
                                            float zPositionInches,
                                            float angle1,
                                            float angle2,
                                            float angle3,
                                            AxesOrder axesOrder,
                                            float multiplier) {
        return OpenGLMatrix.translation(xPositionInches * multiplier, yPositionInches * multiplier,zPositionInches * multiplier).multiplied
                (Orientation.getRotationMatrix(EXTRINSIC, axesOrder, DEGREES, angle1, angle2, angle3));
    }

    public static Float getXPositionMM(final OpenGLMatrix openGLMatrix) {
        return getPosition(openGLMatrix, 0, 1);
    }

    public static Float getYPositionMM(final OpenGLMatrix openGLMatrix) {
        return getPosition(openGLMatrix, 1, 1);
    }

    public static Float getZPositionMM(final OpenGLMatrix openGLMatrix) {
        return getPosition(openGLMatrix, 2, 1);
    }

    public static Float getXPositionInches(final OpenGLMatrix openGLMatrix) {
        return getPosition(openGLMatrix, 0, MM_TO_INCHES);
    }

    public static Float getYPositionInches(final OpenGLMatrix openGLMatrix) {
        return getPosition(openGLMatrix, 1, MM_TO_INCHES);
    }

    public static Float getZPositionInches(final OpenGLMatrix openGLMatrix) {
        return getPosition(openGLMatrix, 2, MM_TO_INCHES);
    }

    public static Float getXAngle(final OpenGLMatrix openGLMatrix) {
        return getAngle(openGLMatrix, 0);
    }

    public static Float getYAngle(final OpenGLMatrix openGLMatrix) {
        return getAngle(openGLMatrix, 1);
    }

    public static Float getZAngle(final OpenGLMatrix openGLMatrix) {
        return getAngle(openGLMatrix, 2);
    }

    private static Float getPosition(OpenGLMatrix openGLMatrix, int index, float multiplier) {

        if (openGLMatrix != null) {
            return openGLMatrix.getTranslation().get(index) * multiplier;
        } else {
            return null;
        }
    }

    private static Float getAngle(OpenGLMatrix openGLMatrix, int index) {

        if (openGLMatrix != null) {
            Orientation orientation = Orientation.getOrientation(openGLMatrix, EXTRINSIC, XYZ, DEGREES);

            switch (index) {
                case 0:
                    return orientation.firstAngle;
                case 1:
                    return orientation.secondAngle;
                case 2:
                default:
                    return orientation.thirdAngle;
            }
        } else {
            return null;
        }
    }

    public static String getPositionsMMString(OpenGLMatrix openGLMatrix) {
        return getString(getXPositionMM(openGLMatrix), getYPositionMM(openGLMatrix), getZPositionMM(openGLMatrix));
    }

    public static String getInchesPositionString(OpenGLMatrix openGLMatrix) {
        return getString(getXPositionInches(openGLMatrix), getYPositionInches(openGLMatrix), getZPositionInches(openGLMatrix));
    }

    public static String getAngleString(OpenGLMatrix openGLMatrix) {
        return getString(getXAngle(openGLMatrix), getYAngle(openGLMatrix), getZAngle(openGLMatrix));
    }

    private static String getString(Float x, Float y, Float z) {

        if (x != null && y != null && z != null) {
            return String.format("(%.1f, %.1f, %.1f)", x, y, z);
        } else {
            return "N/A";
        }
    }
}
