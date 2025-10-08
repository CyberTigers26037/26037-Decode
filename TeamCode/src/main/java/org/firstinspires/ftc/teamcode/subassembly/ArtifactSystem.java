package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class ArtifactSystem {
    private final ArtifactCarousel carousel;
    private final ArtifactColorSensor detector;
    private final ArtifactLauncher launcher;
    private final ArtifactIntake intake;


    public ArtifactSystem(HardwareMap hwMap) {
        carousel = new ArtifactCarousel(hwMap);
        detector = new ArtifactColorSensor(hwMap);
        launcher = new ArtifactLauncher(hwMap);
        intake = new ArtifactIntake(hwMap);
    }


    public void startIntake() {
        carousel.moveCarouselToIntakePosition(1);
        intake.start();
    }


    public void stopIntake() {
        intake.stop();
    }


    public void toggleIntake() {
        if(!intake.isRunning()){
            intake.start();
        }
        else{
            intake.stop();
        }
        //it is  not running
     }


    public void startLauncher() {
        launcher.startFlywheelMotor();
    }

    public void raiseFlipper() {
        launcher.raiseFlipper();
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
}

