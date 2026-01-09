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
        // ============== Pickup 3 ==============
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
        // ============== Pickup 2 ==============
        PREP_PICKUP2,
        COLLECT_PICKUP2,
        PICKUP2_ARTIFACT3,
        // ============== PARK ==============
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
    private PathChain   driveOutBox, scorePickup3, prepPickup3, collectPickup3, scorePickup2, prepPickup2, collectPickup2;

    public void buildPaths() {
        Pose scorePose = isBlueAlliance ?
                new Pose(60, 15, Math.toRadians(120)) :
                new Pose(84, 12, Math.toRadians(60)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
        Pose prepPickup3Pose = isBlueAlliance ?
                new Pose(48, 40, Math.toRadians(180)) :// Highest (First Set) of Artifacts from the Spike Mark.
                new Pose(92, 25, Math.toRadians(0));
        Pose collect3Pose = isBlueAlliance ?
                new Pose(20, 40, Math.toRadians(180)) :
                new Pose(116, 25, Math.toRadians(0));
        Pose scorePose3NotHitWall = isBlueAlliance ?
                new Pose (56, 18, Math.toRadians(115)) :
                new Pose (84, 13, Math.toRadians(65));
        // ============== Pickup 2 ==============
        Pose prepPickup2Pose = isBlueAlliance ?
                new Pose(48, 64, Math.toRadians(180)) :
                new Pose(93, 49, Math.toRadians(0));
        Pose collect2Pose = isBlueAlliance ?
                new Pose(20, 64, Math.toRadians(180)) :
                new Pose(114, 49, Math.toRadians(0));
        Pose scorePose2NotHitWall = isBlueAlliance ?
                new Pose (56, 18, Math.toRadians(115)) :
                new Pose (88, 14, Math.toRadians(65));
        // ============== PARK ==============

        Pose driveOutWhiteBoxPose = isBlueAlliance ?
                new Pose (56, 40, Math.toRadians(180)) :
                new Pose (88, 30, Math.toRadians(70));

        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        // ===================================== Pickup 3 =====================================

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

        // ===================================== Pickup 2 =====================================

        prepPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup2Pose.getHeading())
                .build();
        collectPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(prepPickup2Pose, collect2Pose))
                .setLinearHeadingInterpolation(prepPickup2Pose.getHeading(), collect2Pose.getHeading())
                .build();

        // ===================================== PARK =====================================

        driveOutBox = follower.pathBuilder()
                .addPath(new BezierLine(collect2Pose, driveOutWhiteBoxPose))
                .setLinearHeadingInterpolation(collect2Pose.getHeading(), driveOutWhiteBoxPose.getHeading())
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

                artifactSystem.setLauncherRpm(2800);
                artifactSystem.startLauncher();
                adjustLauncherAngle.adjustFarAngle();
                adjustLauncherAngle.adjustAngle(2.0);
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
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    if (autoRotateTowardGoalAuto(4) || (pathTimer.getElapsedTimeSeconds() > 3.0)) {
                        stopAutoRotating();
                        setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD1);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PRELOAD1:
                moveCarouselToNextLaunchPosition(artifact1, PathState.LAUNCH_PRELOAD1, PathState.AFTER_PRELOAD_LAUNCHES);
                break;
            case LAUNCH_PRELOAD1:
                if (artifactSystem.isCarouselAtTarget() && (artifactSystem.getActualLauncherRpm() > 2750)) {
                    if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                        if (artifactSystem.raiseFlipper()) {
                            setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD2);
                        }
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PRELOAD2:
                moveCarouselToNextLaunchPosition(artifact2, PathState.LAUNCH_PRELOAD2, PathState.AFTER_PRELOAD_LAUNCHES);
                break;
            case LAUNCH_PRELOAD2:
                if (artifactSystem.isCarouselAtTarget() && (artifactSystem.getActualLauncherRpm() > 2750)) {
                    if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                        if (artifactSystem.raiseFlipper()) {
                            setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD3);
                        }
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PRELOAD3:
                moveCarouselToNextLaunchPosition(artifact3, PathState.LAUNCH_PRELOAD3, PathState.AFTER_PRELOAD_LAUNCHES);
                break;
            case LAUNCH_PRELOAD3:
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.getActualLauncherRpm() > 2750)) {
                    if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                        if (artifactSystem.raiseFlipper()) {
                            setPathState(PathState.AFTER_PRELOAD_LAUNCHES);
                        }
                    }
                }
                break;
            case AFTER_PRELOAD_LAUNCHES:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.stopLauncher();
                    setPathState(PathState.PREP_PICKUP3);
                }
                break;

            // ===================================== Pickup 3 =====================================


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
                    artifactSystem.setLauncherRpm(2800);
                    artifactSystem.startLauncher();
                    adjustLauncherAngle.adjustFarAngle();
                    adjustLauncherAngle.adjustAngle(2.0);

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
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    if(autoRotateTowardGoalAuto(2.5) || (pathTimer.getElapsedTimeSeconds() > 3.0)) {
                        stopAutoRotating();
                        setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP3_1);
                    }
                }

                break;
            case PREPARE_TO_LAUNCH_PICKUP3_1:
                moveCarouselToNextLaunchPosition(artifact1, PathState.LAUNCH_PICKUP3_1, PathState.AFTER_PICKUP3_LAUNCHES);
                break;
            case LAUNCH_PICKUP3_1:
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.getActualLauncherRpm() > 2750)) {
                    if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                        if (artifactSystem.raiseFlipper()) {
                            setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP3_2);
                        }
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP3_2:
                moveCarouselToNextLaunchPosition(artifact2, PathState.LAUNCH_PICKUP3_2, PathState.AFTER_PICKUP3_LAUNCHES);
                break;
            case LAUNCH_PICKUP3_2:
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.getActualLauncherRpm() > 2750)) {
                    if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                        if (artifactSystem.raiseFlipper()) {
                            setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP3_3);
                        }
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP3_3:
                moveCarouselToNextLaunchPosition(artifact3, PathState.LAUNCH_PICKUP3_3, PathState.AFTER_PICKUP3_LAUNCHES);
                break;
            case LAUNCH_PICKUP3_3:
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.getActualLauncherRpm() > 2750)) {
                    if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                        if (artifactSystem.raiseFlipper()) {
                            setPathState(PathState.AFTER_PICKUP3_LAUNCHES);
                        }
                    }
                }
                break;

            case AFTER_PICKUP3_LAUNCHES:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.stopLauncher();
                    if (autoMenu.getPickupMiddle()){
                        setPathState(PathState.PREP_PICKUP2);
                    }
                    else {
                        setPathState(PathState.DRIVE_OUT_BOX);
                    }
                }
                break;

            // ===================================== Pickup 2 =====================================


            case PREP_PICKUP2:
                follower.followPath(prepPickup2);
                artifactSystem.startIntake();
                setPathState(PathState.COLLECT_PICKUP2);
                break;

            case COLLECT_PICKUP2:
                if (!follower.isBusy()) {
                    follower.followPath(collectPickup2, 0.2, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.PICKUP2_ARTIFACT3);
                }
                break;
            case PICKUP2_ARTIFACT3:
                if (!follower.isBusy()) {
                    setPathState(PathState.DRIVE_OUT_BOX);
                }
                break;

            // ===================================== PARK =====================================


            case DRIVE_OUT_BOX:
                if (pathTimer.getElapsedTimeSeconds() > 1.0){
                    artifactSystem.stopLauncher();
                    follower.followPath(driveOutBox, 1, Constants.followerConstants.automaticHoldEnd);
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
        super.start();

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
