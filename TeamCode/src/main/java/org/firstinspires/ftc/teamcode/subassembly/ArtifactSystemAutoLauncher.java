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
        STOP
    }
    private ArtifactLaunchingState artifactLaunchingState;

    public ArtifactSystemAutoLauncher(ArtifactSystem artifactSystem) {
        this.artifactSystem = artifactSystem;
    }

    public void launchAllArtifacts() {
        if (!artifactSystem.isReadyForTurboLaunch()) return;
        if (!running) {
            running = true;
            setLaunchingState(ArtifactLaunchingState.START);
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
                launchArtifact(ArtifactLaunchingState.STOP);
                break;

            case STOP:
                artifactSystem.stopLauncher();
                running = false;

                break;
        }
    }

    private void rotateCarouselOrSkip(int position, ArtifactLaunchingState launchState, ArtifactLaunchingState skipState) {
        if (artifactSystem.getArtifactAtPosition(position) != ArtifactColor.NONE) {
            if (artifactSystem.moveCarouselToPosition(position)) {
                setLaunchingState(launchState);
            }
        }
        else {
            setLaunchingState(skipState);
        }
    }

    private void launchArtifact(ArtifactLaunchingState nextState) {
        if (artifactSystem.isCarouselAtTarget() && (artifactSystem.getActualLauncherRpm() > (artifactSystem.getLauncherRpm() - 50))) {
            if (stateTimer.getElapsedSeconds() > 0.5) {
                if (artifactSystem.raiseFlipperTurbo()) {
                    setLaunchingState(nextState);
                }
            }
        }
    }



    public void outputTelemetry(Telemetry telemetry) {
        if (!running) return;

        telemetry.addData("Auto Launching State", artifactLaunchingState);
    }
}
