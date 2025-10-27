package org.firstinspires.ftc.teamcode.subassembly;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.config.RobotConfig;

public class ArtifactLauncher {
    private final DcMotorEx flywheelMotor;
    private final Servo flipperServo;
    private double flywheelPower = 0.5;
    private boolean isRunning;
    private static final double MOTOR_TICKS_PER_SECOND = 28.0 * 6000.0/60.0;
    public double getFlywheelPower(){
        return flywheelPower;
    }

    public void adjustFlywheelPower(double amount){
        flywheelPower += amount;
        flywheelPower = Range.clip(flywheelPower,0.1,1.0);
    }

    public ArtifactLauncher(HardwareMap hwMap) {
        flywheelMotor = hwMap.get(DcMotorEx.class, "flywheelMotor");
        flipperServo = hwMap.get(Servo.class, "flipperServo");
        flywheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        parkFlipper();
    }

    public void startFlywheelMotor() {
        flywheelMotor.setVelocity(flywheelPower * MOTOR_TICKS_PER_SECOND);
        isRunning = true;
    }

    public void stopFlywheelMotor() {
        flywheelMotor.setVelocity(0.0);
        isRunning = false;
    }

    public void raiseFlipper() {
        setServoToAngle(flipperServo, RobotConfig.getFlipperRaisedPosition());
    }

    public void parkFlipper() {
        setServoToAngle(flipperServo,RobotConfig.getFlipperParkedPosition());
    }

    public boolean isRunning(){
        return isRunning;
    }

    private static final double SERVO_DEGREES = 270;
    private void setServoToAngle(Servo servo, double degrees) {
        servo.setPosition(Range.scale(degrees, -SERVO_DEGREES/2, SERVO_DEGREES/2, 0, 1));
    }
}


