package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.config.RobotConfig;

public class ArtifactCarousel {
    private final AxonServo servo;
    private int currentPosition;
    private boolean currentPositionIsALaunchPosition;

    public ArtifactCarousel(HardwareMap hwMap) {
        servo = new AxonServo(hwMap.get(
                Servo.class, "carouselServo"),
                hwMap.get(AnalogInput.class, "carouselServoEncoder"));
    }

    public void moveCarouselToIntakePosition(int position) {
        if (position == 1){
            servo.setTargetAngle(RobotConfig.getCarouselIntakePosition1());
        }
        if (position == 2){
            servo.setTargetAngle(RobotConfig.getCarouselIntakePosition2());
        }
        if (position == 3){
            servo.setTargetAngle(RobotConfig.getCarouselIntakePosition3());
        }
        currentPosition = position;
        currentPositionIsALaunchPosition = false;
    }

    public void moveCarouselToLaunchPosition(int position) {
        if (position == 1 ){
            servo.setTargetAngle(RobotConfig.getCarouselLaunchPosition1());
        }
        if(position == 2){
            servo.setTargetAngle(RobotConfig.getCarouselLaunchPosition2());
        }
        if (position ==3){
            servo.setTargetAngle(RobotConfig.getCarouselLaunchPosition3());
        }
        currentPosition = position;
        currentPositionIsALaunchPosition = true;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean isInLaunchPosition() {
        return currentPositionIsALaunchPosition;
    }

    public boolean isAtTargetPosition() {
        return servo.isAtTarget();
    }
}
