package org.firstinspires.ftc.teamcode.autonomous;



import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subassembly.AprilTagLimelight;
import org.firstinspires.ftc.teamcode.subassembly.ArtifactColor;

@SuppressWarnings("unused")
@Autonomous(name= "CloseAuto", group="Pedro")
public class CloseAuto extends PedroAutoBase {
    private enum PathState {
        PREP_SCAN_OBELISK,
        DRIVE_TO_SCAN,
        DETECT_OBELISK,
        SCORE_PRELOAD,
        SCORE_PRELOAD_AIM,
        AUTO_AIM_PRELOAD,
        PREPARE_TO_LAUNCH_PRELOAD1,
        LAUNCH_PRELOAD1,
        PREPARE_TO_LAUNCH_PRELOAD2,
        LAUNCH_PRELOAD2,
        PREPARE_TO_LAUNCH_PRELOAD3,
        LAUNCH_PRELOAD3,
        AFTER_PRELOAD_LAUNCHES,
        PREP_PICKUP1, // pick up 1st set ---------------
        HALF_PICKUP1,
        COLLECT_PICKUP1,
        SCORE_PICKUP1,
        SCORE_PICKUP1_WAIT_FOR_DRIVING,
        AUTO_AIM_PICKUP1,
        PREPARE_TO_LAUNCH_PICKUP1_1,
        LAUNCH_PICKUP1_1,
        PREPARE_TO_LAUNCH_PICKUP1_2,
        LAUNCH_PICKUP1_2,
        PREPARE_TO_LAUNCH_PICKUP1_3,
        LAUNCH_PICKUP1_3,
        AFTER_PICKUP1_LAUNCHES,
        PARK,
        STOP
    }

    private PathState pathState;


    public Pose getStartPose() {
        // Start Pose of our robot.
        return isBlueAlliance ?
                new Pose(18.5, 119, Math.toRadians(54)) : // blue
                new Pose(125.5, 119, Math.toRadians(126));  // red
    }




    /** We do not use this because everything should automatically disable **/

    private PathChain prepScan, prepPickup1, halfPickup1, collectPickup1, scorePickup1, park;
    public void buildPaths() {
        Pose scorePose = isBlueAlliance ? // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
                new Pose(48, 100, Math.toRadians(135)) : // blue
                new Pose(96, 100, Math.toRadians(45));   // red
        Pose scanObeliskPose = isBlueAlliance ?
                new Pose (48, 100, Math.toRadians(70)) : // blue
                new Pose (96, 100, Math.toRadians(110)); // red
        Pose prepPickup1Pose = isBlueAlliance ? // Highest (First Set) of Artifacts from the Spike Mark.
                new Pose(48, 92, Math.toRadians(180)) :  // blue
                new Pose(96, 92, Math.toRadians(0));     // red
        Pose halfPickup1Pose = isBlueAlliance ?
                new Pose(30, 92, Math.toRadians(180)) :  // blue
                new Pose(114, 92, Math.toRadians(0));    // red
        Pose collect1Pose = isBlueAlliance ?
                new Pose(18, 92, Math.toRadians(180)) : // blue
                new Pose(126, 92, Math.toRadians(0));  // red
        Pose parkPose = isBlueAlliance ?
                new Pose(30, 92, Math.toRadians(180)) : // blue
                new Pose(114, 92, Math.toRadians(0));  // red


        prepScan = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scanObeliskPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scanObeliskPose.getHeading())
                .build();

        prepPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup1Pose.getHeading())
                .build();

        halfPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(prepPickup1Pose, halfPickup1Pose))
                .setLinearHeadingInterpolation(prepPickup1Pose.getHeading(), halfPickup1Pose.getHeading())
                .build();

        collectPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(halfPickup1Pose, collect1Pose))
                .setLinearHeadingInterpolation(halfPickup1Pose.getHeading(), collect1Pose.getHeading())
                .build();

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(collect1Pose, scorePose))
                .setLinearHeadingInterpolation(collect1Pose.getHeading(), scorePose.getHeading())
                .build();

        park = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, parkPose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading())
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
                }
                else if (pathTimer.getElapsedTimeSeconds() > 3.0){
                    setObeliskOder(AprilTagLimelight.ObeliskOrder.GPP);
                    setPathState(CloseAuto.PathState.SCORE_PRELOAD);
                }
                break;

            case SCORE_PRELOAD:
                beginDetectingGoal();
                follower.turnToDegrees( isBlueAlliance ? 145 : 35);
                artifactSystem.setLauncherRpm(2420);
                artifactSystem.startLauncher();
                setPathState(CloseAuto.PathState.SCORE_PRELOAD_AIM);
                break;

            case SCORE_PRELOAD_AIM:
                if ((!follower.isBusy()) || (pathTimer.getElapsedTimeSeconds() > 3.0)) {
                    follower.breakFollowing();
                    setPathState(PathState.AUTO_AIM_PRELOAD);
                }
                break;
            case AUTO_AIM_PRELOAD:
                if(autoRotateTowardGoal(0) || pathTimer.getElapsedTimeSeconds() > 2.0) {
                    stopAutoRotating();
                    setPathState(PathState.PREPARE_TO_LAUNCH_PRELOAD1);
                }
                break;

            case PREPARE_TO_LAUNCH_PRELOAD1:
                moveCarouselToNextLaunchPosition(artifact1, PathState.LAUNCH_PRELOAD1, PathState.AFTER_PRELOAD_LAUNCHES);
                break;
            case LAUNCH_PRELOAD1:
                if (artifactSystem.isCarouselAtTarget() && artifactSystem.getActualLauncherRpm() > 2400) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PRELOAD2);
                    }
                }
                break; // launched 1st artifact

            case PREPARE_TO_LAUNCH_PRELOAD2:
                moveCarouselToNextLaunchPosition(artifact2, PathState.LAUNCH_PRELOAD2, PathState.AFTER_PRELOAD_LAUNCHES);

                break;
            case LAUNCH_PRELOAD2:
                if (artifactSystem.isCarouselAtTarget() && artifactSystem.getActualLauncherRpm() > 2400) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PRELOAD3);
                    }
                }
                break; // launched 2nd artifact

            case PREPARE_TO_LAUNCH_PRELOAD3:
                moveCarouselToNextLaunchPosition(artifact3, PathState.LAUNCH_PRELOAD3, PathState.AFTER_PRELOAD_LAUNCHES);

                break;
            case LAUNCH_PRELOAD3:
                if (artifactSystem.isCarouselAtTarget()) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(CloseAuto.PathState.AFTER_PRELOAD_LAUNCHES);
                    }
                }
                break; // Launched 3rd artifact
            case AFTER_PRELOAD_LAUNCHES:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.stopLauncher();
                    setPathState(CloseAuto.PathState.PREP_PICKUP1);
                }
                break; // stop launch
            case PREP_PICKUP1:
                follower.followPath(prepPickup1); // prep to collect 1
                artifactSystem.startIntake();
                setPathState(CloseAuto.PathState.HALF_PICKUP1);
                break;

            case HALF_PICKUP1:
                if (!follower.isBusy()) {
                    follower.followPath(halfPickup1, 0.2, Constants.followerConstants.automaticHoldEnd);
                    setPathState(CloseAuto.PathState.COLLECT_PICKUP1);
                }
                break;

            case COLLECT_PICKUP1:
                if (!follower.isBusy()) {
                    follower.followPath(collectPickup1, 0.2, Constants.followerConstants.automaticHoldEnd);
                    setPathState(CloseAuto.PathState.SCORE_PICKUP1);
                }
                break;

            case SCORE_PICKUP1:
                if ((!follower.isBusy()) && (pathTimer.getElapsedTimeSeconds() > 2.5)) {
                    artifactSystem.stopIntake(false);
                    follower.followPath(scorePickup1, 1.0, Constants.followerConstants.automaticHoldEnd);
                    artifactSystem.setLauncherRpm(2420);
                    artifactSystem.startLauncher();
                    setPathState(PathState.SCORE_PICKUP1_WAIT_FOR_DRIVING);
                }
                break;

            case SCORE_PICKUP1_WAIT_FOR_DRIVING:
                if (!follower.isBusy()) {
                    follower.breakFollowing();
                    setPathState(PathState.AUTO_AIM_PICKUP1);
                }
                break;
            case AUTO_AIM_PICKUP1:
                if(autoRotateTowardGoal(0) || pathTimer.getElapsedTimeSeconds() > 2.0) {
                    stopAutoRotating();
                    setPathState(PathState.PREPARE_TO_LAUNCH_PICKUP1_1);
                }
                break;

            case PREPARE_TO_LAUNCH_PICKUP1_1:
                moveCarouselToNextLaunchPosition(artifact1, PathState.LAUNCH_PICKUP1_1, PathState.AFTER_PICKUP1_LAUNCHES);

                break;
            case LAUNCH_PICKUP1_1:
                if (artifactSystem.isCarouselAtTarget() && artifactSystem.getActualLauncherRpm() > 2300) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PICKUP1_2);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP1_2:
                moveCarouselToNextLaunchPosition(artifact2, PathState.LAUNCH_PICKUP1_2, PathState.AFTER_PICKUP1_LAUNCHES);

                break;
            case LAUNCH_PICKUP1_2:
                if (artifactSystem.isCarouselAtTarget() && artifactSystem.getActualLauncherRpm() > 2300) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(CloseAuto.PathState.PREPARE_TO_LAUNCH_PICKUP1_3);
                    }
                }
                break;
            case PREPARE_TO_LAUNCH_PICKUP1_3:
                moveCarouselToNextLaunchPosition(artifact3, PathState.LAUNCH_PICKUP1_3, PathState.AFTER_PICKUP1_LAUNCHES);

                break;
            case LAUNCH_PICKUP1_3:
                if (artifactSystem.isCarouselAtTarget() && artifactSystem.getActualLauncherRpm() > 2300) {
                    if (artifactSystem.raiseFlipper()) {
                        setPathState(CloseAuto.PathState.AFTER_PICKUP1_LAUNCHES);
                    }
                }
                break;
            case AFTER_PICKUP1_LAUNCHES:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.stopLauncher();
                    setPathState(CloseAuto.PathState.PARK);
                }
                break;
            case PARK:
                if (!follower.isBusy()) {
                    follower.followPath(park);
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
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(PathState.PREP_SCAN_OBELISK);
    }

    @Override
    protected void outputTelemetry() {
        telemetry.addData("path state", pathState);

        super.outputTelemetry();
    }
}
