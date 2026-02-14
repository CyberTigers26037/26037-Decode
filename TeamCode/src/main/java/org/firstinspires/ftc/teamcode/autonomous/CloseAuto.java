package org.firstinspires.ftc.teamcode.autonomous;



import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subassembly.AprilTagLimelight;
import org.firstinspires.ftc.teamcode.subassembly.ArtifactColor;

@SuppressWarnings("unused")
@Configurable
@Autonomous(name= "CloseAuto", group="Pedro")
public class CloseAuto extends PedroAutoBase {
    public static class CloseAutoConfig {
        public ConfigurablePose startPoseBlue = new ConfigurablePose(18, 119, 52);
        public ConfigurablePose startPoseRed  = new ConfigurablePose(125, 119, 126);

        public ConfigurablePose scanObeliskPoseBlue = new ConfigurablePose(48, 95, 70);
        public ConfigurablePose scanObeliskPoseRed  = new ConfigurablePose(96, 90, 110);

        public ConfigurablePose scorePoseBlue = new ConfigurablePose(48, 92, 140);
        public ConfigurablePose scorePoseRed  = new ConfigurablePose(92, 80, 45);

        public ConfigurablePose prepPickup1Blue = new ConfigurablePose(44, 79, 180);
        public ConfigurablePose prepPickup1Red  = new ConfigurablePose(96, 82, 0);

        public ConfigurablePose collectSpike1Artifact1Blue = new ConfigurablePose(33, 82, 180);
        public ConfigurablePose collectSpike1Artifact1Red = new ConfigurablePose(105, 82, 0);

        public ConfigurablePose collectSpike1Artifact2Blue = new ConfigurablePose(29, 82, 180);
        public ConfigurablePose collectSpike1Artifact2Red = new ConfigurablePose(110, 82, 0);

        public ConfigurablePose collectSpike1Artifact3Blue = new ConfigurablePose(23, 82, 180);
        public ConfigurablePose collectSpike1Artifact3Red = new ConfigurablePose(119, 82, 0);


        public ConfigurablePose prepPickup2Blue = new ConfigurablePose(42, 56, 180);
        public ConfigurablePose prepPickup2Red  = new ConfigurablePose(96, 58, 0);

        public ConfigurablePose collectSpike2Artifact1Blue = new ConfigurablePose(34, 58, 180);
        public ConfigurablePose collectSpike2Artifact1Red = new ConfigurablePose(105, 58, 0);

        public ConfigurablePose collectSpike2Artifact2Blue = new ConfigurablePose(29, 58, 180);
        public ConfigurablePose collectSpike2Artifact2Red = new ConfigurablePose(110, 58, 0);

        public ConfigurablePose collectSpike2Artifact3Blue = new ConfigurablePose(24, 58, 180);
        public ConfigurablePose collectSpike2Artifact3Red = new ConfigurablePose(119, 58, 0);

        public ConfigurablePose parkPoseBlue = new ConfigurablePose(30, 58, 180);
        public ConfigurablePose parkPoseRed  = new ConfigurablePose(114, 45, 50);

    }

    public static CloseAutoConfig config = new CloseAutoConfig();

    private enum PathState {
        PREP_SCAN_OBELISK,
        DRIVE_TO_SCAN,
        DETECT_OBELISK,
        SCORE_PRELOAD,
        SCORE_PRELOAD_AIM,
        AUTO_AIM_PRELOAD,
        PREPARE_TO_LAUNCH_PRELOAD1,
        WAIT_FOR_CAROUSEL_PRELOAD1,
        LAUNCH_PRELOAD1,
        PREPARE_TO_LAUNCH_PRELOAD2,
        WAIT_FOR_CAROUSEL_PRELOAD2,
        LAUNCH_PRELOAD2,
        PREPARE_TO_LAUNCH_PRELOAD3,
        WAIT_FOR_CAROUSEL_PRELOAD3,
        LAUNCH_PRELOAD3,
        AFTER_PRELOAD_LAUNCHES,
        // ============== Pickup 1 ==============
        PREP_PICKUP1,
        WAIT_BEFORE_SPIKE1_ARTIFACT2,
         COLLECT_SPIKE1_ARTIFACT2,
        WAIT_BEFORE_SPIKE1_ARTIFACT3,
        COLLECT_SPIKE1_ARTIFACT3,
        WAIT_AFTER_SPIKE1_ARTIFACT3,
        COLLECT_SPIKE1_ARTIFACT1,
        SCORE_PICKUP1,
        SCORE_PICKUP1_WAIT_FOR_DRIVING,
        AUTO_AIM_PICKUP1,
        PREPARE_TO_LAUNCH_PICKUP1_1,
        WAIT_FOR_CAROUSEL_PICKUP1_1,
        LAUNCH_PICKUP1_1,
        PREPARE_TO_LAUNCH_PICKUP1_2,
        WAIT_FOR_CAROUSEL_PICKUP1_2,
        LAUNCH_PICKUP1_2,
        PREPARE_TO_LAUNCH_PICKUP1_3,
        WAIT_FOR_CAROUSEL_PICKUP1_3,
        LAUNCH_PICKUP1_3,
        AFTER_PICKUP1_LAUNCHES,
        // ============== Pickup 2 ==============
        PREP_PICKUP2,
        COLLECT_SPIKE2_ARTIFACT1,
        WAIT_BEFORE_SPIKE2_ARTIFACT2,
        COLLECT_SPIKE2_ARTIFACT2,
        WAIT_BEFORE_SPIKE2_ARTIFACT3,
        COLLECT_SPIKE2_ARTIFACT3,
        WAIT_AFTER_SPIKE2_ARTIFACT3,
        PARK,
        STOP
    }

    private PathState pathState;




    public Pose getStartPose() {
        // Start Pose of our robot.
        return isBlueAlliance ? config.startPoseBlue.getPose() : config.startPoseRed.getPose();
    }




    /** We do not use this because everything should automatically disable **/

    private PathChain prepScan, prepPickup1, collectSpike1Artifact1,collectSpike1Artifact2,collectSpike1Artifact3, scorePickup1, prepPickup2,collectSpike2Artifact1, collectSpike2Artifact2, collectSpike2Artifact3, park;
    public void buildPaths() {
        Pose scanObeliskPose = isBlueAlliance ? config.scanObeliskPoseBlue.getPose() : config.scanObeliskPoseRed.getPose();
        Pose scorePose = isBlueAlliance ? config.scorePoseBlue.getPose() : config.scorePoseRed.getPose();
        Pose prepPickup1Pose = isBlueAlliance ? config.prepPickup1Blue.getPose() : config.prepPickup1Red.getPose();
        Pose collectSpike1Artifact1Pose = isBlueAlliance ? config.collectSpike1Artifact1Blue.getPose() : config.collectSpike1Artifact1Red.getPose();
        Pose collectSpike1Artifact2Pose = isBlueAlliance ? config.collectSpike1Artifact2Blue.getPose() : config.collectSpike1Artifact2Red.getPose();
        Pose collectSpike1Artifact3Pose = isBlueAlliance ? config.collectSpike1Artifact3Blue.getPose() : config.collectSpike1Artifact3Red.getPose();
        Pose prepPickup2Pose = isBlueAlliance ? config.prepPickup2Blue.getPose() : config.prepPickup2Red.getPose();
        Pose collectSpike2Artifact1Pose = isBlueAlliance ? config.collectSpike2Artifact1Blue.getPose() : config.collectSpike2Artifact1Red.getPose();
        Pose collectSpike2Artifact2Pose = isBlueAlliance ? config.collectSpike2Artifact2Blue.getPose() : config.collectSpike2Artifact2Red.getPose();
        Pose collectSpike2Artifact3Pose = isBlueAlliance ? config.collectSpike2Artifact3Blue.getPose() : config.collectSpike2Artifact3Red.getPose();
        Pose parkPose = isBlueAlliance ? config.parkPoseBlue.getPose() : config.parkPoseRed.getPose();


        prepScan = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scanObeliskPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scanObeliskPose.getHeading())
                .build();

        // ===================================== Pickup 1 =====================================

        prepPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup1Pose.getHeading())
                .build();


        collectSpike1Artifact1 = follower.pathBuilder()
                .addPath(new BezierLine(prepPickup1Pose, collectSpike1Artifact1Pose))
                .setLinearHeadingInterpolation(prepPickup1Pose.getHeading(), collectSpike1Artifact1Pose.getHeading())
                .build();

        collectSpike1Artifact2 = follower.pathBuilder()
                .addPath(new BezierLine(collectSpike1Artifact1Pose, collectSpike1Artifact2Pose))
                .setLinearHeadingInterpolation(collectSpike1Artifact1Pose.getHeading(), collectSpike1Artifact2Pose.getHeading())
                .build();
        collectSpike1Artifact3 = follower.pathBuilder()
                .addPath(new BezierLine(collectSpike1Artifact2Pose, collectSpike1Artifact3Pose))
                .setLinearHeadingInterpolation(collectSpike1Artifact2Pose.getHeading(), collectSpike1Artifact3Pose.getHeading())
                .build();


        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(collectSpike1Artifact3Pose, scorePose))
                .setLinearHeadingInterpolation(collectSpike1Artifact3Pose.getHeading(), scorePose.getHeading())
                .build();

        // ===================================== Pickup 2 =====================================

        prepPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup2Pose.getHeading())
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

        park = follower.pathBuilder()
                .addPath(new BezierLine(collectSpike2Artifact3Pose, parkPose))
                .setLinearHeadingInterpolation(collectSpike2Artifact3Pose.getHeading(), parkPose.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {

            case PREP_SCAN_OBELISK:
                follower.followPath(prepScan);
                setPathState(PathState.DRIVE_TO_SCAN);
                break;

            case DRIVE_TO_SCAN:
                if (!follower.isBusy()) {
                    setPathState(PathState.DETECT_OBELISK);
                }
                break;

            case DETECT_OBELISK:
                AprilTagLimelight.ObeliskOrder obeliskOrder = aprilTagLimeLight.findObeliskArtifactOrder();
                if (obeliskOrder != AprilTagLimelight.ObeliskOrder.NONE) {
                    setObeliskOder(obeliskOrder);
                    setPathState(PathState.SCORE_PRELOAD);
                } else if (pathTimer.getElapsedTimeSeconds() > 3.0) {
                    setObeliskOder(AprilTagLimelight.ObeliskOrder.GPP);
                    setPathState(CloseAuto.PathState.SCORE_PRELOAD);
                }
                break;

            case SCORE_PRELOAD:
                beginDetectingGoal();
                follower.turnToDegrees(isBlueAlliance ? 145 : 35);
                artifactSystem.setLauncherRpm(2200);
                artifactSystem.startLauncher();
                setPathState(CloseAuto.PathState.SCORE_PRELOAD_AIM);
                break;

            case SCORE_PRELOAD_AIM:
                if ((!follower.isBusy()) || (pathTimer.getElapsedTimeSeconds() > 2.0)) {
                    follower.breakFollowing();
                    setPathState(PathState.AUTO_AIM_PRELOAD);
                }
                break;
            case AUTO_AIM_PRELOAD:
                if (autoRotateTowardGoal(isBlueAlliance? 2 : 1) || (pathTimer.getElapsedTimeSeconds() > 1.0)) {
                    stopAutoRotating();
                    setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD1);
                }
                break;

            case PREPARE_TO_LAUNCH_PRELOAD1:
                moveCarouselToNextLaunchPosition(artifact1, PathState.WAIT_FOR_CAROUSEL_PRELOAD1, PathState.AFTER_PRELOAD_LAUNCHES);
                break;

            case WAIT_FOR_CAROUSEL_PRELOAD1:
                if (artifactSystem.isCarouselAtTarget()) {
                    setPathState(PathState.LAUNCH_PRELOAD1);
                }
                break;

            case LAUNCH_PRELOAD1:
                if (artifactSystem.isTargetRPMReached()) {
                    if (pathTimer.getElapsedTimeSeconds() > 0.5) {

                        if (artifactSystem.raiseFlipper()) {
                            setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PRELOAD2);
                        }
                    }
                }
                break; // launched 1st artifact

            case PREPARE_TO_LAUNCH_PRELOAD2:
                moveCarouselToNextLaunchPosition(artifact2, PathState.WAIT_FOR_CAROUSEL_PRELOAD2, PathState.AFTER_PRELOAD_LAUNCHES);

                break;
            case WAIT_FOR_CAROUSEL_PRELOAD2:
                if (artifactSystem.isCarouselAtTarget()) {
                    setPathState(PathState.LAUNCH_PRELOAD2);
                }
                break;
            case LAUNCH_PRELOAD2:
                if (artifactSystem.isTargetRPMReached()) {
                    if ((pathTimer.getElapsedTimeSeconds() > 0.5)) {
                        if (artifactSystem.raiseFlipper()) {
                            setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PRELOAD3);
                        }
                    }
                }
                break; // launched 2nd artifact

            case PREPARE_TO_LAUNCH_PRELOAD3:
                moveCarouselToNextLaunchPosition(artifact3, PathState.WAIT_FOR_CAROUSEL_PRELOAD3, PathState.AFTER_PRELOAD_LAUNCHES);

                break;
            case WAIT_FOR_CAROUSEL_PRELOAD3:
                if (artifactSystem.isCarouselAtTarget()) {
                    setPathState(PathState.LAUNCH_PRELOAD3);
                }
                break;
            case LAUNCH_PRELOAD3:
                if (artifactSystem.isTargetRPMReached()) {
                    if ((pathTimer.getElapsedTimeSeconds() > 0.5)) {
                        if (artifactSystem.raiseFlipper()) {
                            setPathState(CloseAuto.PathState.AFTER_PRELOAD_LAUNCHES);
                        }
                 }
                }
                break; // Launched 3rd artifact

            case AFTER_PRELOAD_LAUNCHES:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.stopLauncher();
                    setPathState(CloseAuto.PathState.PREP_PICKUP1);
                }
                break; // stop launch
            // ===================================== Pickup 1 =====================================
            case PREP_PICKUP1:
                follower.followPath(prepPickup1); // prep to collect 1
                artifactSystem.startIntake();
                setPathState(PathState.COLLECT_SPIKE1_ARTIFACT1);
                break;

            case COLLECT_SPIKE1_ARTIFACT1:
                if (!follower.isBusy()) {
                    follower.followPath(collectSpike1Artifact1, 0.25, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.WAIT_BEFORE_SPIKE1_ARTIFACT2);
                }
                break;
            case WAIT_BEFORE_SPIKE1_ARTIFACT2:
                if (!follower.isBusy()) {
                    setPathState(PathState.COLLECT_SPIKE1_ARTIFACT2);
                }
                break;

            case COLLECT_SPIKE1_ARTIFACT2:
                if (pathTimer.getElapsedTimeSeconds() > 0.2) {
                    follower.followPath(collectSpike1Artifact2, 0.25, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.WAIT_BEFORE_SPIKE1_ARTIFACT3);
                }
                break;
            case WAIT_BEFORE_SPIKE1_ARTIFACT3:
                if (!follower.isBusy()) {
                    setPathState(PathState.COLLECT_SPIKE1_ARTIFACT3);
                }
                break;
            case COLLECT_SPIKE1_ARTIFACT3:
                if (pathTimer.getElapsedTimeSeconds() > 0.2) {
                    follower.followPath(collectSpike1Artifact3, 0.25, Constants.followerConstants.automaticHoldEnd);
                    setPathState(PathState.WAIT_AFTER_SPIKE1_ARTIFACT3);
                }
                break;
            case WAIT_AFTER_SPIKE1_ARTIFACT3:
                if (!follower.isBusy()) {
                    setPathState(PathState.SCORE_PICKUP1);
                }
                break;


            case SCORE_PICKUP1:
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    artifactSystem.stopIntake(false);
                    if ((!follower.isBusy()) || (pathTimer.getElapsedTimeSeconds() > 3.0)) {
                        follower.followPath(scorePickup1, 1.0, Constants.followerConstants.automaticHoldEnd);
                        artifactSystem.setLauncherRpm(2200);
                        artifactSystem.startLauncher();
                        setPathState(PathState.SCORE_PICKUP1_WAIT_FOR_DRIVING);
                    }
                }
                break;

            case SCORE_PICKUP1_WAIT_FOR_DRIVING:
                if ((!follower.isBusy()) || (pathTimer.getElapsedTimeSeconds() > 2.0)) {
                    follower.breakFollowing();
                    setPathState(PathState.AUTO_AIM_PICKUP1);
                }
                break;
            case AUTO_AIM_PICKUP1:
                if(autoRotateTowardGoal(isBlueAlliance ? 2 : 1)|| pathTimer.getElapsedTimeSeconds() > 1.0) {
                    stopAutoRotating();
                    setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP1_1);
                }
                break;

            case PREPARE_TO_LAUNCH_PICKUP1_1:
                moveCarouselToNextLaunchPosition(artifact1, PathState.WAIT_FOR_CAROUSEL_PICKUP1_1, PathState.AFTER_PICKUP1_LAUNCHES);

                break;
            case WAIT_FOR_CAROUSEL_PICKUP1_1:
                if (artifactSystem.isCarouselAtTarget()) {
                    setPathState(PathState.LAUNCH_PICKUP1_1);
                }
                break;
            case LAUNCH_PICKUP1_1:
                if  (artifactSystem.isTargetRPMReached()) {
                    if((pathTimer.getElapsedTimeSeconds() > 0.5)) {
                        if (artifactSystem.raiseFlipper()) {
                            setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PICKUP1_2);
                        }
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP1_2:
                moveCarouselToNextLaunchPosition(artifact2, PathState.WAIT_FOR_CAROUSEL_PICKUP1_2, PathState.AFTER_PICKUP1_LAUNCHES);

                break;
            case WAIT_FOR_CAROUSEL_PICKUP1_2:
                if (artifactSystem.isCarouselAtTarget()) {
                    setPathState(PathState.LAUNCH_PICKUP1_2);
                }
                break;
            case LAUNCH_PICKUP1_2:
                if (artifactSystem.isTargetRPMReached()) {
                    if((pathTimer.getElapsedTimeSeconds() > 0.5)) {
                        if (artifactSystem.raiseFlipper()) {
                            setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PICKUP1_3);
                        }
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP1_3:
                moveCarouselToNextLaunchPosition(artifact3, PathState.WAIT_FOR_CAROUSEL_PICKUP1_3, PathState.AFTER_PICKUP1_LAUNCHES);

                break;
            case WAIT_FOR_CAROUSEL_PICKUP1_3:
                if (artifactSystem.isCarouselAtTarget()) {
                    setPathState(PathState.LAUNCH_PICKUP1_3);
                }
                break;
            case LAUNCH_PICKUP1_3:
                if (artifactSystem.isTargetRPMReached()) {
                    if((pathTimer.getElapsedTimeSeconds() > 0.5)) {
                        if (artifactSystem.raiseFlipper()) {
                            setPathState(CloseAuto.PathState.AFTER_PICKUP1_LAUNCHES);
                        }
                    }
                }
                break;
            case AFTER_PICKUP1_LAUNCHES:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.stopLauncher();
                    if (autoMenu.getPickupMiddle()){
                        setPathState(CloseAuto.PathState.PREP_PICKUP2);
                    }
                    else {
                        setPathState(CloseAuto.PathState.PARK);
                    }
                }
                break;
                // ===================================== Pickup 2 =====================================
            case PREP_PICKUP2:
                follower.followPath(prepPickup2,0.8, Constants.followerConstants.automaticHoldEnd); // prep to collect 1
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
                    setPathState(PathState.PARK);
                }
                break;

            // ===================================== PARK =====================================

            case PARK:
                if (pathTimer.getElapsedTimeSeconds() > 1.0){
                  //  follower.followPath(park);
                    artifactSystem.stopIntake(false);
                    setPathState(CloseAuto.PathState.STOP);
                }
                break;
        }
    }

    private void moveCarouselToNextLaunchPosition(ArtifactColor preferredColor, CloseAuto.PathState successPathState, CloseAuto.PathState failedPathState) {
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

    private void setPathState(PathState pathState) {
        this.pathState = pathState;
        pathTimer.resetTimer();
    }

    @Override
    protected String getAutoName() {
        return "Close Auto";
    }

    @Override
    public void start() {
        super.start();
        opmodeTimer.resetTimer();
        setPathState(PathState.PREP_SCAN_OBELISK);
    }

    @Override
    protected void outputTelemetry() {
        telemetry.addData("path state", pathState);

        super.outputTelemetry();
    }
}
