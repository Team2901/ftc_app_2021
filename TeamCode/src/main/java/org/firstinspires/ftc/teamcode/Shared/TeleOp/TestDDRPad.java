package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Shared.Gamepad.DDRGamepad;
import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;

@Disabled
@TeleOp(name = "DDR Pad Test", group = "Shared Test")
public class TestDDRPad extends OpMode {

    public ElapsedTime timer = new ElapsedTime();
    public DDRGamepad dancePad;

    @Override
    public void init() {
        this.dancePad = new DDRGamepad(gamepad1, timer, "g1");
    }

    @Override
    public void loop() {
        dancePad.update();

        /*
         * +----+----+----+
         * |    |    |    |
         * |    |    |    |
         * +----+----+----+
         * |    |    |    |
         * |    |    |    |
         * +----+----+----+
         * |    |    |    |
         * |    |    |    |
         * +----+----+----+
         */

        telemetry.addData("Are buttons active", dancePad.areButtonsActive());

        if(dancePad.upArrow.getValue()){
            telemetry.addLine("Up arrow");
        }

        if(dancePad.downArrow.getValue()){
            telemetry.addLine("Down arrow");
        }

        if(dancePad.leftArrow.getValue()){
            telemetry.addLine("Left arrow");
        }

        if(dancePad.rightArrow.getValue()){
            telemetry.addLine("Right arrow");
        }

        if(dancePad.topLeftArrow.getValue()){
            telemetry.addLine("Top left arrow");
        }

        if(dancePad.topRightArrow.getValue()){
            telemetry.addLine("Top right arrow");
        }

        if(dancePad.startButton.getValue()){
            telemetry.addLine("Start button");
        }

        if(dancePad.a.getValue()){
            telemetry.addLine("a button");
        }

        telemetry.update();
    }
}
