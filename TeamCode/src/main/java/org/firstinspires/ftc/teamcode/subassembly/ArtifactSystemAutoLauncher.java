package org.firstinspires.ftc.teamcode.subassembly;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Timer;

public class ArtifactSystemAutoLauncher {
    private final ArtifactSystem artifactSystem;
    private final Timer stateTimer = new Timer(1.0);
    private boolean running;
    private enum ArtifactLaunchingState {
        START,
        ROTATE_CAROUSEL_1,
        LAUNCH_1,
        ROTATE_CAROUSEL_2,
        LAUNCH_2,
        ROTATE_CAROUSEL_3,
        LAUNCH_3,
        PAUSE_FOR_LAUNCH,
        STOP
    }
    private ArtifactLaunchingState artifactLaunchingState;
    private boolean forceAll;

    public ArtifactSystemAutoLauncher(ArtifactSystem artifactSystem) {
        this.artifactSystem = artifactSystem;
    }

    public void launchAllArtifacts(boolean forceAll) {
        this.forceAll = forceAll;
        if (!artifactSystem.isReadyForTurboLaunch()) return;
        if (!running) {
            running = true;
            setLaunchingState(ArtifactLaunchingState.START);
        }
    }

    public void overrideTurbo(){
        if (running) {
            setLaunchingState(ArtifactLaunchingState.STOP);
        }
    }

    public boolean isRunning() {
        return running;
    }

    private void setLaunchingState(ArtifactLaunchingState artifactLaunchingState) {
        this.artifactLaunchingState = artifactLaunchingState;
        stateTimer.start();
    }

    public void loop() {
        if (!running) return;

        switch (artifactLaunchingState) {
            case START:
                artifactSystem.startLauncher();
                setLaunchingState(ArtifactLaunchingState.ROTATE_CAROUSEL_1);

                break;
            case ROTATE_CAROUSEL_1:
                rotateCarouselOrSkip(1, ArtifactLaunchingState.LAUNCH_1, ArtifactLaunchingState.ROTATE_CAROUSEL_2);

                break;
            case LAUNCH_1:
                launchArtifact(ArtifactLaunchingState.ROTATE_CAROUSEL_2);
                break;

            case ROTATE_CAROUSEL_2:
                rotateCarouselOrSkip(3, ArtifactLaunchingState.LAUNCH_2, ArtifactLaunchingState.ROTATE_CAROUSEL_3);
                break;

            case LAUNCH_2:
                launchArtifact(ArtifactLaunchingState.ROTATE_CAROUSEL_3);
                break;
            case ROTATE_CAROUSEL_3:
                rotateCarouselOrSkip(2, ArtifactLaunchingState.LAUNCH_3, ArtifactLaunchingState.STOP);
                break;
            case LAUNCH_3:
                launchArtifact(ArtifactLaunchingState.PAUSE_FOR_LAUNCH);
                break;
            case PAUSE_FOR_LAUNCH:
                if (stateTimer.getElapsedSeconds() > 1.0) {
                    setLaunchingState(ArtifactLaunchingState.STOP);
                }
                break;

            case STOP:
                artifactSystem.stopLauncher();
                running = false;

                break;
        }
    }

    private void rotateCarouselOrSkip(int position, ArtifactLaunchingState launchState, ArtifactLaunchingState skipState) {
        if ((artifactSystem.getArtifactAtPosition(position) != ArtifactColor.NONE) || forceAll) {
            if (artifactSystem.moveCarouselToPosition(position)) {
                setLaunchingState(launchState);
            }
        }
        else {
            setLaunchingState(skipState);
        }
    }

    private void launchArtifact(ArtifactLaunchingState nextState) {
        if (artifactSystem.isCarouselAtTarget() && (isLauncherRPMAtTarget())) {
            if (stateTimer.getElapsedSeconds() > 0.5) {
                if (artifactSystem.raiseFlipperTurbo()) {
                    setLaunchingState(nextState);
                }
            }
        }
    }

    private boolean isLauncherRPMAtTarget() {
        int delta = Math.abs(artifactSystem.getActualLauncherRpm() - artifactSystem.getLauncherRpm());
        return (delta < 20);
    }

    public void outputTelemetry(Telemetry telemetry) {
        if (!running) return;

        telemetry.addData("Auto Launching State", artifactLaunchingState);
    }
}
