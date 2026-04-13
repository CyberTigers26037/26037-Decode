package org.firstinspires.ftc.teamcode.autonomous;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subassembly.ArtifactColor;


@SuppressWarnings("unused")
@Configurable
@Autonomous(name= "DanceAuto", group="Pedro")
public class DemoDanceAuto extends PedroAutoBase {
    public static class CloseAutoConfig {
        public ConfigurablePose startPose = new ConfigurablePose(0, 0, 0);
    }

    public static CloseAutoConfig config = new CloseAutoConfig();

    private enum PathState {
        SPIN1_1,
        SPIN1_2,
        STOP_SPINNING,
        CYCLE_LIGHT1,
        CYCLE_LIGHT2,
        CYCLE_LIGHT3,
        STOP_CYCLE_LIGHT,
        INTAKE,
        STOP_INTAKE,
        REVERSE_INTAKE,
        ADJUST_LAUNCHER_FAR,
        ADJUST_LAUNCHER_CLOSE,
        CAROUSEL1,
        CAROUSEL2,
        CAROUSEL3,
        RAISE_FLIPPER,
        PARK_FLIPPER,
        LAUNCH,
        PARK,
        STOP
    }

    private PathState pathState;
    private Servo rgbLight;

    private static final double OFF = 0.0;
    private static final double BLUE = 0.611;
    private static final double RED = 0.279;

    @Override
    public void init() {
        super.init();
        rgbLight = hardwareMap.get(Servo.class, "artifactLight");
    }

    public Pose getStartPose() {
        return config.startPose.getPose();
    }

    public void buildPaths() {

    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case SPIN1_1:
                    follower.turnDegrees(180, false);
                    setPathState(PathState.SPIN1_2);
                break;
            case SPIN1_2:
                if ((!follower.isBusy()) || (pathTimer.getElapsedTimeSeconds() > 4.0)) {
                    follower.turnDegrees(180, false);
                    setPathState(PathState.STOP_SPINNING);
                }
                break;
            case STOP_SPINNING:
                if ((!follower.isBusy()) || (pathTimer.getElapsedTimeSeconds() > 4.0)) {
                    setPathState(PathState.CYCLE_LIGHT1);
                }
                break;

            case CYCLE_LIGHT1:
                    rgbLight.setPosition(RED);
                    setPathState(PathState.CYCLE_LIGHT2);
                break;
            case CYCLE_LIGHT2:
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    rgbLight.setPosition(BLUE);
                    setPathState(PathState.CYCLE_LIGHT3);
                }
                break;
            case CYCLE_LIGHT3:
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    rgbLight.setPosition(RED);
                    setPathState(PathState.STOP_CYCLE_LIGHT);
                }
                break;
            case STOP_CYCLE_LIGHT:
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    rgbLight.setPosition(OFF);
                    setPathState(PathState.INTAKE);
                }
                break;

            case INTAKE:
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    artifactSystem.startIntake();
                    setPathState(PathState.STOP_INTAKE);
                }
                break;
            case STOP_INTAKE:
                if (pathTimer.getElapsedTimeSeconds() > 2.0) {
                    artifactSystem.stopIntake(false);
                    setPathState(PathState.REVERSE_INTAKE);
                }
                break;
            case REVERSE_INTAKE:
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    artifactSystem.startReverseIntake();
                    setPathState(PathState.ADJUST_LAUNCHER_FAR);
                }
                break;

            case ADJUST_LAUNCHER_FAR:
                if (pathTimer.getElapsedTimeSeconds() > 2.0) {
                    artifactSystem.stopReverseIntake();
                    adjustLauncherAngle.adjustFarAngle();
                    setPathState(PathState.ADJUST_LAUNCHER_CLOSE);
                }
                break;
            case ADJUST_LAUNCHER_CLOSE:
                if (pathTimer.getElapsedTimeSeconds() > 2.0) {
                    adjustLauncherAngle.adjustCloseAngle();
                    setPathState(PathState.CAROUSEL1);
                }
                break;

            case CAROUSEL1:
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    artifactSystem.moveCarouselToPosition(1);
                    setPathState(PathState.CAROUSEL2);
                }
                break;
            case CAROUSEL2:
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    artifactSystem.moveCarouselToPosition(2);
                    setPathState(PathState.CAROUSEL3);
                }
                break;

            case CAROUSEL3:
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    artifactSystem.moveCarouselToPosition(3);
                    setPathState(PathState.RAISE_FLIPPER);
                }
                break;

            case RAISE_FLIPPER:
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    artifactSystem.raiseFlipper();
                    setPathState(PathState.LAUNCH);
                }
                break;
            case PARK_FLIPPER:
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    artifactSystem.parkFlipper();
                    setPathState(PathState.LAUNCH);
                }
                break;

            case LAUNCH:
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    artifactSystem.setLauncherRpm(2000);
                    artifactSystem.startLauncher();
                    setPathState(PathState.PARK);
                }
                break;

            case PARK:
                if (pathTimer.getElapsedTimeSeconds() > 4.0) {
                    artifactSystem.stopLauncher();
                    setPathState(PathState.SPIN1_1);
                }
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
        return "Dance Auto";
    }

    @Override
    public void start() {
        super.start();
        opmodeTimer.resetTimer();
        setPathState(PathState.SPIN1_1);
    }

    @Override
    protected void outputTelemetry() {
        telemetry.addData("path state", pathState);

        super.outputTelemetry();
    }
}
