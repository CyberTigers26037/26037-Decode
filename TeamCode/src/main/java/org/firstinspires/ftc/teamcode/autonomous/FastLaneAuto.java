package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "FastLaneAuto", group = "Pedro")
public class FastLaneAuto extends PedroSimpleBase {

    private enum PathState {
        PATH1,
        CURVE_PATH1,
        PATH2,
        PATH3,
        CURVE_PATH2,
        PATH4,
        PATH5,
        CURVE_PATH3,
        PATH6,
        PATH7,
        PATH8,
        PATH9,
        STOP
    }

    private PathState pathState;

    private PathChain path1;
    private PathChain curvePath1;
    private PathChain path2;
    private PathChain path3;
    private PathChain curvePath2;
    private PathChain path4;
    private PathChain path5;
    private PathChain curvePath3;
    private PathChain path6;
    private PathChain path7;
    private PathChain path8;
    private PathChain path9;

    @Override
    public Pose getStartPose() {
        return new Pose(60, 18, 0);
    }

    @Override
    public void buildPaths() {

        path1 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(60, 18, Math.toRadians(0)),
                        new Pose(96, 18, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();

        curvePath1 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(96, 18, Math.toRadians(0)),
                        new Pose(120, 36, Math.toRadians(90))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(90))
                .build();

        path2 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(120, 36, Math.toRadians(90)),
                        new Pose(96, 60, Math.toRadians(180))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(90),
                        Math.toRadians(180))
                .build();
// U-turn 1 ^^^^ complete
        path3 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(96, 60, Math.toRadians(180)),
                        new Pose(84, 60, Math.toRadians(180))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(180),
                        Math.toRadians(180))
                .build();

        curvePath2 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(84, 60, Math.toRadians(180)),
                        new Pose(60, 75, Math.toRadians(90))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(180),
                        Math.toRadians(90))
                .build();

        path4 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(60, 75, Math.toRadians(90)),
                        new Pose(84, 90, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(90),
                        Math.toRadians(0))
                .build();
// U-Turn 2 ^^^^ need to test
        path5 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(84, 90, Math.toRadians(0)),
                        new Pose(96, 90, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();

        curvePath3 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(96, 90, Math.toRadians(0)),
                        new Pose(120, 108, Math.toRadians(90))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(90))
                .build();

        path6 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(120, 108, Math.toRadians(90)),
                        new Pose(96, 126, Math.toRadians(180))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(90),
                        Math.toRadians(180))
                .build();
// U-Turn 3 ^^^ need to test
        path7 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(96, 126, Math.toRadians(180)),
                        new Pose(30, 126, Math.toRadians(180))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(180),
                        Math.toRadians(180))
                .build();

        path8 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(30, 126, Math.toRadians(180)),
                        new Pose(30, 18, Math.toRadians(270))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(180),
                        Math.toRadians(270))
                .build();

        path9 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(30, 18, Math.toRadians(270)),
                        new Pose(60, 18, Math.toRadians(0))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(270),
                        Math.toRadians(0))
                .build();
    }

    public void autonomousPathUpdate() {

        switch (pathState) {

            case PATH1:
                if (!follower.isBusy()) {
                    follower.followPath(curvePath1);
                    setPathState(PathState.CURVE_PATH1);
                }
                break;

            case CURVE_PATH1:
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
                    follower.followPath(curvePath2);
                    setPathState(PathState.CURVE_PATH2);
                }
                break;

            case CURVE_PATH2:
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
                    follower.followPath(curvePath3);
                    setPathState(PathState.CURVE_PATH3);
                }
                break;

            case CURVE_PATH3:
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
                    setPathState(PathState.PATH1);
                }
                break;

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
        return "Fast Lane Auto";
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