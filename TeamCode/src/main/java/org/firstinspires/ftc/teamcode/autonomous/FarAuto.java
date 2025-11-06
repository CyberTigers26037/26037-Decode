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

    private final Pose scorePose = new Pose(60, 15, Math.toRadians(110)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
    private final Pose prepPickup3Pose = new Pose(46, 40, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose collectPickup3Pose = new Pose(13, 40, Math.toRadians(180));
    private final Pose collect1Pose = new Pose (19,35, Math.toRadians(180));
    private final Pose prepPickup2Pose = new Pose(50, 60, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose collect2Pose = new Pose(26, 60, Math.toRadians(180));
   // private final Pose prepPickup3Pose = new Pose(50, 36, Math.toRadians(180)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    private final Pose collect3Pose = new Pose(15, 40, Math.toRadians(180));
    public Pose getStartPose(){
        return new Pose(48, 8, Math.toRadians(90)); // Start Pose of our robot.
    }




    /** We do not use this because everything should automatically disable **/

    private Path scorePreload;
    private PathChain prepPickup1, scorePickup1, collectPickup1, prepPickup2, collectPickup2, scorePickup2, prepPickup3, collectPickup3, scorePickup3;
    public void buildPaths() {
        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        prepPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup3Pose.getHeading())
                .build();

        collectPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(prepPickup3Pose, collect3Pose))
                .setLinearHeadingInterpolation(prepPickup3Pose.getHeading(), collect1Pose.getHeading())
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

            case 0:
                AprilTagLimelight.ObeliskOrder obeliskOrder = aprilTagLimeLight.findObeliskArtifactOrder();
                if (obeliskOrder != AprilTagLimelight.ObeliskOrder.NONE) {
                    setObeliskOder(obeliskOrder);
                    setPathState(1);
                }
                else if (pathTimer.getElapsedTimeSeconds() > 3.0){
                    setObeliskOder(AprilTagLimelight.ObeliskOrder.GPP);
                    setPathState(1);
                }
                break;

            case 1:
                follower.followPath(scorePreload);
                artifactSystem.setLauncherRpm(3000);
                artifactSystem.startLauncher();
                setPathState(2);
                break;
            case 2:
                if ((!follower.isBusy()) && (artifactSystem.getActualLauncherRpm() > 2900)) {
                    artifactSystem.moveCarouselToLaunchFirstColor(artifact1);
                    setPathState(3);
                }
                break;
            case 3:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(4);
                }
                break;
            case 4:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.moveCarouselToLaunchFirstColor(artifact2);
                    setPathState(5);
                }
                break;
            case 5:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(6);
                }
                break;
            case 6:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.moveCarouselToLaunchFirstColor(artifact3);
                    setPathState(7);
                }
                break;
            case 7:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(8);
                }
                break;
            case 8:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.stopLauncher();
                    setPathState(9);
                }
                break;
            case 9:
                    follower.followPath(prepPickup3);
                    artifactSystem.startIntake();
                    setPathState(10);

                break;
            case 10:
                if (!follower.isBusy()) {
                    follower.followPath(collectPickup3, 0.2, Constants.followerConstants.automaticHoldEnd);
                    setPathState(11);
                }
                break;
            case 11:
                if (!follower.isBusy()) {
                    artifactSystem.stopIntake();
                    follower.followPath(scorePickup1, 1.0, Constants.followerConstants.automaticHoldEnd);
                    artifactSystem.setLauncherRpm(3000);
                    artifactSystem.startLauncher();
                    setPathState(12);
                }
                break;
            case 12:
                if (!follower.isBusy() && artifactSystem.getActualLauncherRpm()>2900){
                    artifactSystem.moveCarouselToPosition(1);
                    setPathState(13);
                }
                break;
            case 13:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(14);
                }
                break;
            case 14:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.moveCarouselToPosition(2);
                    setPathState(15);
                }
                break;
            case 15:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(16);
                }
                break;
            case 16:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(17);
                }
                break;
            case 17:
                if (!artifactSystem.isFlipperRaised()) {
                    artifactSystem.moveCarouselToPosition(3);
                    setPathState(18);
                }
                break;
            case 18:
                if (artifactSystem.isCarouselAtTarget()) {
                    artifactSystem.raiseFlipper();
                    setPathState(19);
                }
                break;
            case 19:
                if (!artifactSystem.isFlipperRaised()){
                    artifactSystem.stopLauncher();
                    setPathState(20);
                }
                break;
        }
    }






}
