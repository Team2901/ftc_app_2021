package org.firstinspires.ftc.teamcode.Utility;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.vuforia.Image;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto;

import java.io.IOException;
import java.util.List;

public class BitmapUtilities {

    public static final int PIXEL_FORMAT_RGB565 = 1;

    public static BaseRoverRuckusAuto.GoldPosition findWinnerLocation(int[] leftHueTotal,
                                                                      int[] middleHueTotal,
                                                                      int[] rightHueTotal) {
        BaseRoverRuckusAuto.GoldPosition winner = BaseRoverRuckusAuto.GoldPosition.MIDDLE;
        int leftColor, middleColor, rightColor;
        if(leftHueTotal[0]/*yellow counts*/ > leftHueTotal[1]/*white counts*/)
        {
            leftColor = Color.YELLOW;
            winner = BaseRoverRuckusAuto.GoldPosition.LEFT;
        }
        else {
            leftColor= Color.WHITE;
        }

        if(middleHueTotal[0]/*yellow counts*/ > middleHueTotal[1]/*white counts*/)
        {
            middleColor = Color.YELLOW;
            winner = BaseRoverRuckusAuto.GoldPosition.MIDDLE;
        }
        else {
            middleColor= Color.WHITE;
        }
        if(rightHueTotal[0]/*yellow counts*/ > rightHueTotal[1]/*white counts*/)
        {
            rightColor = Color.YELLOW;
            winner = BaseRoverRuckusAuto.GoldPosition.RIGHT;
        }
        else {
            rightColor= Color.WHITE;
        }

        return winner;
    }

    public static BaseRoverRuckusAuto.GoldPosition findWinnerLocation(int[] middleHueTotal,
                                                                      int[] rightHueTotal) {
        BaseRoverRuckusAuto.GoldPosition winner = BaseRoverRuckusAuto.GoldPosition.LEFT;
        int middleColor, rightColor;


        if(middleHueTotal[0]/*yellow counts*/ > middleHueTotal[1]/*white counts*/)
        {
            middleColor = Color.YELLOW;
            winner = BaseRoverRuckusAuto.GoldPosition.MIDDLE;
        }
        else {
            middleColor= Color.WHITE;
        }
        if(rightHueTotal[0]/*yellow counts*/ > rightHueTotal[1]/*white counts*/)
        {
            rightColor = Color.YELLOW;
            winner = BaseRoverRuckusAuto.GoldPosition.RIGHT;
        }
        else {
            rightColor= Color.WHITE;
        }
        return winner;
    }

    public static BaseRoverRuckusAuto.GoldPosition findCorrectGoldLocation (int[] middleHueTotal,
                                                                      int[] rightHueTotal) {
        BaseRoverRuckusAuto.GoldPosition winner = BaseRoverRuckusAuto.GoldPosition.LEFT;
        int middleColor, rightColor;


        if(middleHueTotal[0]/*yellow counts*/ > 0)
        {
            middleColor = Color.YELLOW;
            winner = BaseRoverRuckusAuto.GoldPosition.MIDDLE;
        }
        else {
            middleColor= Color.WHITE;
        }
        if(rightHueTotal[0]/*yellow counts*/ > 0)
        {
            rightColor = Color.YELLOW;
            winner = BaseRoverRuckusAuto.GoldPosition.RIGHT;
        }
        else {
            rightColor= Color.WHITE;
        }
        return winner;
    }

    BaseRoverRuckusAuto.GoldPosition findGoldPosition(int leftHueTotal,
                                                      int middleHueTotal,
                                                      int rightHueTotal) {
        //TODO
        return null;
    }

    BaseRoverRuckusAuto.GoldPosition findGoldPosition(int middleHueTotal,
                                                      int rightHueTotal) {
        //TODO
        return null;
    }

    public static Bitmap getBabyBitmap(Bitmap bitmap,List<Integer> config) {
        return getBabyBitmap(bitmap, config.get(0), config.get(1), config.get(2), config.get(3));
    }

    public static Bitmap getBabyBitmap(Bitmap bitmap,
                                       int sampleLeftXPct, int sampleTopYPct,
                                       int sampleRightXPct, int sampleBotYPct) {
        int startXPx = (int) ((sampleLeftXPct / 100.0) * bitmap.getWidth());
        int startYPx = (int) ((sampleTopYPct / 100.0) * bitmap.getHeight());
        int endXPx = (int) ((sampleRightXPct / 100.0) * bitmap.getWidth());
        int endYPx = (int) ((sampleBotYPct / 100.0) * bitmap.getHeight());
        int width = endXPx - startXPx;
        int height = endYPx - startYPx;

        return Bitmap.createBitmap(bitmap, startXPx, startYPx, width, height);
    }


    public static Bitmap drawSamplingBox(String filename, Bitmap bitmap,
                                         int sampleLeftXPct, int sampleTopYPct,
                                         int sampleRightXPct, int sampleBotYPct) throws IOException {
        double xPercent = (bitmap.getWidth()) / 100.0;
        double yPercent = (bitmap.getHeight()) / 100.0;
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(mutableBitmap);
        Paint p = new Paint();
        p.setARGB(100, 0, 200, 0);
        c.drawRect((int) (sampleLeftXPct * xPercent),
                (int) (sampleTopYPct * yPercent),
                (int) (sampleRightXPct * xPercent),
                (int) (sampleBotYPct * yPercent), p);
        //writeBitmapFile(filename, mutableBitmap);
        return mutableBitmap;
    }

    public static Bitmap getVuforiaImage(VuforiaLocalizer vuforia) {
        if (vuforia != null) {
            try {
                VuforiaLocalizer.CloseableFrame closeableFrame = vuforia.getFrameQueue().take();
                for (int i = 0; i < closeableFrame.getNumImages(); i++) {
                    Image image = closeableFrame.getImage(i);
                    if (image.getFormat() == PIXEL_FORMAT_RGB565) {
                        Bitmap bitmap = Bitmap.createBitmap(image.getWidth(),
                                image.getHeight(),
                                Bitmap.Config.RGB_565);
                        bitmap.copyPixelsFromBuffer(image.getPixels());
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);

                        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    }
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
