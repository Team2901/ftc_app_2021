package org.firstinspires.ftc.teamcode.SkyStone.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.SkyStone.Hardware.BaseSkyStoneHardware;
import org.firstinspires.ftc.teamcode.SkyStone.Hardware.CompetitionSkystoneHardware;

@Disabled
@TeleOp(name = "Servo Swerve Offset Setter Hard", group = "TEST")
public class SweveServoOffsetSetterHardStop extends OpMode {

    CompetitionSkystoneHardware robot = new CompetitionSkystoneHardware();
    ImprovedGamepad impGamepad;
    ImprovedGamepad impGamepad2;
    ElapsedTime timer = new ElapsedTime();

    Servo servoUnderTest;
    BaseSkyStoneHardware.SwerveWheel swerveWheelUnderTest;

    int servoIndex;

    boolean mode = false;

    @Override
    public void init() {
        impGamepad = new ImprovedGamepad(this.gamepad1, this.timer, "GP1");
        impGamepad2 = new ImprovedGamepad(this.gamepad2, this.timer, "GP2");

        robot.init(hardwareMap);

        robot.swerveStraight(0,0);

        telemetry.update();
    }

    @Override
    public void loop() {
        impGamepad.update();
        impGamepad2.update();

        if (this.impGamepad2.a.isPressed()) {
            robot.swerveStraight(0,0);
        }

        if(this.impGamepad.dpad_right.isInitialPress()){
            servoIndex++;
            if(servoIndex >= 4){
                servoIndex = 0;
            }
        } else if(this.impGamepad.dpad_left.isInitialPress()) {
            servoIndex--;
            if(servoIndex < 0){
                servoIndex = robot.swerveWheels.length - 1;
            }
        }

        if (impGamepad.start.isInitialPress()) {
            mode = !mode;
        }

        servoUnderTest = robot.swerveWheels[servoIndex].servo;
        swerveWheelUnderTest = robot.swerveWheels[servoIndex];

        if(servoUnderTest != null){

            if(this.impGamepad.left_bumper.isInitialPress()){
                servoUnderTest.setPosition(servoUnderTest.getPosition()-0.1);
            } else if(this.impGamepad.right_bumper.isInitialPress()){
                servoUnderTest.setPosition(servoUnderTest.getPosition()+0.1);
            } else if(this.impGamepad.left_trigger.isInitialPress()){
                servoUnderTest.setPosition(servoUnderTest.getPosition()-0.01);
            } else if(this.impGamepad.right_trigger.isInitialPress()) {
                servoUnderTest.setPosition(servoUnderTest.getPosition()+0.01);
            }

            if (this.impGamepad.a.isInitialPress()) {
                swerveWheelUnderTest.setOffset(servoUnderTest.getPosition());
            }

            double relativePosition = servoUnderTest.getPosition() - swerveWheelUnderTest.offset;

            if (this.impGamepad.x.isInitialPress()) {
                swerveWheelUnderTest.hardMinWheelPositionRelToZero = relativePosition;
                swerveWheelUnderTest.setOffset(swerveWheelUnderTest.offset);

            }

            if (this.impGamepad.y.isInitialPress()) {
                swerveWheelUnderTest.hardMaxWheelPositionRelToZero = relativePosition;
                swerveWheelUnderTest.setOffset(swerveWheelUnderTest.offset);
            }

            double targetVoltage = 3.124;
            double targetVoltageFL = 2.899;
            double targetVoltageFR = 3.336;
            double targetVoltageBL = 0.671;
            double targetVoltageRL = 1.264;

            if (this.impGamepad.b.isPressed()) {

                if (swerveWheelUnderTest.potentiometer.getVoltage() < swerveWheelUnderTest.potentiometerTarget) {
                    servoUnderTest.setPosition(servoUnderTest.getPosition()+0.01);
                } else if (swerveWheelUnderTest.potentiometer.getVoltage() > swerveWheelUnderTest.potentiometerTarget) {
                    servoUnderTest.setPosition(servoUnderTest.getPosition()-0.01);
                }
            }

            telemetry.addData("Left/Right bumper","-/+0.1");
            telemetry.addData("Left/Right trigger","-/+0.01");
            telemetry.addData("Servo name",swerveWheelUnderTest.name);
            telemetry.addData("Position", servoUnderTest.getPosition());
            telemetry.addData("Rel Pos", relativePosition);
            telemetry.addData("Offset (a)", swerveWheelUnderTest.offset);
            telemetry.addData("hardMinPos (x)", swerveWheelUnderTest.hardMinWheelPositionRelToZero);
            telemetry.addData("hardMaxPos (y)", swerveWheelUnderTest.hardMaxWheelPositionRelToZero);
            telemetry.addData("hardMinAngle", swerveWheelUnderTest.hardMinWheelAngle);
            telemetry.addData("hardMaxAngle", swerveWheelUnderTest.hardMaxWheelAngle);
            telemetry.addData("targetVoltage", swerveWheelUnderTest.potentiometerTarget);

            if (swerveWheelUnderTest.potentiometer != null) {
                telemetry.addData("potentiometer", swerveWheelUnderTest.potentiometer.getVoltage());

                telemetry.addData("potentiometerFL", robot.swerveWheels[0].potentiometer.getVoltage());
                telemetry.addData("potentiometerFR", robot.swerveWheels[1].potentiometer.getVoltage());
                telemetry.addData("potentiometerBL", robot.swerveWheels[2].potentiometer.getVoltage());
                telemetry.addData("potentiometerBR", robot.swerveWheels[3].potentiometer.getVoltage());

            }
        }

        telemetry.addData("D Pad Right/Left", "Increment/decrement servos");

        telemetry.update();

    }

    @Override
    public void stop() {

        super.stop();

        String errorMsg = robot.writeServoOffsets();
        if (errorMsg != null) {
            throw new RuntimeException(errorMsg);
        }

        errorMsg = robot.writeServoHardMinMaxValues();

        if (errorMsg != null) {
            throw new RuntimeException(errorMsg);
        }
    }
}
