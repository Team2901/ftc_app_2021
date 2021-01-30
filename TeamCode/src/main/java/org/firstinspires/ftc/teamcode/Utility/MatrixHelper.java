package org.firstinspires.ftc.teamcode.Utility;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities.INCHES_TO_MM;
import static org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities.MM_TO_INCHES;

@SuppressLint("DefaultLocale")
public class MatrixHelper {

    /**
     * Builds a Matrix with the given translation (in MM) and orientation (in Degrees)
     *
     * @param xPositionMM x translations (in MM)
     * @param yPositionMM y translation (in MM)
     * @param zPositionMM z translation (in MM)
     * @param axesOrder   order of axes to rotate around
     * @param angle1      first angle rotation
     * @param angle2      second angle rotation
     * @param angle3      third angle rotation
     * @return Matrix with the given translation (in MM) and orientation (in Degrees)
     * @see OpenGLMatrix#translation(float, float, float)
     * @see Orientation#getRotationMatrix(AxesReference, AxesOrder, AngleUnit, float, float, float)
     */
    public static OpenGLMatrix buildMatrixMM(float xPositionMM, float yPositionMM, float zPositionMM,
                                             AxesOrder axesOrder,
                                             float angle1, float angle2, float angle3) {
        // TODO implement
        return null;
    }

    /**
     * Builds a Matrix with the given translation (in MM) and orientation (in Degrees)
     *
     * @param xPositionInches x translations (in inches)
     * @param yPositionInches y translation (in inches)
     * @param zPositionInches z translation (in inches)
     * @param axesOrder       order of axes to rotate around (ex XYZ to rotate around x first, y second, and z third)
     * @param angle1          first angle rotation
     * @param angle2          second angle rotation
     * @param angle3          third angle rotation
     * @return Matrix with the given translation (in MM) and orientation (in Degrees)
     * @see OpenGLMatrix#translation(float, float, float)
     * @see Orientation#getRotationMatrix(AxesReference, AxesOrder, AngleUnit, float, float, float)
     */
    public static OpenGLMatrix buildMatrixInches(float xPositionInches, float yPositionInches, float zPositionInches,
                                                 AxesOrder axesOrder,
                                                 float angle1, float angle2, float angle3) {
        // TODO implement
        return null;
    }

    /**
     * Gets the X position (in MM) for the given Matrix, else null if the matrix is null
     *
     * @param openGLMatrix Matrix to get position from
     * @return X position (in MM) for the given matrix
     * @see OpenGLMatrix#getTranslation()
     */
    public static Float getXPositionMM(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }

    /**
     * Gets the Y position (in MM) for the given Matrix, else null if the matrix is null
     *
     * @param openGLMatrix Matrix to get position from
     * @return Y position (in MM) for the given matrix
     * @see OpenGLMatrix#getTranslation()
     */
    public static Float getYPositionMM(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }

    /**
     * Gets the Z position (in MM) for the given Matrix, else null if the matrix is null
     *
     * @param openGLMatrix Matrix to get position from
     * @return Z position (in MM) for the given matrix
     * @see OpenGLMatrix#getTranslation()
     */
    public static Float getZPositionMM(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }

    /**
     * Gets the X position (in inches) for the given Matrix, else null if the matrix is null
     *
     * @param openGLMatrix Matrix to get position from
     * @return X position (in inches)
     * @see OpenGLMatrix#getTranslation()
     */
    public static Float getXPositionInches(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }

    /**
     * Gets the Y position (in inches) for the given Matrix, else null if the matrix is null
     *
     * @param openGLMatrix Matrix to get position from
     * @return Y position (in inches)
     * @see OpenGLMatrix#getTranslation()
     */
    public static Float getYPositionInches(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }

    /**
     * Gets the Z position (in inches) for the given Matrix, else null if the matrix is null
     *
     * @param openGLMatrix Matrix to get position from
     * @return Z position (in inches)
     * @see OpenGLMatrix#getTranslation()
     */
    public static Float getZPositionInches(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }

    /**
     * Gets the X angle (in degrees) for the given Matrix, else null if the matrix is null
     *
     * @param openGLMatrix Matrix to get angle from
     * @return X angle (in degrees)
     * @see Orientation#getOrientation
     */
    public static Float getXAngle(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }

    /**
     * Gets the Y angle (in degrees) for the given Matrix, else null if the matrix is null
     *
     * @param openGLMatrix Matrix to get angle from
     * @return Y angle (in degrees)
     * @see Orientation#getOrientation
     */
    public static Float getYAngle(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }

    /**
     * Gets the Y angle (in degrees) for the given Matrix, else null if the matrix is null
     *
     * @param openGLMatrix Matrix to get angle from
     * @return Y angle (in degrees)
     * @see Orientation#getOrientation
     */
    public static Float getZAngle(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }

    /**
     * Builds a formatted string of the x,y,z positions (in MM) that rounds to the nearest tenth (ie one decimal place), else "N/A" if the matrix is null
     *
     * @param openGLMatrix Matrix to build position string for
     * @return formatting string of the x,y,z positions (in MM)
     */
    public static String getPositionsMMString(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }

    /**
     * Builds a formatted string of the x,y,z positions (in inches) that rounds to the nearest tenth (ie one decimal place), else "N/A" if the matrix is null
     *
     * @param openGLMatrix Matrix to build position string for
     * @return formatting string of the x,y,z positions (in inches)
     */
    public static String getInchesPositionString(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }

    /**
     * Builds a formatted string of the x,y,z angles (in degrees) that rounds to the nearest tenth (ie one decimal place), else "N/A" if the matrix is null
     *
     * @param openGLMatrix Matrix to build angle string for
     * @return formatting string of the x,y,z angles (in degrees)
     */
    public static String getAngleString(final OpenGLMatrix openGLMatrix) {
        // TODO implement
        return null;
    }
}
