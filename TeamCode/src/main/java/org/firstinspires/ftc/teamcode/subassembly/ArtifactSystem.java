package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArtifactSystem {
    private final ArtifactCarousel carousel;
    private final ArtifactDetector detector;
    private final ArtifactLauncher launcher;
    private final ArtifactIntake intake;
    private final ArtifactTracker tracker;

    private boolean inDetectionMode;

    private long detectionTimeoutMillis;

    public ArtifactSystem(HardwareMap hwMap) {
        carousel = new ArtifactCarousel(hwMap);
        detector = new ArtifactDetector(hwMap);
        launcher = new ArtifactLauncher(hwMap);
        intake = new ArtifactIntake(hwMap);
        tracker = new ArtifactTracker();
    }

    public void startIntake() {
        intake.start();
        moveCarouselToPosition(1);
    }

    public void stopIntake() {
        intake.stop();
    }

    public void toggleIntake() {
        if (!intake.isRunning()) {
            startIntake();
        } else {
            stopIntake();
        }
    }

    public void startLauncher() {
        launcher.startFlywheelMotor();
    }

    public void raiseFlipper() {
        launcher.raiseFlipper();
        tracker.removeArtifactFromPosition(carousel.getCurrentPosition());
    }

    public void parkFlipper() {
        launcher.parkFlipper();
    }

    public void stopLauncher() {
        launcher.stopFlywheelMotor();
    }

    public void adjustLauncherRpm(int amount) {
        launcher.adjustFlywheelRpm(amount);
    }

    public int getLauncherRpm() {
        return launcher.getFlywheelRpm();
    }

    public void moveCarouselToPosition(int position) {
        if (intake.isRunning()){
            carousel.moveCarouselToIntakePosition(position);
            detector.tempStopDetection();
        }
        else{
            carousel.moveCarouselToLaunchPosition(position);
        }
    }

    public boolean isIntakeRunning() {
        return intake.isRunning();
    }

    public boolean isLauncherRunning() {
        return launcher.isRunning();
    }

    public void moveCarouselToFireFirstPurple() {
        if (isIntakeRunning()) return;

        int position = tracker.getFirstForArtifactColor(ArtifactColor.PURPLE);
        if (position != 0) {
            moveCarouselToPosition(position);
        }
    }

    public void moveCarouselToFireFirstGreen() {
        if (isIntakeRunning()) return;

        int position = tracker.getFirstForArtifactColor(ArtifactColor.GREEN);
        if (position != 0) {
            moveCarouselToPosition(position);
        }
    }

    public void resetCarouselDetection() {
        tracker.reset();
        carousel.moveCarouselToIntakePosition(1);
        detector.tempStopDetection();
        inDetectionMode = true;
        detectionTimeoutMillis = System.currentTimeMillis() + 1000;
    }

    public void loop() {
        if (inDetectionMode) {
            ArtifactColor artifactColor = detector.detectArtifactColor();
            if (artifactColor != ArtifactColor.NONE) {
                tracker.loadArtifactAtPosition(carousel.getCurrentPosition(), artifactColor);
                advanceCarouselInDetectionMode();
            }
            else if (System.currentTimeMillis() > detectionTimeoutMillis) {
                advanceCarouselInDetectionMode();
            }
            return;
        }

        if (intake.isRunning()) {
            ArtifactColor artifactColor = detector.detectArtifactColor();
            if (artifactColor != ArtifactColor.NONE) {
                tracker.loadArtifactAtPosition(carousel.getCurrentPosition(), artifactColor);
                int emptyArtifactPosition = tracker.getFirstEmptyArtifactPosition();
                if (emptyArtifactPosition != 0) {
                    carousel.moveCarouselToIntakePosition(emptyArtifactPosition);
                    detector.tempStopDetection();
                }
                else {
                    stopIntake();
                }
            }
        }
    }

    private void advanceCarouselInDetectionMode() {
        if (carousel.getCurrentPosition() == 1) {
            carousel.moveCarouselToIntakePosition(2);
            detector.tempStopDetection();
            detectionTimeoutMillis = System.currentTimeMillis() + 1000;
        }
        else if (carousel.getCurrentPosition() == 2) {
            carousel.moveCarouselToIntakePosition(3);
            detector.tempStopDetection();
            detectionTimeoutMillis = System.currentTimeMillis() + 1000;
        }
        else if (carousel.getCurrentPosition() == 3) {
            inDetectionMode = false;
        }
    }

    public void outputTelemetry(Telemetry telemetry) {
        telemetry.addData("Position 1", tracker.getArtifactAtPosition(1));
        telemetry.addData("Position 2", tracker.getArtifactAtPosition(2));
        telemetry.addData("Position 3", tracker.getArtifactAtPosition(3));
        telemetry.addData("Flywheel RPM (Target): ", getLauncherRpm());
        telemetry.addData("Flywheel RPM (Actual): ", launcher.getActualFlywheelRpm());
        telemetry.addData("Intake Running: ",   isIntakeRunning());
        telemetry.addData("Launcher Running: ", isLauncherRunning());
    }
}

