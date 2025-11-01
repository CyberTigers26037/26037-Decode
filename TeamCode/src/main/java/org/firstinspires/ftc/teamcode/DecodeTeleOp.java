package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.subassembly.AprilTagLimelight;
import org.firstinspires.ftc.teamcode.subassembly.ArtifactSystem;
import org.firstinspires.ftc.teamcode.subassembly.MecanumDrive;

@SuppressWarnings("unused")
@TeleOp(name="DecodeTeleOp")
public class DecodeTeleOp extends OpMode {
    private ArtifactSystem artifactSystem;
    private MecanumDrive drive;
    private final double flywheelPower = 0.5;
    private static final double TURN_GAIN     = 0.034;
    private static final double MAX_AUTO_TURN = 0.2;
    private AprilTagLimelight aprilTagLimeLight;

    @Override
    public void init() {
        artifactSystem = new ArtifactSystem(hardwareMap);
        drive = new MecanumDrive();
        drive.init(hardwareMap);

        aprilTagLimeLight = new AprilTagLimelight();
        aprilTagLimeLight.init(hardwareMap);
        aprilTagLimeLight.beginDetectingTeamBlue();
    }



    @Override
    public void loop() {

        if (gamepad1.dpad_right) {
            Double goalDistance = aprilTagLimeLight.detectGoalDistance();
            if (goalDistance != null){
                artifactSystem.setLauncherRpm(calculateRpmFromDistance(goalDistance));
            }
        }
        if (gamepad1.left_trigger > 0.1) {
            artifactSystem.startLauncher();
        }
        else {
            artifactSystem.stopLauncher();
        }
        if (gamepad1.rightBumperWasPressed()) {
            artifactSystem.moveCarouselToLaunchFirstPurple();
        }
        if (gamepad1.leftBumperWasPressed()) {
            artifactSystem.moveCarouselToLaunchFirstGreen();
        }

        if (gamepad1.dpadUpWasPressed()) {
            artifactSystem.adjustLauncherRpm(+ 100);
        }


        if (gamepad1.dpadDownWasPressed()) {
            artifactSystem.adjustLauncherRpm(- 100);
        }


        if (gamepad1.aWasPressed()) {
            artifactSystem.toggleIntake();
            artifactSystem.moveCarouselToPosition(1);
        }


        if (gamepad1.rightStickButtonWasPressed()) {
            artifactSystem.resetCarouselDetection();
        }


        if (gamepad1.right_trigger > 0.1) {
            artifactSystem.raiseFlipper();
        }


        if (gamepad1.xWasPressed()) {
            artifactSystem.moveCarouselToPosition(1);
        }
        if (gamepad1.yWasPressed()) {
            artifactSystem.moveCarouselToPosition(2);
        }
        if (gamepad1.bWasPressed()) {
            artifactSystem.moveCarouselToPosition(3);
        }

        artifactSystem.loop();

        artifactSystem.outputTelemetry(telemetry);

        Double goalAngle = aprilTagLimeLight.detectGoalAngle();

        double axial = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double yaw = gamepad1.right_stick_x;

        if (gamepad1.dpad_left && (goalAngle != null)) {
            double yawError = goalAngle;
            axial = 0;
            lateral = 0;
            yaw = Range.clip(yawError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
        }

        drive.drive(axial, lateral, yaw);

        telemetry.addData("Goal Angle: ", goalAngle);
        aprilTagLimeLight.outputTelemetry(telemetry);

    }

    private int calculateRpmFromDistance(Double goalDistance) {

        return (int)(9.060*(goalDistance) + 1901.0);
    }
}
