package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.config.RobotConfig;
import org.firstinspires.ftc.teamcode.subassembly.AdjustLauncherAngle;
import org.firstinspires.ftc.teamcode.subassembly.AllianceOverrideMenu;
import org.firstinspires.ftc.teamcode.subassembly.AprilTagLimelight;
import org.firstinspires.ftc.teamcode.subassembly.ArtifactSystem;
import org.firstinspires.ftc.teamcode.subassembly.ArtifactSystemAutoLauncher;
import org.firstinspires.ftc.teamcode.subassembly.MecanumDrive;

@SuppressWarnings("unused")
@TeleOp(name="DecodeTeleOp")
public class DecodeTeleOp extends OpMode {
    /*
    Control hub
        Motors
            Port 0 : front_left_drive
            Port 1 : back_left_drive
            Port 2 : back_right_drive
            Port 3 : front_right_drive
        Servos
            Port 0 : launcherServo
        Digital
            Port 1 : numberPlateSensor
        I2C
            Port 0 : imu
            Port 1 : sensor_color_distance_one
            Port 2 : sensor_color_distance_two
     Expansion hub
        Motors
            Port 0 : intakeMotor
            Port 1 : flywheelMotor
        Servos
            Port 1 :
            Port 2 : artifactLight
            Port 3 :
            Port 4 : carouselServo
            Port 5 : flipperServo
        I2C
            Port 2 : pinpoint
         Analog Input
            Port 0 : carouselServoEncoder
            Port 1 : flipperServoEncoder
     */

    private AllianceOverrideMenu allianceOverrideMenu;
    private ArtifactSystem artifactSystem;
    private ArtifactSystemAutoLauncher autoLauncher;
    private MecanumDrive drive;
    private final double flywheelPower = 0.5;
    private static final double TURN_GAIN     = 0.034;
    private static final double MAX_AUTO_TURN = 0.2;
    private AprilTagLimelight aprilTagLimeLight;
    private AdjustLauncherAngle adjustLauncherAngle;


    @Override
    public void init() {
        artifactSystem = new ArtifactSystem(hardwareMap);
        adjustLauncherAngle = new AdjustLauncherAngle(hardwareMap);
        drive = new MecanumDrive();
        drive.init(hardwareMap);
        autoLauncher = new ArtifactSystemAutoLauncher(artifactSystem);

        allianceOverrideMenu = new AllianceOverrideMenu();
        allianceOverrideMenu.init(hardwareMap);
        aprilTagLimeLight = new AprilTagLimelight();
        aprilTagLimeLight.init(hardwareMap);
    }

    @Override
    public void init_loop() {
        allianceOverrideMenu.init_loop(gamepad1, telemetry);
    }

    @Override
    public void start() {
        adjustLauncherAngle.adjustCloseAngle();
        artifactSystem.resetCarouselDetection();
        if (allianceOverrideMenu.isBlueAlliance()) {
            aprilTagLimeLight.beginDetectingTeamBlue();
        }
        else {
            aprilTagLimeLight.beginDetectingTeamRed();
        }
    }

    @Override
    public void loop() {


        if (gamepad2.left_trigger > 0.1) {
            artifactSystem.startLauncher();
        }
        else if (!autoLauncher.isRunning()){
            artifactSystem.stopLauncher();
        }
        if (gamepad2.rightBumperWasPressed()) {
            artifactSystem.moveCarouselToLaunchFirstPurple();
        }
        if (gamepad2.leftBumperWasPressed()) {
            artifactSystem.moveCarouselToLaunchFirstGreen();
        }

        if (gamepad2.dpadUpWasPressed()) {
            artifactSystem.adjustLauncherRpm(+ 100);
        }


        if (gamepad2.dpadDownWasPressed()) {
            artifactSystem.adjustLauncherRpm(- 100);
        }

        if (gamepad2.leftStickButtonWasPressed()) {
            autoLauncher.launchAllArtifacts(true);
        }





        if (gamepad1.rightStickButtonWasPressed() || gamepad2.rightStickButtonWasPressed()) {
            artifactSystem.resetCarouselDetection();
        }


        if (gamepad2.right_trigger > 0.1) {
            if (artifactSystem.isTargetRPMReached()){
                artifactSystem.raiseFlipper();
            }

        }


        if (gamepad2.xWasPressed()) {
            artifactSystem.moveCarouselToPosition(1);
        }
        if (gamepad2.yWasPressed()) {
            artifactSystem.moveCarouselToPosition(2);
        }
        if (gamepad2.bWasPressed()) {
            artifactSystem.moveCarouselToPosition(3);
        }

        if ((gamepad1.backWasPressed()) || (gamepad2.backWasPressed())) { // reverse intake
            artifactSystem.startReverseIntake();
        }
        if ((gamepad1.backWasReleased()) || (gamepad2.backWasReleased())) { // reverse intake
            artifactSystem.stopReverseIntake();
        }

        if (gamepad1.aWasPressed() || gamepad2.aWasPressed()){ // toggle intake
            artifactSystem.toggleIntake();

        }

        if (gamepad2.dpadRightWasPressed()) {
            if (autoLauncher.isRunning()){
                autoLauncher.overrideTurbo();
            }
            else {
                autoLauncher.launchAllArtifacts(false);
            }
        }

        Double goalAngle = aprilTagLimeLight.detectGoalAngle();

        double axial = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double yaw = gamepad1.right_stick_x;

        if (gamepad2.dpad_left && (goalAngle != null)) {
            double yawError = goalAngle;
            axial = 0;
            lateral = 0;
            yaw = Range.clip(yawError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
        }
        if (gamepad2.dpad_left) {
            Double goalDistance = aprilTagLimeLight.detectGoalDistance();
            if (goalDistance != null){
                if (goalDistance > 80) {
                    artifactSystem.setLauncherRpm(calculateFarRpmFromDistance(goalDistance));
                    adjustLauncherAngle.adjustFarAngle();
                }
                else {
                    artifactSystem.setLauncherRpm(calculateCloseRpmFromDistance(goalDistance));
                    adjustLauncherAngle.adjustCloseAngle();
                }

            }
        }

        // for testing
        if (gamepad1.yWasPressed()) {
            adjustLauncherAngle.adjustCloseAngle();
        }
        if (gamepad1.xWasPressed()) {
            adjustLauncherAngle.adjustFarAngle();
        }

        if (gamepad1.dpadRightWasPressed()) {
            adjustLauncherAngle.adjustAngle(1);
        }
        if (gamepad1.dpadLeftWasPressed()) {
            adjustLauncherAngle.adjustAngle(- 1);
        }
        if (gamepad1.left_stick_button){
            artifactSystem.reinitializeArtifactDetector();
        }

        autoLauncher.loop();
        artifactSystem.loop();

        artifactSystem.outputTelemetry(telemetry);
        autoLauncher.outputTelemetry(telemetry);
        adjustLauncherAngle.outputTelemetry(telemetry);

        drive.drive(axial, lateral, yaw);

        telemetry.addData("Goal Angle: ", goalAngle);
        aprilTagLimeLight.outputTelemetry(telemetry);

    }

    private int calculateCloseRpmFromDistance(Double goalDistance) {
        return (int)((RobotConfig.getCloseGoalCalcSlope() * goalDistance) + RobotConfig.getCloseGoalCalcYIntercept());
    }

    private int calculateFarRpmFromDistance(Double goalDistance) { // need to recalculate this auto aim
        return (int)(RobotConfig.getFarGoalRPM());
    }

}
