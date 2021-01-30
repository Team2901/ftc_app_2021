package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "Distance Sensor Test", group = "Shared Test")
public class TestDistanceSensor extends OpMode {

    DistanceSensor distanceSensor;

    @Override
    public void init() { distanceSensor = hardwareMap.get(DistanceSensor.class, "sensor_distance"); }

    @Override
    public void loop() {
        telemetry.addData("Distance",  "%.01f mm", distanceSensor.getDistance(DistanceUnit.MM));
        telemetry.update();
    }
}
