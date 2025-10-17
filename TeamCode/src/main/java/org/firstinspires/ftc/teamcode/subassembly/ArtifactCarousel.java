package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.config.RobotConfig;

public class ArtifactCarousel {
    private final Servo servo;
    private int currentPosition;

    public ArtifactCarousel(HardwareMap hwMap) {
        servo = hwMap.get(Servo.class, "carouselServo");
    }
    public void moveCarouselToIntakePosition(int position) {
        if (position == 1){
            setServoToAngle(servo, RobotConfig.getCarouselIntakePosition1());
        }
        if (position == 2){
            setServoToAngle(servo, RobotConfig.getCarouselIntakePosition2());
        }
        if (position == 3){
            setServoToAngle(servo, RobotConfig.getCarouselIntakePosition3());
        }
        currentPosition = position;

    }


    public void moveCarouselToFirePosition(int position) {
        if (position == 1 ){
            setServoToAngle(servo, RobotConfig.getCarouselLaunchPosition1());
        }
        if(position == 2){
            setServoToAngle(servo, RobotConfig.getCarouselLaunchPosition2());
        }
        if (position ==3){
            setServoToAngle(servo, RobotConfig.getCarouselLaunchPosition3());
        }
        currentPosition = position;
    }



    private static final double SERVO_DEGREES = 270;
    private void setServoToAngle(Servo servo, double degrees ) {
        servo.setPosition(Range.scale(degrees, -SERVO_DEGREES/2, SERVO_DEGREES/2, 0, 1));
}

    public int getCurrentPosition() {
        return currentPosition;
    }
}
