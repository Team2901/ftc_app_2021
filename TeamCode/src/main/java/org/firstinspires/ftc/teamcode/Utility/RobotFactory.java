package org.firstinspires.ftc.teamcode.Utility;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class RobotFactory {

    /**
     * Gets the contexts of an activity without calling from an Activity class
     *
     * @return the main Application (as a Context)
     */
    private static Context getContext() {
        try {
            final Class<?> activityThreadClass =
                    Class.forName("android.app.ActivityThread");
            //find and load the main activity method
            final Method method = activityThreadClass.getMethod("currentApplication");
            return (Application) method.invoke(null, (Object[]) null);
        } catch (final java.lang.Throwable e) {
            // handle exception
            throw new IllegalArgumentException("No context could be retrieved!");
        }
    }
    private static String getRobotConfigurationName() {
        //String key = context.getString(R.string.pref_hardware_config_filename);
        Context context = RobotFactory.getContext();
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        String objSerialized = preferences.getString("pref_hardware_config_filename", null);
        String configName = null;
        try {
            JSONObject jObject = new JSONObject(objSerialized);
            configName = jObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return configName;
    }
    private static Object _createRobotInstance(Telemetry telemetry) throws Exception {
        Object robot = null;
        String packageName = "org.firstinspires.ftc.teamcode.UltimateGoal.Hardware";
        String hardwareClassName = RobotFactory.getRobotConfigurationName();
        String className = packageName + "." + hardwareClassName;
        try {
            robot = Class.forName(className).getConstructor().newInstance();
        } catch (Exception e) {
            robot = Class.forName("org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.BaseUltimateGoalHardware").getConstructor().newInstance();
            telemetry.addData("Unable to create hardware class: ", hardwareClassName );
            telemetry.update();
        }
        return robot;
    }
    public static Object create(Telemetry telemetry) {
        Object robot = null;
        try {
            robot = _createRobotInstance(telemetry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return robot;
    }
}
