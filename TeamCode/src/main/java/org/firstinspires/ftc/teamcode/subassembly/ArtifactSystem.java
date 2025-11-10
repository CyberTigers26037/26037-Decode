package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Timer;

public class ArtifactSystem {
    private final ArtifactCarousel carousel;
    private final ArtifactDetector detector;
    private final ArtifactLauncher launcher;
    private final ArtifactIntake intake;
    private final ArtifactTracker tracker;
    private final ArtifactLight light;
    private final Timer flipperTimer;

    private boolean inDetectionMode;

    private final Timer detectionTimeoutTimer = new Timer(0.1);

    public ArtifactSystem(HardwareMap hwMap) {
        carousel = new ArtifactCarousel(hwMap);
        detector = new ArtifactDetector(hwMap);
        launcher = new ArtifactLauncher(hwMap);
        intake = new ArtifactIntake(hwMap);
        light = new ArtifactLight(hwMap);
        tracker = new ArtifactTracker();
        flipperTimer = new Timer(0.5);
    }

    public void initializeArtifactColors(ArtifactColor position1, ArtifactColor position2, ArtifactColor position3) {
        tracker.reset();
        tracker.loadArtifactAtPosition(1, position1);
        tracker.loadArtifactAtPosition(2, position2);
        tracker.loadArtifactAtPosition(3, position3);
    }

    //Tell Exaveer (sorry spelling) to add method to auto. and set preload colors for all times

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
        if (!carousel.isInLaunchPosition()) return;
        if (!carousel.isAtTargetPosition()) return;
        if (!launcher.isLauncherAboveMinSpeed()) return;

        launcher.raiseFlipper();
        tracker.removeArtifactFromPosition(carousel.getCurrentPosition());
        updateArtifactLight();
        flipperTimer.start();
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

    public int getActualLauncherRpm() {
        return launcher.getActualFlywheelRpm();
    }

    public void moveCarouselToPosition(int position) {
        if (launcher.isFlipperRaised()) return;

        if (intake.isRunning()){
            carousel.moveCarouselToIntakePosition(position);

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

    public void moveCarouselToLaunchFirstPurple() {
        if (isIntakeRunning()) return;

        int position = tracker.getFirstForArtifactColor(ArtifactColor.PURPLE);
        if (position != 0) {
            moveCarouselToPosition(position);
        }
    }

    public void moveCarouselToLaunchFirstGreen() {
        if (isIntakeRunning()) return;

        int position = tracker.getFirstForArtifactColor(ArtifactColor.GREEN);
        if (position != 0) {
            moveCarouselToPosition(position);
        }
    }

    public void moveCarouselToLaunchFirstColor(ArtifactColor artifactColor) {
        if (isIntakeRunning()) return;

        int position = tracker.getFirstForArtifactColor(artifactColor);
        if (position != 0) {
            moveCarouselToPosition(position);
        }
    }

    public void resetCarouselDetection() {
        if (launcher.isFlipperRaised()) return;

        tracker.reset();
        carousel.moveCarouselToIntakePosition(1);
        inDetectionMode = true;
        detectionTimeoutTimer.stop();
        updateArtifactLight();
    }

    public void loop() {
        if (inDetectionMode && !launcher.isFlipperRaised() && isCarouselAtTarget()) {
            ArtifactColor artifactColor = detector.detectArtifactColor();
            if (artifactColor != ArtifactColor.NONE) {
                tracker.loadArtifactAtPosition(carousel.getCurrentPosition(), artifactColor);
                advanceCarouselInDetectionMode();
            }
            else {
                if (!detectionTimeoutTimer.isRunning()){
                    detectionTimeoutTimer.start();
                }
                if (detectionTimeoutTimer.isExpired()){
                    advanceCarouselInDetectionMode();
                    detectionTimeoutTimer.stop();
                }
            }
            return;
        }

        if (intake.isRunning() && !launcher.isFlipperRaised() && isCarouselAtTarget()) {
            ArtifactColor artifactColor = detector.detectArtifactColor();
            if (artifactColor != ArtifactColor.NONE) {
                tracker.loadArtifactAtPosition(carousel.getCurrentPosition(), artifactColor);
                int emptyArtifactPosition = tracker.getFirstEmptyArtifactPosition();
                if (emptyArtifactPosition != 0) {
                    carousel.moveCarouselToIntakePosition(emptyArtifactPosition);

                }
                else {
                    stopIntake();
                }
            }
            updateArtifactLight();
        }
        if (flipperTimer.isExpired() && launcher.isFlipperRaised()) {
            parkFlipper();
        }
    }

    private void advanceCarouselInDetectionMode() {
        if (carousel.getCurrentPosition() == 1) {
            carousel.moveCarouselToIntakePosition(2);

       }
        else if (carousel.getCurrentPosition() == 2) {
            carousel.moveCarouselToIntakePosition(3);

        }
        else if (carousel.getCurrentPosition() == 3) {
            inDetectionMode = false;
        }
        detectionTimeoutTimer.stop();
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

    public boolean isCarouselAtTarget() {
        return carousel.isAtTargetPosition();
    }

    public boolean isFlipperRaised() {
        return launcher.isFlipperRaised();
    }

    public void outputTelemetry(Telemetry telemetry) {
        telemetry.addData("Position 1", tracker.getArtifactAtPosition(1));
        telemetry.addData("Position 2", tracker.getArtifactAtPosition(2));
        telemetry.addData("Position 3", tracker.getArtifactAtPosition(3));
        telemetry.addData("Flywheel RPM (Target): ", getLauncherRpm());
        telemetry.addData("Flywheel RPM (Actual): ", launcher.getActualFlywheelRpm());
        telemetry.addData("Intake Running: ",   isIntakeRunning());
        telemetry.addData("Launcher Running: ", isLauncherRunning());

        telemetry.addData("Flipper at Target", launcher.isFlipperAtTargetPosition());
        telemetry.addData("Carousel at Target", carousel.isAtTargetPosition());
    }


}

