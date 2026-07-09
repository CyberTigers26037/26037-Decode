package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Navig8rAuto", group = "Pedro")
public class Navig8rAuto extends PedroSimpleBase {
    private enum PathState {
        PATH1,
        PATH2,
        PATH3,
        PATH4,
        PATH5,
        PATH6,
        PATH7,
        PATH8,
        PATH9,
        PATH10,
        PATH11,
        PATH12,
        PARK,
        STOP
    }

    private PathState pathState;

    private PathChain path1;
    private PathChain path2;
    private PathChain path3;
    private PathChain path4;
    private PathChain path5;
    private PathChain path6;
    private PathChain path7;
    private PathChain path8;
    private PathChain path9;
    private PathChain path10;
    private PathChain path11;
    private PathChain path12;
    private PathChain park;


    @Override
    public Pose getStartPose() {
        return new Pose(12, 12, 0);
    }

    @Override
    public void buildPaths() {

        path1 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(12, 12, Math.toRadians(0)),
                        new Pose(36, 12, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();


        path2 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(36, 12, Math.toRadians(0)),
                        new Pose(36, 60, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();
        path3 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(36, 60, Math.toRadians(0)),
                        new Pose(12, 60, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();

        path4 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(12, 60, Math.toRadians(0)),
                        new Pose(12, 132, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();
        path5 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(12, 132, Math.toRadians(0)),
                        new Pose(54, 132, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();

        path6 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(54, 132, Math.toRadians(0)),
                        new Pose(84, 60, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();

        path7 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(84, 60, Math.toRadians(0)),
                        new Pose(84, 84, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();

        path8 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(84, 84, Math.toRadians(0)),
                        new Pose(132, 84, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();

        path9 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(132, 84, Math.toRadians(0)),
                        new Pose(132, 132, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();

        path10 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(132, 132, Math.toRadians(0)),
                        new Pose(108, 132, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();

        path11 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(108, 132, Math.toRadians(0)),
                        new Pose(132, 132, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();

        path12 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(132, 132, Math.toRadians(0)),
                        new Pose(132, 12, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();

        park = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(132, 12, Math.toRadians(0)),
                        new Pose(84, 12, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();
    }

    public void autonomousPathUpdate() {

        switch (pathState) {

            case PATH1:
                if (!follower.isBusy()) {
                    follower.followPath(path2);
                    setPathState(PathState.PATH2);
                }
                break;

            case PATH2:
                if (!follower.isBusy()) {
                    follower.followPath(path3);
                    setPathState(PathState.PATH3);
                }
                break;

            case PATH3:
                if (!follower.isBusy()) {
                    follower.followPath(path4);
                    setPathState(PathState.PATH4);
                }
                break;

            case PATH4:
                if (!follower.isBusy()) {
                    follower.followPath(path5);
                    setPathState(PathState.PATH5);
                }
                break;

            case PATH5:
                if (!follower.isBusy()) {
                    follower.followPath(path6);
                    setPathState(PathState.PATH6);
                }
                break;

            case PATH6:
                if (!follower.isBusy()) {
                    follower.followPath(path7);
                    setPathState(PathState.PATH7);
                }
                break;

            case PATH7:
                if (!follower.isBusy()) {
                    follower.followPath(path8);
                    setPathState(PathState.PATH8);
                }
                break;

            case PATH8:
                if (!follower.isBusy()) {
                    follower.followPath(path9);
                    setPathState(PathState.PATH9);
                }
                break;

            case PATH9:
                if (!follower.isBusy()) {
                    follower.followPath(path10);
                    setPathState(PathState.PATH10);
                }
                break;

            case PATH10:
                if (!follower.isBusy()) {
                    follower.followPath(path11);
                    setPathState(PathState.PATH11);
                }
                break;

            case PATH11:
                if (!follower.isBusy()) {
                    follower.followPath(path12);
                    setPathState(PathState.PATH12);
                }
                break;

            case PATH12:
                if (!follower.isBusy()) {
                    follower.followPath(park);
                    setPathState(PathState.PARK);
                }
                break;

            case PARK:
                if (!follower.isBusy()) {
                    setPathState(PathState.STOP);
                }
                break;

// should loop till 30 seconds is over
            case STOP:
                follower.breakFollowing();
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

        follower.followPath(path1);
        setPathState(PathState.PATH1);
    }

    @Override
    protected String getAutoName() {
        return "Navig8r Auto";
    }

    @Override
    protected void outputTelemetry() {
        telemetry.addData("Path State", pathState);
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));

        super.outputTelemetry();
    }
}
