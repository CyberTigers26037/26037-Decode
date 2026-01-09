package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subassembly.AdjustLauncherAngle;
import org.firstinspires.ftc.teamcode.subassembly.AllianceOverrideMenu;
import org.firstinspires.ftc.teamcode.subassembly.AprilTagLimelight;
import org.firstinspires.ftc.teamcode.subassembly.ArtifactColor;
import org.firstinspires.ftc.teamcode.subassembly.ArtifactSystem;
import org.firstinspires.ftc.teamcode.subassembly.AutoMenu;
import org.firstinspires.ftc.teamcode.subassembly.MecanumDrive;

public abstract class PedroAutoBase extends OpMode {
    private static final double TURN_GAIN     = 0.034;
    private static final double MAX_AUTO_TURN = 0.2;
    private static final double TURN_GAIN_AUTO = 0.051;

    protected MecanumDrive drive;
    protected Follower follower;
    protected ArtifactSystem artifactSystem;
    protected AdjustLauncherAngle adjustLauncherAngle;
    protected AprilTagLimelight aprilTagLimeLight;

    protected Timer pathTimer, opmodeTimer;

    protected ArtifactColor artifact1;
    protected ArtifactColor artifact2;
    protected ArtifactColor artifact3;

    protected Pose startPose;
    protected boolean isBlueAlliance;

    private AllianceOverrideMenu allianceOverrideMenu;
    protected AutoMenu autoMenu;



    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        drive = new MecanumDrive();
        drive.init(hardwareMap);

        aprilTagLimeLight = new AprilTagLimelight();
        aprilTagLimeLight.init(hardwareMap);
        aprilTagLimeLight.beginDetectingObelisk();

        artifactSystem = new ArtifactSystem(hardwareMap);
        adjustLauncherAngle = new AdjustLauncherAngle(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        artifactSystem.initializeArtifactColors(ArtifactColor.GREEN, ArtifactColor.PURPLE, ArtifactColor.PURPLE);

        follower = Constants.createFollower(hardwareMap);

        autoMenu = new AutoMenu();
        autoMenu.init();

        allianceOverrideMenu = new AllianceOverrideMenu();
        allianceOverrideMenu.init(hardwareMap);
    }

    @Override
    public void init_loop() {
        allianceOverrideMenu.init_loop(gamepad1, telemetry);
        autoMenu.init_loop(gamepad1, telemetry);

    }

    @Override
    public void start() {
        isBlueAlliance = allianceOverrideMenu.isBlueAlliance();
        startPose = getStartPose();
        follower.setStartingPose(startPose);
        buildPaths();

    }

    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        autonomousPathUpdate();
        artifactSystem.loop();

        outputTelemetry();
        telemetry.update();
    }

    protected void outputTelemetry() {
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        Double goalAngle = aprilTagLimeLight.detectGoalAngle();

        if (goalAngle != null) {
            telemetry.addData("Goal Angle", goalAngle);
        }
        artifactSystem.outputTelemetry(telemetry);
    }

    public abstract void buildPaths();

    public abstract void autonomousPathUpdate();

    public abstract Pose getStartPose();

    protected void setObeliskOder(AprilTagLimelight.ObeliskOrder obeliskOrder) {
        switch(obeliskOrder){

            case GPP:
                artifact1 = ArtifactColor.GREEN;
                artifact2 = ArtifactColor.PURPLE;
                artifact3 = ArtifactColor.PURPLE;

                break;
            case PGP:
                artifact1 = ArtifactColor.PURPLE;
                artifact2 = ArtifactColor.GREEN;
                artifact3 = ArtifactColor.PURPLE;

                break;

            case PPG:
                artifact1 = ArtifactColor.PURPLE;
                artifact2 = ArtifactColor.PURPLE;
                artifact3 = ArtifactColor.GREEN;

                break;

        }
    }

    protected void beginDetectingGoal() {
        if (isBlueAlliance) {
            aprilTagLimeLight.beginDetectingTeamBlue();
        }
        else {
            aprilTagLimeLight.beginDetectingTeamRed();
        }
    }

    protected boolean autoRotateTowardGoal(double delta, double turnGain) {
        Double goalAngle = aprilTagLimeLight.detectGoalAngle();

        if (goalAngle != null) {
            double axial = 0;
            double lateral = 0;
            double yawError = goalAngle - delta;
            double yaw = Range.clip(yawError * turnGain, -MAX_AUTO_TURN, MAX_AUTO_TURN);
            drive.drive(axial, lateral, yaw);
            return (Math.abs(yawError) < 0.5);
        }

        return false;
    }

    @SuppressWarnings("SameParameterValue")
    protected boolean autoRotateTowardGoal(double delta) {
        return autoRotateTowardGoal(delta, TURN_GAIN);
    }

    protected boolean autoRotateTowardGoalAuto(double delta) {
        return autoRotateTowardGoal(delta, TURN_GAIN_AUTO);
    }

    protected void stopAutoRotating() {
            drive.stop();
    }
}


