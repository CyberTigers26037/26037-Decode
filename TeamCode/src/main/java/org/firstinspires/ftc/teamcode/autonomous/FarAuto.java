package org.firstinspires.ftc.teamcode.autonomous;

import com.bylazar.configurables.annotations.Configurable;
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
@Configurable
public class FarAuto extends PedroAutoBase {
    public static class FarAutoConfig {
        public ConfigurablePose startPoseBlue = new ConfigurablePose(48, 8, 90);
        public ConfigurablePose startPoseRed  = new ConfigurablePose(96, 8, 90);

        public ConfigurablePose scorePoseBlue = new ConfigurablePose(60, 12, 120);
        public ConfigurablePose scorePoseRed  = new ConfigurablePose(84, 12, 75);

        public ConfigurablePose prepPickup3Blue = new ConfigurablePose(54, 33, 180);
        public ConfigurablePose prepPickup3Red = new ConfigurablePose(90, 32, 0);

        public ConfigurablePose collectSpike3Artifact1Blue = new ConfigurablePose(44, 35, 180);
        public ConfigurablePose collectSpike3Artifact1Red = new ConfigurablePose(100, 34, 0);

        public ConfigurablePose collectSpike3Artifact2Blue = new ConfigurablePose(38, 35, 180);
        public ConfigurablePose collectSpike3Artifact2Red = new ConfigurablePose(106, 34, 0);

        public ConfigurablePose collectSpike3Artifact3Blue = new ConfigurablePose(26, 35, 180);
        public ConfigurablePose collectSpike3Artifact3Red = new ConfigurablePose(114, 34, 0);

        public ConfigurablePose scorePose3NotHitWallBlue = new ConfigurablePose(56, 23, 115);
        public ConfigurablePose scorePose3NotHitWallRed = new ConfigurablePose(84, 23, 75);

        public ConfigurablePose prepPickup2Blue = new ConfigurablePose(54, 57, 180);
        public ConfigurablePose prepPickup2Red = new ConfigurablePose(90, 56, 0);

        public ConfigurablePose collectSpike2Artifact1Blue = new ConfigurablePose(45, 59, 180);
        public ConfigurablePose collectSpike2Artifact1Red = new ConfigurablePose(100, 58, 0);

        public ConfigurablePose collectSpike2Artifact2Blue = new ConfigurablePose(38, 59, 180);
        public ConfigurablePose collectSpike2Artifact2Red = new ConfigurablePose(106, 58, 0);

        public ConfigurablePose collectSpike2Artifact3Blue = new ConfigurablePose(26, 59, 180);
        public ConfigurablePose collectSpike2Artifact3Red = new ConfigurablePose(114, 58, 0);

        public ConfigurablePose scorePose2NotHitWallBlue = new ConfigurablePose(56, 23, 115);
        public ConfigurablePose scorePose2NotHitWallRed = new ConfigurablePose(94, 26, 75);

        public ConfigurablePose driveOutWhiteBoxBlue = new ConfigurablePose(26, 59, 180);
        public ConfigurablePose driveOutWhiteBoxRed = new ConfigurablePose(114, 58, 0);

    }

    public static FarAutoConfig config = new FarAutoConfig();

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
        COLLECT_SPIKE3_ARTIFACT1,
        WAIT_BEFORE_SPIKE3_ARTIFACT2,
        COLLECT_SPIKE3_ARTIFACT2,
        WAIT_BEFORE_SPIKE3_ARTIFACT3,
        COLLECT_SPIKE3_ARTIFACT3,
        WAIT_AFTER_SPIKE3_ARTIFACT3,
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
        COLLECT_SPIKE2_ARTIFACT1,
        WAIT_BEFORE_SPIKE2_ARTIFACT2,
        COLLECT_SPIKE2_ARTIFACT2,
        WAIT_BEFORE_SPIKE2_ARTIFACT3,
        COLLECT_SPIKE2_ARTIFACT3,
        WAIT_AFTER_SPIKE2_ARTIFACT3,
        // ============== PARK ==============
        DRIVE_OUT_BOX,
        STOP
    }

    private PathState pathState;

    public Pose getStartPose(){
        return isBlueAlliance ?
                config.startPoseBlue.getPose() :
                config.startPoseRed.getPose(); // Start Pose of our robot.
    }

    /** We do not use this because everything should automatically disable **/

    private Path scorePreload;
    private PathChain   driveOutBox, scorePickup3, prepPickup3, collectSpike3Artifact1, collectSpike3Artifact2, collectSpike3Artifact3, scorePickup2, prepPickup2, collectSpike2Artifact1, collectSpike2Artifact2, collectSpike2Artifact3;

    public void buildPaths() {
        Pose scorePose = isBlueAlliance ? config.scorePoseBlue.getPose() : config.scorePoseRed.getPose(); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
        Pose prepPickup3Pose = isBlueAlliance ? config.prepPickup3Blue.getPose() : config.prepPickup3Red.getPose(); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
        Pose collectSpike3Artifact1Pose = isBlueAlliance ? config.collectSpike3Artifact1Blue.getPose() : config.collectSpike3Artifact1Red.getPose();
        Pose collectSpike3Artifact2Pose = isBlueAlliance ? config.collectSpike3Artifact2Blue.getPose() : config.collectSpike3Artifact2Red.getPose();
        Pose collectSpike3Artifact3Pose = isBlueAlliance ? config.collectSpike3Artifact3Blue.getPose() : config.collectSpike3Artifact3Red.getPose(); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
        Pose scorePose3NotHitWall = isBlueAlliance ? config.scorePose3NotHitWallBlue.getPose() : config.scorePose3NotHitWallRed.getPose(); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
        Pose prepPickup2Pose = isBlueAlliance ? config.prepPickup2Blue.getPose() : config.prepPickup2Red.getPose(); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
        Pose collectSpike2Artifact1Pose = isBlueAlliance ? config.collectSpike2Artifact1Blue.getPose() : config.collectSpike2Artifact1Red.getPose();
        Pose collectSpike2Artifact2Pose = isBlueAlliance ? config.collectSpike2Artifact2Blue.getPose() : config.collectSpike2Artifact2Red.getPose();
        Pose collectSpike2Artifact3Pose = isBlueAlliance ? config.collectSpike2Artifact3Blue.getPose() : config.collectSpike2Artifact3Red.getPose(); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
        Pose scorePose2NotHitWall = isBlueAlliance ? config.scorePose2NotHitWallBlue.getPose() : config.scorePose2NotHitWallRed.getPose(); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
        Pose driveOutWhiteBoxPose = isBlueAlliance ? config.driveOutWhiteBoxBlue.getPose() : config.driveOutWhiteBoxRed.getPose(); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.

        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        // ===================================== Pickup 3 =====================================

        prepPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup3Pose.getHeading())
                .setTimeoutConstraint(0.5)
                .build();
        collectSpike3Artifact1 = follower.pathBuilder()
                .addPath(new BezierLine(prepPickup3Pose, collectSpike3Artifact1Pose))
                .setLinearHeadingInterpolation(prepPickup3Pose.getHeading(), collectSpike3Artifact1Pose.getHeading())
                .build();
        collectSpike3Artifact2 = follower.pathBuilder()
                .addPath(new BezierLine(collectSpike3Artifact1Pose, collectSpike3Artifact2Pose))
                .setLinearHeadingInterpolation(collectSpike3Artifact1Pose.getHeading(), collectSpike3Artifact2Pose.getHeading())
                .build();
        collectSpike3Artifact3 = follower.pathBuilder()
                .addPath(new BezierLine(collectSpike3Artifact2Pose, collectSpike3Artifact3Pose))
                .setLinearHeadingInterpolation(collectSpike3Artifact2Pose.getHeading(), collectSpike3Artifact3Pose.getHeading())
                .build();
        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(collectSpike3Artifact3Pose, scorePose3NotHitWall))
                .setLinearHeadingInterpolation(collectSpike3Artifact3Pose.getHeading(), scorePose3NotHitWall.getHeading())
                .build();

        // ===================================== Pickup 2 =====================================

        prepPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup2Pose.getHeading())
                .setTimeoutConstraint(0.5)
                .build();
        collectSpike2Artifact1 = follower.pathBuilder()
                .addPath(new BezierLine(prepPickup2Pose, collectSpike2Artifact1Pose))
                .setLinearHeadingInterpolation(prepPickup2Pose.getHeading(), collectSpike2Artifact1Pose.getHeading())
                .build();
        collectSpike2Artifact2 = follower.pathBuilder()
                .addPath(new BezierLine(collectSpike2Artifact1Pose, collectSpike2Artifact2Pose))
                .setLinearHeadingInterpolation(collectSpike2Artifact1Pose.getHeading(), collectSpike2Artifact2Pose.getHeading())
                .build();
        collectSpike2Artifact3 = follower.pathBuilder()
                .addPath(new BezierLine(collectSpike2Artifact2Pose, collectSpike2Artifact3Pose))
                .setLinearHeadingInterpolation(collectSpike2Artifact2Pose.getHeading(), collectSpike2Artifact3Pose.getHeading())
                .build();

        // ===================================== PARK =====================================

        driveOutBox = follower.pathBuilder()
                .addPath(new BezierLine(collectSpike2Artifact3Pose, driveOutWhiteBoxPose))
                .setLinearHeadingInterpolation(collectSpike2Artifact3Pose.getHeading(), driveOutWhiteBoxPose.getHeading())
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

                artifactSystem.setLauncherRpm(3400);
                artifactSystem.startLauncher();
                adjustLauncherAngle.adjustFarAngleAuto();
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
                    if (autoRotateTowardGoalAuto( isBlueAlliance? 3:-1) || (pathTimer.getElapsedTimeSeconds() > 3.0)) {
                        stopAutoRotating();
                        setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD1);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PRELOAD1:
                moveCarouselToNextLaunchPosition(artifact1, PathState.LAUNCH_PRELOAD1, PathState.AFTER_PRELOAD_LAUNCHES);
                break;
            case LAUNCH_PRELOAD1:
                if (artifactSystem.isCarouselAtTarget() && (artifactSystem.isTargetRPMReached())) {
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
                if (artifactSystem.isCarouselAtTarget() && (artifactSystem.isTargetRPMReached())) {
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
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.isTargetRPMReached())) {
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
                follower.followPath(prepPickup3, 0.7, Constants.followerConstants.automaticHoldEnd);
                artifactSystem.startIntake();
                setPathState(PathState.COLLECT_SPIKE3_ARTIFACT1);
                break;
            case COLLECT_SPIKE3_ARTIFACT1:
                if (!follower.isBusy()) {
                    follower.followPath(collectSpike3Artifact1, 0.25, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.WAIT_BEFORE_SPIKE3_ARTIFACT2);
                }
                break;
            case WAIT_BEFORE_SPIKE3_ARTIFACT2:
                if (!follower.isBusy()) {
                    setPathState(PathState.COLLECT_SPIKE3_ARTIFACT2);
                }
                break;
            case COLLECT_SPIKE3_ARTIFACT2:
                if (pathTimer.getElapsedTimeSeconds() > 0.2) {
                    follower.followPath(collectSpike3Artifact2, 0.25, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.WAIT_BEFORE_SPIKE3_ARTIFACT3);
                }
                break;
            case WAIT_BEFORE_SPIKE3_ARTIFACT3:
                if (!follower.isBusy()) {
                    setPathState(PathState.COLLECT_SPIKE3_ARTIFACT3);
                }
                break;
            case COLLECT_SPIKE3_ARTIFACT3:
                if (pathTimer.getElapsedTimeSeconds() > 0.2) {
                    follower.followPath(collectSpike3Artifact3, 0.25, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.WAIT_AFTER_SPIKE3_ARTIFACT3);
                }
                break;
            case WAIT_AFTER_SPIKE3_ARTIFACT3:
                if (!follower.isBusy()) {
                    setPathState(PathState.SCORE_PICKUP3);
                }
                break;
            case SCORE_PICKUP3:
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    artifactSystem.stopIntake(false);
                    follower.followPath(scorePickup3, 0.8, Constants.followerConstants.automaticHoldEnd);
                    artifactSystem.setLauncherRpm(3400);
                    artifactSystem.startLauncher();
                    adjustLauncherAngle.adjustFarAngleAuto();

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
                    if(autoRotateTowardGoalAuto(isBlueAlliance? 4:0) || (pathTimer.getElapsedTimeSeconds() > 3.0)) {
                        stopAutoRotating();
                        setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP3_1);
                    }
                }

                break;
            case PREPARE_TO_LAUNCH_PICKUP3_1:
                moveCarouselToNextLaunchPosition(artifact1, PathState.LAUNCH_PICKUP3_1, PathState.AFTER_PICKUP3_LAUNCHES);
                break;
            case LAUNCH_PICKUP3_1:
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.isTargetRPMReached())) {
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
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.isTargetRPMReached())) {
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
                if (artifactSystem.isCarouselAtTarget() &&  (artifactSystem.isTargetRPMReached())) {
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
                follower.followPath(prepPickup2, 0.7, Constants.followerConstants.automaticHoldEnd);
                artifactSystem.startIntake();
                setPathState(PathState.COLLECT_SPIKE2_ARTIFACT1);
                break;
            case COLLECT_SPIKE2_ARTIFACT1:
                if (!follower.isBusy()) {
                    follower.followPath(collectSpike2Artifact1, 0.25, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.WAIT_BEFORE_SPIKE2_ARTIFACT2);
                }
                break;
            case WAIT_BEFORE_SPIKE2_ARTIFACT2:
                if (!follower.isBusy()) {
                    setPathState(PathState.COLLECT_SPIKE2_ARTIFACT2);
                }
                break;
            case COLLECT_SPIKE2_ARTIFACT2:
                if (pathTimer.getElapsedTimeSeconds() > 0.2) {
                    follower.followPath(collectSpike2Artifact2, 0.25, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.WAIT_BEFORE_SPIKE2_ARTIFACT3);
                }
                break;
            case WAIT_BEFORE_SPIKE2_ARTIFACT3:
                if (!follower.isBusy()) {
                    setPathState(PathState.COLLECT_SPIKE2_ARTIFACT3);
                }
                break;
            case COLLECT_SPIKE2_ARTIFACT3:
                if (pathTimer.getElapsedTimeSeconds() > 0.2) {
                    follower.followPath(collectSpike2Artifact3, 0.25, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.WAIT_AFTER_SPIKE2_ARTIFACT3);
                }
                break;
            case WAIT_AFTER_SPIKE2_ARTIFACT3:
                if (!follower.isBusy()) {
                    setPathState(PathState.DRIVE_OUT_BOX);
                }
                break;

            // ===================================== PARK =====================================


            case DRIVE_OUT_BOX:
                if (pathTimer.getElapsedTimeSeconds() > 1.0){
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
    protected String getAutoName() {
        return "Far Auto";
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
