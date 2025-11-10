package org.firstinspires.ftc.teamcode.autonomous;



import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subassembly.AprilTagLimelight;

@SuppressWarnings("unused")
@Autonomous(name= "CloseAuto", group="Pedro")
public class CloseAuto extends PedroAutoBase {
    private enum PathState {
        DETECT_OBELISK,
        SCORE_PRELOAD,
        PREPARE_TO_LAUNCH_PRELOAD1,
        LAUNCH_PRELOAD1,
        PREPARE_TO_LAUNCH_PRELOAD2,
        LAUNCH_PRELOAD2,
        PREPARE_TO_LAUNCH_PRELOAD3,
        LAUNCH_PRELOAD3,
        AFTER_PRELOAD_LAUNCHES,
        PREP_PICKUP1, // pick up 1st set ---------------
        COLLECT_PICKUP1,
        PICKUP3_ARTIFACT1,
        SCORE_PICKUP1,
        PREPARE_TO_LAUNCH_PICKUP1_1,
        LAUNCH_PICKUP1_1,
        PREPARE_TO_LAUNCH_PICKUP1_2,
        LAUNCH_PICKUP1_2,
        PREPARE_TO_LAUNCH_PICKUP1_3,
        LAUNCH_PICKUP1_3,
        AFTER_PICKUP1_LAUNCHES,
        PREP_PICKUP2, // pick up 2nd set ---------------
        COLLECT_PICKUP2,
        PICKUP3_ARTIFACT2,
        SCORE_PICKUP2,
        PREPARE_TO_LAUNCH_PICKUP2_1,
        LAUNCH_PICKUP2_1,
        PREPARE_TO_LAUNCH_PICKUP2_2,
        LAUNCH_PICKUP2_2,
        PREPARE_TO_LAUNCH_PICKUP2_3,
        LAUNCH_PICKUP2_3,
        AFTER_PICKUP2_LAUNCHES,
        PREP_PICKUP3, // pick up 3rd set ---------------
        COLLECT_PICKUP3,
        PICKUP3_ARTIFACT3,
        SCORE_PICKUP3,
        PREPARE_TO_LAUNCH_PICKUP3_1,
        LAUNCH_PICKUP3_1,
        PREPARE_TO_LAUNCH_PICKUP3_2,
        LAUNCH_PICKUP3_2,
        PREPARE_TO_LAUNCH_PICKUP3_3,
        LAUNCH_PICKUP3_3,
        AFTER_PICKUP3_LAUNCHES,
        STOP
    }

    private PathState pathState;

    private final Pose scorePose = new Pose(48, 105, Math.toRadians(135)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
    private final Pose prepPickup1Pose = new Pose(50, 96, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose collect1Pose = new Pose(22, 96, Math.toRadians(180));
    private final Pose prepPickup2Pose = new Pose(50, 65, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose collect2Pose = new Pose(22, 65, Math.toRadians(180));
    private final Pose prepPickup3Pose = new Pose(50, 41, Math.toRadians(180)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    private final Pose collect3Pose = new Pose(22, 41, Math.toRadians(180));
    public Pose getStartPose(){
        return new Pose(24, 122, Math.toRadians(31)); // Start Pose of our robot.
    }




    /** We do not use this because everything should automatically disable **/

    private Path scorePreload;
    private PathChain prepPickup1, scorePickup1, collectPickup1, prepPickup2, collectPickup2, scorePickup2, prepPickup3, collectPickup3, scorePickup3;
    public void buildPaths() {
        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        prepPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup1Pose.getHeading())
                .build();

        collectPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(prepPickup1Pose, collect1Pose))
                .setLinearHeadingInterpolation(prepPickup1Pose.getHeading(), collect1Pose.getHeading())
                .build();

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(collect1Pose, scorePose))
                .setLinearHeadingInterpolation(collect1Pose.getHeading(), scorePose.getHeading())
                .build();

        prepPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup2Pose.getHeading())
                .build();

        collectPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(prepPickup2Pose, collect2Pose))
                .setLinearHeadingInterpolation(prepPickup2Pose.getHeading(), collect2Pose.getHeading())
                .build();

        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(collect2Pose, scorePose))
                .setLinearHeadingInterpolation(collect2Pose.getHeading(), scorePose.getHeading())
                .build();

        prepPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup3Pose.getHeading())
                .build();

        collectPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(prepPickup3Pose, collect3Pose))
                .setLinearHeadingInterpolation(prepPickup3Pose.getHeading(), collect3Pose.getHeading())
                .build();

        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(collect3Pose, scorePose))
                .setLinearHeadingInterpolation(collect3Pose.getHeading(), scorePose.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {

            case DETECT_OBELISK:
                AprilTagLimelight.ObeliskOrder obeliskOrder = aprilTagLimeLight.findObeliskArtifactOrder();
                if (obeliskOrder != AprilTagLimelight.ObeliskOrder.NONE) {
                    setObeliskOder(obeliskOrder);
                    setPathState(PathState.SCORE_PRELOAD);
                }
                else if (pathTimer.getElapsedTimeSeconds() > 3.0){
                    setObeliskOder(AprilTagLimelight.ObeliskOrder.GPP);
                    setPathState(CloseAuto.PathState.SCORE_PRELOAD);
                }
                break;

            case SCORE_PRELOAD:
                follower.followPath(scorePreload);

                artifactSystem.setLauncherRpm(2420);
                artifactSystem.startLauncher();
                setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PRELOAD1);
                break;
            case PREPARE_TO_LAUNCH_PRELOAD1:
                if ((!follower.isBusy()) && (artifactSystem.getActualLauncherRpm() > 2400)) {
                    artifactSystem.moveCarouselToLaunchFirstColor(artifact1);
                    setPathState(CloseAuto.PathState.LAUNCH_PRELOAD1);
                }
                break;
            case LAUNCH_PRELOAD1:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PRELOAD2);
                }
                break; // launched 1st artifact
            case PREPARE_TO_LAUNCH_PRELOAD2:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.moveCarouselToLaunchFirstColor(artifact2);
                    setPathState(CloseAuto.PathState.LAUNCH_PRELOAD2);
                }
                break;
            case LAUNCH_PRELOAD2:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PRELOAD3);
                }
                break; // launched 2nd artifact
            case PREPARE_TO_LAUNCH_PRELOAD3:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.moveCarouselToLaunchFirstColor(artifact3);
                    setPathState(CloseAuto.PathState.LAUNCH_PRELOAD3);
                }
                break;
            case LAUNCH_PRELOAD3:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(CloseAuto.PathState.AFTER_PRELOAD_LAUNCHES);
                }
                break; // Launched 3rd artifact
            case AFTER_PRELOAD_LAUNCHES:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.stopLauncher();
                    setPathState(CloseAuto.PathState.PREP_PICKUP1);
                }
                break; // stop launch
            case PREP_PICKUP1:
                follower.followPath(prepPickup1);
                artifactSystem.startIntake();
                setPathState(CloseAuto.PathState.COLLECT_PICKUP1);
                break; // prep to intake

            case COLLECT_PICKUP1:
                if (!follower.isBusy()) {
                    follower.followPath(collectPickup1, 0.2, Constants.followerConstants.automaticHoldEnd);
                    setPathState(CloseAuto.PathState.SCORE_PICKUP1);
                }
                break;

            case SCORE_PICKUP1:
                if (!follower.isBusy()) {
                    artifactSystem.stopIntake();
                    follower.followPath(scorePickup1, 1.0, Constants.followerConstants.automaticHoldEnd);
                    artifactSystem.setLauncherRpm(2420);
                    artifactSystem.startLauncher();
                    setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PICKUP1_1);
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP1_1:
                if (!follower.isBusy() && artifactSystem.getActualLauncherRpm() > 2400){
                    artifactSystem.moveCarouselToPosition(1);
                    setPathState(CloseAuto.PathState.LAUNCH_PICKUP1_1);
                }
                break;
            case LAUNCH_PICKUP1_1:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PICKUP1_2);
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP1_2:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.moveCarouselToPosition(2);
                    setPathState(CloseAuto.PathState.LAUNCH_PICKUP1_2);
                }
                break;
            case LAUNCH_PICKUP1_2:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PICKUP1_3);
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP1_3:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.moveCarouselToPosition(3);
                    setPathState(CloseAuto.PathState.LAUNCH_PICKUP1_3);
                }
                break;
            case LAUNCH_PICKUP1_3:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(CloseAuto.PathState.AFTER_PICKUP1_LAUNCHES);
                }
                break;
            case AFTER_PICKUP1_LAUNCHES:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.stopLauncher();
                    setPathState(CloseAuto.PathState.STOP);
                }
                break;
        }
    }

    private void setPathState(PathState pathState) {
        this.pathState = pathState;
        pathTimer.resetTimer();
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(PathState.DETECT_OBELISK);
    }

    @Override
    protected void outputTelemetry() {
        telemetry.addData("path state", pathState);


        super.outputTelemetry();
    }
}
