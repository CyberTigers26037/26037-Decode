package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArtifactSystem {
    private final ArtifactCarousel carousel;
    private final ArtifactColorSensor detector;
    private final ArtifactLauncher launcher;
    private final ArtifactIntake intake;
    private final ArtifactTracker tracker;


    public ArtifactSystem(HardwareMap hwMap) {
        carousel = new ArtifactCarousel(hwMap);
        detector = new ArtifactColorSensor(hwMap);
        launcher = new ArtifactLauncher(hwMap);
        intake = new ArtifactIntake(hwMap);
        tracker = new ArtifactTracker();
    }




    public void startIntake() {
        carousel.moveCarouselToIntakePosition(1);
        detector.tempStopDetection();
        intake.start();
    }


    public void stopIntake() {
        intake.stop();
    }


    public void toggleIntake() {
        if(!intake.isRunning()){
            startIntake();
        }
        else{
            stopIntake();
        }
        //it is  not running
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

    public void adjustLauncherPower(double amount) {
        launcher.adjustFlywheelPower(amount);
    }

    public double getLauncherPower() {
        return launcher.getFlywheelPower();
    }


    public void moveCarouselToPosition(int position) {
        if (intake.isRunning()){
            carousel.moveCarouselToIntakePosition(position);
            detector.tempStopDetection();
        }
        else{
            carousel.moveCarouselToFirePosition(position);
        }
        // is running and move the carousel to a fire position if the intake is not running
    }


    public boolean isIntakeRunning() {
        return intake.isRunning();
    }


    public boolean isLauncherRunning() {
        return launcher.isRunning();

        // NOTE: to complete this method, you will need to add a method on the
        // ArtifactLauncher class called isRunning(), similar to the one in our
        // ArtifactIntake class.
    }

    public void loop() {
        if (intake.isRunning()) {
            ArtifactColor artifactColor = detector.detectArtifactColor();
            if (artifactColor != ArtifactColor.NONE){
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

    public void outputTelemetry(Telemetry telemetry) {
        telemetry.addData("Position 1", tracker.getArtifactAtPosition(1));
        telemetry.addData("Position 2", tracker.getArtifactAtPosition(2));
        telemetry.addData("Position 3", tracker.getArtifactAtPosition(3));
        telemetry.addData("Fly Wheel Power: ",  getLauncherPower());
        telemetry.addData("Intake Running: ",   isIntakeRunning());
        telemetry.addData("Launcher Running: ", isLauncherRunning());

    }
}

