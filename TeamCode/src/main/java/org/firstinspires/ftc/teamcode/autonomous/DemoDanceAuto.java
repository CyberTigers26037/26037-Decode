package org.firstinspires.ftc.teamcode.autonomous;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subassembly.ArtifactColor;


@SuppressWarnings("unused")
@Configurable
@Autonomous(name= "DanceAuto", group="Pedro")
public class DemoDanceAuto extends PedroAutoBase {
    public static class CloseAutoConfig {
        public ConfigurablePose startPoseBlue = new ConfigurablePose(18, 119, 52);
        public ConfigurablePose startPoseRed  = new ConfigurablePose(125, 119, 126);

        public ConfigurablePose parkPoseBlue = new ConfigurablePose(30, 58, 180);
        public ConfigurablePose parkPoseRed  = new ConfigurablePose(114, 45, 50);

    }

    public static CloseAutoConfig config = new CloseAutoConfig();

    // Spin
    // Stop spinning
    // Change light color
    // Intake
    // Reverse intake
    // Adjust launcher
    // Carousel
    // Attempt launch
    // Park

    private enum PathState {
        SPIN,
        STOP_SPINNING,
        CYCLE_LIGHT,
        INTAKE,
        STOP_INTAKE,
        REVERSE_INTAKE,
        ADJUST_LAUNCHER_CLOSE,
        ADJUST_LAUNCHER_FAR,
        CAROUSEL1,
        CAROUSEL2,
        CAROUSEL3,
        RAISE_FLIPPER,
        LAUNCH,
        PARK,
        STOP
    }

    private PathState pathState;


    public Pose getStartPose() {
        // Start Pose of our robot.
        return isBlueAlliance ? config.startPoseBlue.getPose() : config.startPoseRed.getPose();
    }

    /** We do not use this because everything should automatically disable **/

    private PathChain park;
    public void buildPaths() {
        Pose parkPose = isBlueAlliance ? config.parkPoseBlue.getPose() : config.parkPoseRed.getPose();

        park = follower.pathBuilder()
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case SPIN:
                setPathState(PathState.STOP_SPINNING);
                break;
            case STOP_SPINNING:
                setPathState(PathState.CYCLE_LIGHT);
                break;

            case CYCLE_LIGHT:
                setPathState(PathState.INTAKE);
                break;

            case INTAKE:
                artifactSystem.startIntake();
                setPathState(PathState.STOP_INTAKE);
                break;
            case STOP_INTAKE:
                artifactSystem.stopIntake(false);
                setPathState(PathState.REVERSE_INTAKE);
                break;
            case REVERSE_INTAKE:
                artifactSystem.startReverseIntake();
                setPathState(PathState.ADJUST_LAUNCHER_CLOSE);
                break;

            case ADJUST_LAUNCHER_CLOSE:
                artifactSystem.stopReverseIntake();
                adjustLauncherAngle.adjustCloseAngle();
                setPathState(PathState.ADJUST_LAUNCHER_FAR);
                break;
            case ADJUST_LAUNCHER_FAR:
                adjustLauncherAngle.adjustCloseAngle();
                setPathState(PathState.CAROUSEL1);
                break;

            case CAROUSEL1:
                artifactSystem.moveCarouselToPosition(1);
                setPathState(PathState.CAROUSEL2);
                break;
            case CAROUSEL2:
                artifactSystem.moveCarouselToPosition(2);
                setPathState(PathState.CAROUSEL3);
                break;
            case CAROUSEL3:
                artifactSystem.moveCarouselToPosition(3);
                setPathState(PathState.RAISE_FLIPPER);
                break;

            case RAISE_FLIPPER:
                artifactSystem.raiseFlipper();
                setPathState(PathState.LAUNCH);
                break;
            case LAUNCH:
                artifactSystem.setLauncherRpm(1000);
                artifactSystem.startLauncher();
                setPathState(PathState.PARK);
                break;

            case PARK:
                artifactSystem.stopLauncher();
                setPathState(PathState.STOP);
                break;

        }
    }

    private void moveCarouselToNextLaunchPosition(ArtifactColor preferredColor, DemoDanceAuto.PathState successPathState, DemoDanceAuto.PathState failedPathState) {
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
