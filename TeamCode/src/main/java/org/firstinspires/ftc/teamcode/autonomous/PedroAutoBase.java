package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subassembly.AprilTagLimelight;
import org.firstinspires.ftc.teamcode.subassembly.ArtifactColor;
import org.firstinspires.ftc.teamcode.subassembly.ArtifactSystem;

public abstract class PedroAutoBase extends OpMode {

    protected Follower follower;
    protected ArtifactSystem artifactSystem;
    protected AprilTagLimelight aprilTagLimeLight;

    protected Timer pathTimer, opmodeTimer;

    protected ArtifactColor artifact1;
    protected ArtifactColor artifact2;
    protected ArtifactColor artifact3;

    protected final Pose startPose = getStartPose();

    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        aprilTagLimeLight = new AprilTagLimelight();
        aprilTagLimeLight.init(hardwareMap);
        aprilTagLimeLight.beginDetectingObelisk();

        artifactSystem = new ArtifactSystem(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        artifactSystem.initializeArtifactColors(ArtifactColor.GREEN, ArtifactColor.PURPLE, ArtifactColor.PURPLE);

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);
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
}


