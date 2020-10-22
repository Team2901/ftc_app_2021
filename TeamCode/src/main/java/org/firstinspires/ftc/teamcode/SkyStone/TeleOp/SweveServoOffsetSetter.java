package org.firstinspires.ftc.teamcode.SkyStone.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.SkyStone.Hardware.CompetitionSkystoneHardware;

@Disabled
@TeleOp(name = "Servo Swerve Offset Setter", group = "TEST")
public class SweveServoOffsetSetter extends OpMode {

    CompetitionSkystoneHardware robot = new CompetitionSkystoneHardware();
    ImprovedGamepad impGamepad;
    ElapsedTime timer = new ElapsedTime();

    Servo servoUnderTest;

    int servoIndex;

    @Override
    public void init() {
        impGamepad = new ImprovedGamepad(this.gamepad1, this.timer, "GP");

        robot.init(hardwareMap);

        robot.swerveStraight(0,0);
        
        telemetry.update();
    }

    @Override
    public void loop() {
        impGamepad.update();

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

        servoUnderTest = robot.swerveWheels[servoIndex].servo;

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

            robot.swerveWheels[servoIndex].setOffset(servoUnderTest.getPosition());

            telemetry.addData("Left bumper","-0.1");
            telemetry.addData("Right bumper","+0.1");
            telemetry.addData("Left trigger","-0.01");
            telemetry.addData("Right trigger","+0.01");
            telemetry.addData("Servo name",robot.swerveWheels[servoIndex].name);
            telemetry.addData("Position", servoUnderTest.getPosition());
            telemetry.addData("Offset", robot.swerveWheels[servoIndex].offset);
            telemetry.addData("Rel Pos", servoUnderTest.getPosition() - robot.swerveWheels[servoIndex].offset);
            telemetry.addData("hardMin", robot.swerveWheels[servoIndex].hardMinWheelAngle);
            telemetry.addData("hardMax", robot.swerveWheels[servoIndex].hardMaxWheelAngle);
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
    }
}
