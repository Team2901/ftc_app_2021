package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.BaseSkyStoneAuto;


/**
 *  Steps
 *  0) initialize robot and web camera with TensorFlow, point wheels forward
 *  1) Move forwards/backwards until a skystone location is within 10% of the center of the camera's view
 *  2) Turn to face the skystone
 *  3) Open the jaw
 *  4) Move forwards 2 feet towards the skystone
 *  5) Close the jaw on the skystone
 *  6) Move backwards 2 feet away from the skystone
 *  7) Turn to face towards the building zone
 *  8) Move back to where we were in step 1
 *
 *  TODO:
 *  9) Move forwards to in front of the waffle
 *  10) deposit skystone on waffle
 *  11) Park under the skybridge
 */

@Autonomous(name = "Swerve Straight Tester", group = "__TEST")
public class SwerveStraightTester extends BaseSkyStoneAuto {

    @Override
    public void runOpMode() throws InterruptedException {

        // Step 0) Initialize robot and web camera with TensorFlow
        robot.init(hardwareMap);

        robot.swerveStraightAbsolute(0,0);
        waitForStart();
        this.moveInches(0, 120, .4);

        while (opModeIsActive()) {
        }
    }


}
