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
    private int flywheelRpm = 3000;
    private boolean isRunning;
    private static final int MIN_RPM =  100;
    private static final int MAX_RPM = 6000;
    private static final double MOTOR_TICKS_PER_REVOLUTION = 28;
    private static final double SECONDS_PER_MINUTE = 60;

    public int getFlywheelRpm(){
        return flywheelRpm;
    }

    public void adjustFlywheelRpm(int amount){
        flywheelRpm += amount;
        flywheelRpm = Range.clip(flywheelRpm, MIN_RPM, MAX_RPM);
    }

    public ArtifactLauncher(HardwareMap hwMap) {
        flywheelMotor = hwMap.get(DcMotorEx.class, "flywheelMotor");
        flywheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheelMotor.setVelocityPIDFCoefficients(260, 16, 26, 0);

        flipperServo = hwMap.get(Servo.class, "flipperServo");
        parkFlipper();
    }

    private double rpmToTps(int rpm) {
        // Converts revolutions per minute (RPM) to ticks per second (TPS)
        return rpm * MOTOR_TICKS_PER_REVOLUTION / SECONDS_PER_MINUTE;
    }

    private int tpsToRpm(double tps) {
        // Converts ticks per second (TPS) to revolutions per minute (RPM)
        return (int)(tps / MOTOR_TICKS_PER_REVOLUTION * SECONDS_PER_MINUTE);
    }

    public void startFlywheelMotor() {
        flywheelMotor.setVelocity(rpmToTps(flywheelRpm));
        isRunning = true;
    }

    public void stopFlywheelMotor() {
        flywheelMotor.setVelocity(0.0);
        isRunning = false;
    }

    public int getActualFlywheelRpm() {
        return tpsToRpm(flywheelMotor.getVelocity());
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

    public void setFlywheelRpm(int rpm) {
        flywheelRpm = rpm;
        flywheelRpm = Range.clip(flywheelRpm, MIN_RPM, MAX_RPM);
    }
}


