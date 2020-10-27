package org.firstinspires.ftc.teamcode.UltimateGoal.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.teamcode.UltimateGoal.Hardware.TankUltimateGoalHardware;

@TeleOp(name = "Vuforia UltimateGoal Test", group = "2021_UltimateGoal")
public class VuforiaUltimateGoalTest extends OpMode {
    public TankUltimateGoalHardware robot = new TankUltimateGoalHardware();
    @Override
    public void init() {
        robot.init(this.hardwareMap);
        robot.initWebCamera(this.hardwareMap);
        robot.webCamera.loadVuforiaTrackables("UltimateGoal");
        VuforiaTrackable vuforiaBlueTower = robot.webCamera.vuforiaTrackables.get(0);

        //This is used for telemetry purposes.
        vuforiaBlueTower.setName("Blue Tower");

        //This props up the blue tower, it is currently in the middle of the field.
        OpenGLMatrix blueTowerLocation = OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90,0,-90);
        vuforiaBlueTower.setLocation(blueTowerLocation);
    }

    @Override
    public void loop() {

    }
}
