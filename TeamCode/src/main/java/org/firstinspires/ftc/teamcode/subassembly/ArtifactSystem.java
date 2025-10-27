package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArtifactSystem {
    private final ArtifactCarousel carousel;
    private final ArtifactDetector detector;
    private final ArtifactLauncher launcher;
    private final ArtifactIntake intake;
    private final ArtifactTracker tracker;
    private final ArtifactLight light;

    private boolean inDetectionMode;

    private long detectionTimeoutMillis;

    public ArtifactSystem(HardwareMap hwMap) {
        carousel = new ArtifactCarousel(hwMap);
        detector = new ArtifactDetector(hwMap);
        launcher = new ArtifactLauncher(hwMap);
        intake = new ArtifactIntake(hwMap);
        light = new ArtifactLight(hwMap);
        tracker = new ArtifactTracker();
    }

    public void initializeArtifactColors(ArtifactColor position1, ArtifactColor position2, ArtifactColor position3) {
        tracker.reset();
        tracker.loadArtifactAtPosition(1, position1);
        tracker.loadArtifactAtPosition(2, position2);
        tracker.loadArtifactAtPosition(3, position3);
    }

    //Tell Exaveer (sorry spelling) to add meathod to auto. and set preload colors for all times

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
        updateArtifactLight();
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

    public void setLauncherRpm(int rpm){launcher.setFlywheelRpm(rpm);}

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

        updateArtifactLight();
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
        updateArtifactLight();
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
            updateArtifactLight();
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
        updateArtifactLight();
    }

    private void updateArtifactLight () {
        if (carousel.isInLaunchPosition()) {
            ArtifactColor color =
                    tracker.getArtifactAtPosition(carousel.getCurrentPosition());
            light.setColor(color);
        }
        else {
            light.setColor(ArtifactColor.NONE);
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

