package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class ArtifactCarousel {
    private final Servo servo;
    private int currentPosition;

    public ArtifactCarousel(HardwareMap hwMap) {
        servo = hwMap.get(Servo.class, "carouselServo");
    }
    public void moveCarouselToIntakePosition(int position) {
        if (position == 1){
            setServoToAngle(servo,15.7);
        }
        if (position == 2){
            setServoToAngle(servo, 114.6);
        }
        if (position == 3){
            setServoToAngle(servo,-83.8);
        }
        currentPosition = position;

    }


    public void moveCarouselToFirePosition(int position) {
        if (position == 1 ){
            setServoToAngle(servo,-135);
        }
        if(position == 2){
            setServoToAngle(servo, -34.3);

        }
        if (position ==3){
            setServoToAngle(servo,63.8);
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
