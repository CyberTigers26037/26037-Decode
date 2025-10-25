package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subassembly.ArtifactSystem;

@SuppressWarnings("unused")
@Autonomous(name= "Decode Auto", group="Pedro")
public class PedroAuto extends OpMode {
    private Follower follower;
    private ArtifactSystem artifactSystem;
    private Timer pathTimer, opmodeTimer;
    private int pathState;

    private final Pose startPose = new Pose(48, 8, Math.toRadians(90)); // Start Pose of our robot.
    private final Pose scorePose = new Pose(60, 84, Math.toRadians(135)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
    private final Pose prepPickup1Pose = new Pose(50, 84, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose collect1Pose = new Pose(26, 84, Math.toRadians(180));
    private final Pose prepPickup2Pose = new Pose(50, 60, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose collect2Pose = new Pose(26, 60, Math.toRadians(180));
    private final Pose prepPickup3Pose = new Pose(50, 36, Math.toRadians(180)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    private final Pose collect3Pose = new Pose(26, 36, Math.toRadians(180));


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
            case 0:
                follower.followPath(scorePreload);
                setPathState(1);
                break;
            case 1:
                if (!follower.isBusy()) {
                    artifactSystem.startLauncher();
                    artifactSystem.moveCarouselToPosition(1);
                    setPathState(2);
                }
                break;
            case 2:
                if (pathTimer.getElapsedTimeSeconds() > 2.0) {
                    artifactSystem.raiseFlipper();
                    setPathState(3);
                }
                break;
            case 3:
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    artifactSystem.parkFlipper();
                    setPathState(4);
                }
                break;
            case 4:
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    artifactSystem.moveCarouselToPosition(2);
                    setPathState(5);
                }
                break;
            case 5:
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    artifactSystem.raiseFlipper();
                    setPathState(6);
                }
                break;
            case 6:
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    artifactSystem.parkFlipper();
                    setPathState(7);
                }
                break;
            case 7:
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    artifactSystem.moveCarouselToPosition(3);
                    setPathState(8);
                }
                break;
            case 8:
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    artifactSystem.raiseFlipper();
                    setPathState(9);
                }
                break;
            case 9:
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    artifactSystem.parkFlipper();
                    setPathState(10);
                }
                break;
            case 10:
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    artifactSystem.stopLauncher();
                    setPathState(11);
                }
                break;
//            case 1:
//
//            /* You could check for
//            - Follower State: "if(!follower.isBusy()) {}"
//            - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
//            - Robot Position: "if(follower.getPose().getX() > 36) {}"
//            */
//
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                if(!follower.isBusy()) {
//                    /* Score Preload */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
//                    follower.followPath(prepPickup1,true);
//                    setPathState(2);
//                }
//                break;
//            case 2:
//                if(!follower.isBusy()) {
//                    /* Grab Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                    follower.followPath(collectPickup1,true);
//                    setPathState(3);
//                }
//                break;
//            case 3:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
//                if(!follower.isBusy()) {
//                    /* Grab Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                    follower.followPath(scorePickup1,true);
//                    setPathState(4);
//                }
//                break;
//            case 4:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                if(!follower.isBusy()) {
//                    /* Score Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
//                    follower.followPath(prepPickup2,true);
//                    setPathState(5);
//                }
//                break;
//            case 5:
//                if(!follower.isBusy()) {
//                    /* Grab Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                    follower.followPath(collectPickup2,true);
//                    setPathState(6);
//                }
//                break;
//            case 6:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup2Pose's position */
//                if(!follower.isBusy()) {
//                    /* Grab Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                    follower.followPath(scorePickup2,true);
//                    setPathState(7);
//                }
//                break;
//            case 7:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                if(!follower.isBusy()) {
//                    /* Score Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
//                    follower.followPath(prepPickup3,true);
//                    setPathState(8);
//                }
//                break;
//            case 8:
//                if(!follower.isBusy()) {
//                    /* Grab Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                    follower.followPath(collectPickup3,true);
//                    setPathState(9);
//                }
//                break;
//            case 9:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
//                if(!follower.isBusy()) {
//                    /* Grab Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                    follower.followPath(scorePickup3, true);
//                    setPathState(10);
//                }
//                break;
//            case 10:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                if(!follower.isBusy()) {
//                    /* Set the state to a Case we won't use or define, so it just stops running an new paths */
//                    setPathState(-1);
//                }
//                break;
        }
    }

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        artifactSystem = new ArtifactSystem(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);
    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {}

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {}
}
