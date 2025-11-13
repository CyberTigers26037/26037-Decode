package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subassembly.AprilTagLimelight;

@SuppressWarnings("unused")
@Autonomous(name= "FarAuto", group="Pedro")
public class FarAuto extends PedroAutoBase {
    private enum PathState {
        DETECT_OBELISK,
        SCORE_PRELOAD,
        AUTO_AIM_PRELOAD,
        PREPARE_TO_LAUNCH_PRELOAD1,
        LAUNCH_PRELOAD1,
        PREPARE_TO_LAUNCH_PRELOAD2,
        LAUNCH_PRELOAD2,
        PREPARE_TO_LAUNCH_PRELOAD3,
        LAUNCH_PRELOAD3,
        AFTER_PRELOAD_LAUNCHES,
        PREP_PICKUP3,
        COLLECT_PICKUP3,
        PICKUP3_ARTIFACT3,
        SCORE_PICKUP3,
        AUTO_AIM_PRELOAD_3,
        PREPARE_TO_LAUNCH_PICKUP3_1,
        LAUNCH_PICKUP3_1,
        PREPARE_TO_LAUNCH_PICKUP3_2,
        LAUNCH_PICKUP3_2,
        PREPARE_TO_LAUNCH_PICKUP3_3,
        LAUNCH_PICKUP3_3,
        AFTER_PICKUP3_LAUNCHES,
        DRIVE_OUT_BOX,
        STOP
    }

    private PathState pathState;

    private final Pose scorePose = new Pose(60, 15, Math.toRadians(110)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
    private final Pose prepPickup3Pose = new Pose(48, 40, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose collect1Pose = new Pose (19, 35, Math.toRadians(180));
    private final Pose prepPickup2Pose = new Pose(50, 60, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose collect2Pose = new Pose(26, 60, Math.toRadians(180));
    // private final Pose prepPickup3Pose = new Pose(50, 36, Math.toRadians(180)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    private final Pose collect3Pose = new Pose(20, 40, Math.toRadians(180));
    private final Pose collect3Pose2 = new Pose(30, 40, Math.toRadians(180));
    private final Pose scorePoseNotHitWall = new Pose (56, 18, Math.toRadians(110));
    private final Pose driveOutWhiteBoxPose = new Pose (56, 40, Math.toRadians(110));

    public Pose getStartPose(){
        return new Pose(48, 8, Math.toRadians(90)); // Start Pose of our robot.
    }

    /** We do not use this because everything should automatically disable **/

    private Path scorePreload;
    private PathChain   driveOutBox, prepPickup1, scorePickup1, collectPickup1, prepPickup2, collectPickup2, scorePickup2, prepPickup3, collectPickup3, scorePickup3, pickup3Artifact3;

    public void buildPaths() {
        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        prepPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup3Pose.getHeading())
                .build();

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(collect1Pose, scorePoseNotHitWall))
                .setLinearHeadingInterpolation(collect1Pose.getHeading(), scorePoseNotHitWall.getHeading())
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
                .setLinearHeadingInterpolation(prepPickup3Pose.getHeading(), collect3Pose2.getHeading())
                .build();
        pickup3Artifact3 = follower.pathBuilder()
                .addPath(new BezierLine(collect3Pose2, collect3Pose))
                .setLinearHeadingInterpolation(collect3Pose2.getHeading(), collect3Pose.getHeading())
                .build();
        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(collect3Pose, scorePose))
                .setLinearHeadingInterpolation(collect3Pose.getHeading(), scorePose.getHeading())
                .build();
        driveOutBox = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, driveOutWhiteBoxPose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), driveOutWhiteBoxPose.getHeading())
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
                    setPathState(PathState.SCORE_PRELOAD);
                }
                break;
            case SCORE_PRELOAD:
                beginDetectingGoal();
                follower.followPath(scorePreload);

                artifactSystem.setLauncherRpm(3300);
                artifactSystem.startLauncher();
                setPathState(PathState.AUTO_AIM_PRELOAD);
                break;

            case AUTO_AIM_PRELOAD:
                if(!follower.isBusy()){
                    if(autoRotateTowardGoal(1) ){
                        stopAutoRotating();
                        setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD1);
                    }
                }
                break;

            case PREPARE_TO_LAUNCH_PRELOAD1:
                if (!follower.isBusy()) {
                    if (artifactSystem.moveCarouselToLaunchFirstColor(artifact1)) {
                        setPathState(PathState.LAUNCH_PRELOAD1);
                    }
                }
                break;
            case LAUNCH_PRELOAD1:
                if (artifactSystem.isCarouselAtTarget() && (artifactSystem.getActualLauncherRpm() > 3250)) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD2);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PRELOAD2:
                if (!artifactSystem.isFlipperRaised()) {
                    if (artifactSystem.moveCarouselToLaunchFirstColor(artifact2)) {
                        setPathState(PathState.LAUNCH_PRELOAD2);
                    }
                }
                break;
            case LAUNCH_PRELOAD2:
                if (artifactSystem.isCarouselAtTarget() && (artifactSystem.getActualLauncherRpm() > 3250)) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD3);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PRELOAD3:
                if (!artifactSystem.isFlipperRaised()) {
                    if (artifactSystem.moveCarouselToLaunchFirstColor(artifact3)) {
                        setPathState(PathState.LAUNCH_PRELOAD3);
                    }
                }
                break;
            case LAUNCH_PRELOAD3:
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.getActualLauncherRpm() > 3250)) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(PathState.AFTER_PRELOAD_LAUNCHES);
                    }
                }
                break;
            case AFTER_PRELOAD_LAUNCHES:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.stopLauncher();
                    setPathState(PathState.PREP_PICKUP3);
                }
                break;
            case PREP_PICKUP3:
                follower.followPath(prepPickup3);
                artifactSystem.startIntake();
                setPathState(PathState.COLLECT_PICKUP3);

                break;
            case COLLECT_PICKUP3:
                if (!follower.isBusy()) {
                    follower.followPath(collectPickup3, 0.2, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.PICKUP3_ARTIFACT3);
                }
                break;
            case PICKUP3_ARTIFACT3:
                if (!follower.isBusy()) {
                    setPathState(PathState.SCORE_PICKUP3);
                }
                break;

          /*  case PICKUP3_ARTIFACT3:
                if (pathTimer.getElapsedTimeSeconds() > 2.5) {
                    follower.followPath(pickup3Artifact3, 0.5, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.SCORE_PICKUP3);
                }


                break;

           */
            case SCORE_PICKUP3:
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    artifactSystem.stopIntake(false);
                    follower.followPath(scorePickup1, 1.0, Constants.followerConstants.automaticHoldEnd);
                    artifactSystem.setLauncherRpm(3192);
                    artifactSystem.startLauncher();
                    setPathState(PathState.AUTO_AIM_PRELOAD_3);
                }
                break;

            case AUTO_AIM_PRELOAD_3:
                if(!follower.isBusy()){
                    if(autoRotateTowardGoal(6) ){
                        stopAutoRotating();
                        setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP3_1);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP3_1:
                if (!follower.isBusy()){
                    if (artifactSystem.moveCarouselToLaunchFirstColor(artifact1)) {
                        setPathState(PathState.LAUNCH_PICKUP3_1);
                    }
                    else {
                        artifactSystem.moveCarouselToLaunchFirstNonEmptyPosition();
                        setPathState(PathState.LAUNCH_PICKUP3_1);
                    }
                }
                break;
            case LAUNCH_PICKUP3_1:
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.getActualLauncherRpm() > 3150)) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP3_2);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP3_2:
                if (!artifactSystem.isFlipperRaised()) {
                    if(artifactSystem.moveCarouselToLaunchFirstColor(artifact2)){
                        setPathState(PathState.LAUNCH_PICKUP3_2);
                    }
                    else {
                        artifactSystem.moveCarouselToLaunchFirstNonEmptyPosition();
                        setPathState(PathState.LAUNCH_PICKUP3_2);
                    }
                }
                break;
            case LAUNCH_PICKUP3_2:
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.getActualLauncherRpm() > 3150)) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP3_3);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP3_3:
                if (!artifactSystem.isFlipperRaised()) {
                    if (artifactSystem.moveCarouselToLaunchFirstColor(artifact3)){
                        setPathState(PathState.LAUNCH_PICKUP3_3);
                    }
                    else {
                        artifactSystem.moveCarouselToLaunchFirstNonEmptyPosition();
                        setPathState(PathState.LAUNCH_PICKUP3_3);
                    }
                }
                break;
            case LAUNCH_PICKUP3_3:
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.getActualLauncherRpm() > 3150)) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(PathState.DRIVE_OUT_BOX);
                    }
                }
                break;

            case DRIVE_OUT_BOX:
                if (pathTimer.getElapsedTimeSeconds() > 1){
                    follower.followPath(driveOutBox, 1, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.AFTER_PICKUP3_LAUNCHES);
                }
            break;

            case AFTER_PICKUP3_LAUNCHES:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.stopLauncher();
                    setPathState(PathState.STOP);
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
