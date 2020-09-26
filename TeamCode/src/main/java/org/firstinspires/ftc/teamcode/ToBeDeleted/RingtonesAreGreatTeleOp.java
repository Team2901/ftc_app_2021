package org.firstinspires.ftc.teamcode.ToBeDeleted;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Disabled
@TeleOp(name = "RingtonesAreGreatTeleOp")

public class RingtonesAreGreatTeleOp extends OpMode {
    Ringtone r;
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        Uri notification;
        if(gamepad1.y){
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }else if (gamepad1.x){
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
        }else if (gamepad1.b){
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }else if (gamepad1.a && r != null){
            notification = null;
            r.stop();
        }else {
            notification = null;
        }
        if (notification != null) {
            if (r != null){
                r.stop();
            }
            r = RingtoneManager.getRingtone(hardwareMap.appContext, notification);
            r.play();
        }
    }
}
