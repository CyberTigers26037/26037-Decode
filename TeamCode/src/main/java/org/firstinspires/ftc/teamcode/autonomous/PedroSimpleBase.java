package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subassembly.MecanumDrive;

public abstract class PedroSimpleBase extends OpMode {
    protected MecanumDrive drive;
    protected Follower follower;

    protected Timer pathTimer, opmodeTimer;

    protected Pose startPose;

    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        drive = new MecanumDrive();
        drive.init(hardwareMap);

        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        follower = Constants.createFollower(hardwareMap);
    }

    protected abstract String getAutoName();

    @Override
    public void start() {
        startPose = getStartPose();
        follower.setStartingPose(startPose);
        buildPaths();
    }

    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        autonomousPathUpdate();

        outputTelemetry();
        telemetry.update();
    }

    protected void outputTelemetry() {
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
    }

    public abstract void buildPaths();

    public abstract void autonomousPathUpdate();

    public abstract Pose getStartPose();
}


