package org.firstinspires.ftc.teamcode.subassembly;


import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.config.RobotConfig;

public class ArtifactLauncher {
    private final DcMotorEx flywheelMotor;
    private final AxonServo flipperServo;
    private int flywheelRpm = 3000;
    private boolean isRunning;
    private boolean isFlipperRaised;
    private static final int MIN_RPM =  1000;
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
        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flipperServo = new AxonServo(hwMap.get(
                Servo.class, "flipperServo"),
                hwMap.get(AnalogInput.class, "flipperServoEncoder"));

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
        flywheelMotor.setVelocity(0);
        isRunning = false;
    }

    public int getActualFlywheelRpm() {
        return tpsToRpm(flywheelMotor.getVelocity());
    }

    public void raiseFlipper() {
        flipperServo.setTargetAngle(RobotConfig.getFlipperRaisedPosition());
        isFlipperRaised = true;
    }

    public void parkFlipper() {
        flipperServo.setTargetAngle(RobotConfig.getFlipperParkedPosition());
        isFlipperRaised = false;
    }

    public boolean isRunning(){
        return isRunning;
    }

    public void setFlywheelRpm(int rpm) {
        flywheelRpm = rpm;
        flywheelRpm = Range.clip(flywheelRpm, MIN_RPM, MAX_RPM);
    }

    public boolean isFlipperAtTargetPosition() {
        return flipperServo.isAtTarget();
    }

    public boolean isFlipperRaised() {
        if (!isFlipperAtTargetPosition()) {
            return true;
        }
        return isFlipperRaised;
    }

    public boolean isLauncherAboveMinSpeed() {
        return getActualFlywheelRpm() > MIN_RPM;
    }
}


