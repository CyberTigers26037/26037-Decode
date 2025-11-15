package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subassembly.AprilTagLimelight;
import org.firstinspires.ftc.teamcode.subassembly.ArtifactColor;

@SuppressWarnings("unused")
@Autonomous(name= "FarAuto", group="Pedro")
public class FarAuto extends PedroAutoBase {
    private enum PathState {
        DETECT_OBELISK,
        SCORE_PRELOAD,
        SCORE_PRELOAD_FINISH_DRIVING,
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
        SCORE_PICKUP3_FINISH_DRIVING,
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

    public Pose getStartPose(){
        return isBlueAlliance ?
                new Pose(48, 8, Math.toRadians(90)) :
                new Pose(96, 8, Math.toRadians(90)); // Start Pose of our robot.
    }

    /** We do not use this because everything should automatically disable **/

    private Path scorePreload;
    private PathChain   driveOutBox, scorePickup3, prepPickup3, collectPickup3;

    public void buildPaths() {
        Pose scorePose = isBlueAlliance ?
                new Pose(60, 15, Math.toRadians(110)) :
                new Pose(84, 15, Math.toRadians(70)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
        Pose prepPickup3Pose = isBlueAlliance ?
                new Pose(48, 40, Math.toRadians(180)) :// Highest (First Set) of Artifacts from the Spike Mark.
                new Pose(96, 40, Math.toRadians(0));
        Pose collect3Pose = isBlueAlliance ?
                new Pose(20, 40, Math.toRadians(180)) :
                new Pose(124, 40, Math.toRadians(0));
        Pose scorePose3NotHitWall = isBlueAlliance ?
                new Pose (56, 18, Math.toRadians(110)) :
                new Pose (88, 18, Math.toRadians(70));
        Pose driveOutWhiteBoxPose = isBlueAlliance ?
                new Pose (56, 40, Math.toRadians(110)) :
                new Pose (88, 40, Math.toRadians(70));

        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());
        prepPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup3Pose.getHeading())
                .build();
        collectPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(prepPickup3Pose, collect3Pose))
                .setLinearHeadingInterpolation(prepPickup3Pose.getHeading(), collect3Pose.getHeading())
                .build();
        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(collect3Pose, scorePose3NotHitWall))
                .setLinearHeadingInterpolation(collect3Pose.getHeading(), scorePose3NotHitWall.getHeading())
                .build();
        driveOutBox = follower.pathBuilder()
                .addPath(new BezierLine(scorePose3NotHitWall, driveOutWhiteBoxPose))
                .setLinearHeadingInterpolation(scorePose3NotHitWall.getHeading(), driveOutWhiteBoxPose.getHeading())
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
                setPathState(PathState.SCORE_PRELOAD_FINISH_DRIVING);
                break;

            case SCORE_PRELOAD_FINISH_DRIVING:
                if(!follower.isBusy()) {
                    // Stop pedro pathing following so we can auto-rotate the robot to aim
                    follower.breakFollowing();
                    setPathState(PathState.AUTO_AIM_PRELOAD);
                }
                break;
            case AUTO_AIM_PRELOAD:
                if(autoRotateTowardGoal(1)){
                    stopAutoRotating();
                    setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD1);
                }
                break;
            case PREPARE_TO_LAUNCH_PRELOAD1:
                moveCarouselToNextLaunchPosition(artifact1, PathState.LAUNCH_PRELOAD1, PathState.AFTER_PRELOAD_LAUNCHES);
                break;
            case LAUNCH_PRELOAD1:
                if (artifactSystem.isCarouselAtTarget() && (artifactSystem.getActualLauncherRpm() > 3250)) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD2);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PRELOAD2:
                moveCarouselToNextLaunchPosition(artifact2, PathState.LAUNCH_PRELOAD2, PathState.AFTER_PRELOAD_LAUNCHES);
                break;
            case LAUNCH_PRELOAD2:
                if (artifactSystem.isCarouselAtTarget() && (artifactSystem.getActualLauncherRpm() > 3250)) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD3);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PRELOAD3:
                moveCarouselToNextLaunchPosition(artifact3, PathState.LAUNCH_PRELOAD3, PathState.AFTER_PRELOAD_LAUNCHES);
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
            case SCORE_PICKUP3:
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    artifactSystem.stopIntake(false);
                    follower.followPath(scorePickup3, 1.0, Constants.followerConstants.automaticHoldEnd);
                    artifactSystem.setLauncherRpm(3192);
                    artifactSystem.startLauncher();
                    setPathState(PathState.SCORE_PICKUP3_FINISH_DRIVING);
                }
                break;

            case SCORE_PICKUP3_FINISH_DRIVING:
                if(!follower.isBusy()){
                    // Stop pedro pathing following so we can auto-rotate the robot to aim
                    follower.breakFollowing();
                    setPathState(PathState.AUTO_AIM_PRELOAD_3);
                }
                break;

            case AUTO_AIM_PRELOAD_3:
                if(autoRotateTowardGoal(1) ){
                    stopAutoRotating();
                    setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP3_1);
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP3_1:
                moveCarouselToNextLaunchPosition(artifact1, PathState.LAUNCH_PICKUP3_1, PathState.DRIVE_OUT_BOX);
                break;
            case LAUNCH_PICKUP3_1:
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.getActualLauncherRpm() > 3150)) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP3_2);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP3_2:
                moveCarouselToNextLaunchPosition(artifact2, PathState.LAUNCH_PICKUP3_2, PathState.DRIVE_OUT_BOX);
                break;
            case LAUNCH_PICKUP3_2:
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.getActualLauncherRpm() > 3150)) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP3_3);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP3_3:
                moveCarouselToNextLaunchPosition(artifact3, PathState.LAUNCH_PICKUP3_3, PathState.DRIVE_OUT_BOX);
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

    private void moveCarouselToNextLaunchPosition(ArtifactColor preferredColor, PathState successPathState, PathState failedPathState) {
        int position = artifactSystem.getNextArtifactPositionToLaunch(preferredColor);
        if(position != 0) {
            if (artifactSystem.moveCarouselToPosition(position)) {
                setPathState(successPathState);
            }
        }
        else {
            // Nothing to launch...
            setPathState(failedPathState);
        }
    }

    @Override
    protected void outputTelemetry() {
        telemetry.addData("path state", pathState);

        super.outputTelemetry();
    }
}
