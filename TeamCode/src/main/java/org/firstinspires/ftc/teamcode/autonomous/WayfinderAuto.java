package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@SuppressWarnings("unused")
@Autonomous(name = "WayfinderAuto", group = "Pedro")
public class WayfinderAuto extends PedroSimpleBase {


    private enum PathState {
        PATH1,
        PATH2,
        PATH3,
        PATH4,
        PATH5
    }


    private WayfinderAuto.PathState pathState;

    private PathChain path1;
    private PathChain path2;
    private PathChain path3;
    private PathChain path4;
    private PathChain path5;


    @Override
    public Pose getStartPose() {
        return new Pose(60, 18, 0);
    }

    @Override
    public void buildPaths() {

        path1 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(60, 18, 0),
                        new Pose(121, 18, 0)))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(0))
                .build();
        path2 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(121, 18, 0),
                        new Pose(123, 127, Math.toRadians(90))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(0),
                        Math.toRadians(90))
                .build();

        path3 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(123, 127, Math.toRadians(90)),
                        new Pose(18, 126, Math.toRadians(180))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(90),
                        Math.toRadians(180))
                .build();

        path4 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(18, 126, Math.toRadians(180)),
                        new Pose(17, 18, Math.toRadians(90))))
                .setLinearHeadingInterpolation(
                        Math.toRadians(180),
                        Math.toRadians(90))
                .build();

        path5 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(17, 18, Math.toRadians(90)),
                        new Pose(59, 18, 0)))
                .setLinearHeadingInterpolation(
                        Math.toRadians(90),
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




        }
    }



    private void setPathState(WayfinderAuto.PathState pathState) {
        this.pathState = pathState;
        pathTimer.resetTimer();
    }


        @Override
        public void start() {
            super.start();

            follower.followPath(path1);
            setPathState(WayfinderAuto.PathState.PATH1);
        }

        @Override
        protected String getAutoName() {
            return "Way Finder Auto";
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