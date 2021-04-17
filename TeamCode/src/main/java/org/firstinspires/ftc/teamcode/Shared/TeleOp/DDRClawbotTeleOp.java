package org.firstinspires.ftc.teamcode.Shared.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Shared.Gamepad.DDRGamepad;
import org.firstinspires.ftc.teamcode.Shared.Gamepad.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.Shared.Hardware.ClawbotHardware;

/**
 * Created by Kearneyg20428 on 2/7/2017.
 */

@TeleOp(name = "DDR Clawbot", group = "Shared")
public class DDRClawbotTeleOp extends OpMode {

    final double CLAW_SPEED = 0.05;
    final ClawbotHardware robot = new ClawbotHardware();
    double clawOffset = 0.0;
    boolean isLastClawPressed = false;
    boolean isClawOpen = false;
    boolean override = false;
    int difficultyMode = 1;
    String[] difficultyNames = {"Beginner", "Intermediate", "Lawsuit"};
    DDRGamepad participantGP;
    ImprovedGamepad gameMasterGP;
    ElapsedTime timer = new ElapsedTime();
    int konamiCodeProgress = 0;

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.claw.setPosition(ClawbotHardware.MID_SERVO);

        participantGP = new DDRGamepad(this.gamepad1, this.timer, "GP1");
        gameMasterGP = new ImprovedGamepad(this.gamepad2, this.timer, "GP2");
    }

    @Override
    public void loop() {
        participantGP.update();
        gameMasterGP.update();

        double participantLeftPower;
        double participantRightPower;
        double participantArmPower;

        // gm = game master
        double gmLeftPower = 0;
        double gmRightPower = 0;
        double gmArmPower = 0;

        /*if(robot.potentiometer.getVoltage() < ClawbotHardware.MIN_ARM_VOLTAGE){
            robot.armMotor.setPower(0.3);
        } else if(robot.potentiometer.getVoltage() > ClawbotHardware.MAX_ARM_VOLTAGE){
            robot.armMotor.setPower(-0.3);
        }*/

        //another different comment to prove a point

        // Moves robot forward using the left joystick

        danceRoutine(isKonamiCodeComplete());



        if(gameMasterGP.left_bumper.isInitialPress() && difficultyMode > 0){
            difficultyMode--;
        }

        if(gameMasterGP.right_bumper.isInitialPress() && difficultyMode < 2){
            difficultyMode++;
        }

        if(gameMasterGP.a.isInitialPress() && !gameMasterGP.start.getValue()){
            override = !override;
        }

        gmRightPower = gameMasterGP.left_stick_y.getValue();
        gmLeftPower = gameMasterGP.left_stick_y.getValue();

        // Turns the robot using the left joystick

        gmRightPower -= gameMasterGP.left_stick_x.getValue();
        gmLeftPower += gameMasterGP.left_stick_x.getValue();

        double maxPower = Math.max(Math.abs(gmLeftPower), Math.abs(gmRightPower));

        // Adjusts speeds for the arcs.
        if(maxPower > 1){
            gmRightPower /= maxPower;
            gmLeftPower /= maxPower;
        }

        // DDR pad left moves the arm down, DDR pad right moves the arm up, else, it stays in place.
        gmArmPower = gameMasterGP.right_stick_y.getValue() * .5;

        if(this.participantGP.rightArrow.getValue() && this.participantGP.leftArrow.getValue() && difficultyMode > 0){
            participantLeftPower = -0.75;
            participantRightPower = -0.75;

        } else if(this.participantGP.rightArrow.getValue() && this.participantGP.leftArrow.getValue() && difficultyMode == 0) {
            participantLeftPower = 0;
            participantRightPower = 0;
        }
        //Topleft + Up = arc counterclockwise
        //Left power = 0.75, Right power = 1
        else if(this.participantGP.leftArrow.getValue() && this.participantGP.upArrow.getValue()){
            participantLeftPower = 0.75;
            participantRightPower = 1;

        } //Top right + Up = arc clockwise
        //Left power = 1, Right power = 0.75
        else if(this.participantGP.rightArrow.getValue() && this.participantGP.upArrow.getValue()){
            participantLeftPower = 1;
            participantRightPower = 0.75;

        } //Top left = counterclockwise
        //Left power = -0.75, Right power = 0.75
        else if(this.participantGP.leftArrow.getValue()){
            participantLeftPower = -0.75;
            participantRightPower = 0.75;

        } //Top right = clockwise
        //Left power = 0.75, Right power = -0.75
        else if(this.participantGP.rightArrow.getValue()){
            participantLeftPower = 0.75;
            participantRightPower = -0.75;
        } //Up = straight
        //Left and right motors same power 0.75
        else if(this.participantGP.upArrow.getValue()){
            participantLeftPower = 0.75;
            participantRightPower = 0.75;
        } else {
            participantLeftPower = 0;
            participantRightPower = 0;
        }

        // DDR pad left moves the arm down, DDR pad right moves the arm up, else, it stays in place.
        if(this.participantGP.topLeftArrow.getValue() && robot.potentiometer.getVoltage() > ClawbotHardware.MIN_ARM_VOLTAGE) {
            participantArmPower = ClawbotHardware.ARM_DOWN_POWER;
        } else if(this.participantGP.topRightArrow.getValue() && robot.potentiometer.getVoltage() < ClawbotHardware.MAX_ARM_VOLTAGE){
            participantArmPower = ClawbotHardware.ARM_UP_POWER;
        } else {
            participantArmPower = 0;
        }

        // Checks to see if it is the initial press of DDR pad down
        if(this.participantGP.downArrow.isInitialPress() && !override && !this.participantGP.startButton.getValue()){
            isClawOpen = !isClawOpen;
        } else if(this.gameMasterGP.y.isInitialPress()) {
            isClawOpen = !isClawOpen;
        }

        //If isClawOpen is true, opens the claw, otherwise it closes the claw
        if(isClawOpen){
            robot.claw.setPosition(ClawbotHardware.MID_SERVO - ClawbotHardware.MIN_SAFE_CLAW_OFFSET);
        } else {
            robot.claw.setPosition(ClawbotHardware.MID_SERVO - ClawbotHardware.MAX_SAFE_CLAW_OFFSET);
        }


        final boolean participantInput = participantGP.areButtonsActive();



        // If the user is pressing a button and the override is turned off then
        // the participant can use the robot.  Otherwise, the game master has complete control.
        if(participantInput && !override) {
            if(difficultyMode == 0){
                participantLeftPower /= 3;
                participantRightPower /= 3;
            } else if(difficultyMode == 1){
                participantLeftPower *= 2.0/3;
                participantRightPower *= 2.0/3;
            }
            // Sets power to motors
            power(participantLeftPower, participantRightPower);
            robot.armMotor.setPower(participantArmPower);
        }else{
            power(gmLeftPower, gmRightPower);
            robot.armMotor.setPower(gmArmPower);
        }
        telemetry.addData("Override", override);
        telemetry.addData("Mode", difficultyNames[difficultyMode]);
        telemetry.addData("Participant Input", participantInput);
        telemetry.addData("Potentiometer", robot.potentiometer.getVoltage());
        telemetry.update();

        danceRoutine(gameMasterGP.b.getValue());
    }

    public void power(double left, double right) {
        robot.leftMotor.setPower(left);
        robot.rightMotor.setPower(right);
    }

    public void danceRoutine(boolean active){
        if(active){
            power(-.25, .25);
            while(robot.potentiometer.getVoltage() < robot.MAX_ARM_VOLTAGE) {
                //move claw all of the way up
                robot.armMotor.setPower(ClawbotHardware.ARM_UP_POWER);
            }
            robot.claw.setPosition(robot.MID_SERVO - robot.MAX_SAFE_CLAW_OFFSET);

            robot.claw.setPosition(robot.MID_SERVO - robot.MIN_SAFE_CLAW_OFFSET);

            robot.claw.setPosition(robot.MID_SERVO - robot.MAX_SAFE_CLAW_OFFSET);

            robot.claw.setPosition(robot.MID_SERVO - robot.MIN_SAFE_CLAW_OFFSET);


            while(robot.potentiometer.getVoltage() < 1.5) {
                //move claw all of the way down
                robot.armMotor.setPower(ClawbotHardware.ARM_DOWN_POWER);
            }
            power(0,0);
        }
    }

    public void telemetryDDRGraphic(){
        String lineOne = "";
        if(participantGP.topLeftArrow.getValue()){
            lineOne += "x|";
        } else {
            lineOne += "  |";
        }
        if(participantGP.upArrow.getValue()){
            lineOne += "^|";
        } else {
            lineOne += "  |";
        }
        if (participantGP.topRightArrow.getValue()) {
            lineOne += "o";
        }

        String lineTwo = "";
        if(participantGP.leftArrow.getValue()){
            lineTwo += "<|  |";
        } else {
            lineTwo += "  |  |";
        }
        if(participantGP.rightArrow.getValue()){
            lineTwo += ">";
        }

        String lineThree;
        if(participantGP.downArrow.getValue()) {
            lineThree = "  |*|  ";
        } else {
            lineThree = "  |  |  ";
        }

        telemetry.addLine(lineOne);
        telemetry.addLine(lineTwo);
        telemetry.addLine(lineThree);
    }

    public boolean isKonamiCodeComplete(){
        if ((konamiCodeProgress == 0) && (this.participantGP.upArrow.isInitialPress())){
            konamiCodeProgress = 1;
        }
        else if ((konamiCodeProgress == 1) && (this.participantGP.upArrow.isInitialPress())){
            konamiCodeProgress = 2;
        }
        else if ((konamiCodeProgress == 2) && (this.participantGP.downArrow.isInitialPress())){
            konamiCodeProgress = 3;
        }
        else if ((konamiCodeProgress == 3) && (this.participantGP.downArrow.isInitialPress())){
            konamiCodeProgress = 4;
        }
        else if ((konamiCodeProgress == 4) && (this.participantGP.leftArrow.isInitialPress())){
            konamiCodeProgress = 5;
        }
        else if ((konamiCodeProgress == 5) && (this.participantGP.rightArrow.isInitialPress())){
            konamiCodeProgress = 6;
        }
        else if ((konamiCodeProgress == 6) && (this.participantGP.leftArrow.isInitialPress())){
            konamiCodeProgress = 7;
        }
        else if ((konamiCodeProgress == 7) && (this.participantGP.rightArrow.isInitialPress())){
            konamiCodeProgress = 8;
        }
        else if ((konamiCodeProgress == 8) && (this.participantGP.topLeftArrow.isInitialPress())){
            konamiCodeProgress = 9;
        }
        else if ((konamiCodeProgress == 9) && (this.participantGP.topRightArrow.isInitialPress())){
            konamiCodeProgress = 10;
        }
        else if ((konamiCodeProgress == 10) && (this.participantGP.leftArrow.isInitialPress()) && (this.participantGP.rightArrow.isInitialPress())){
            konamiCodeProgress = 11;
            return true;
        }
        else
            konamiCodeProgress = 0;

        return (konamiCodeProgress == 11);
    }
}
