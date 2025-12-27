package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.config.RobotConfig;

// 45 close, 50 far
public class AdjustLauncherAngle {
    private final AxonServo launcherServo;
    private boolean isLauncherRaised;



    public void adjustCloseAngle() {
        launcherServo.setTargetAngle(RobotConfig.getLauncherRaisedPosition());
        isLauncherRaised = false;
    }

    public void adjustFarAngle() {
        launcherServo.setTargetAngle(RobotConfig.getLauncherParkedPosition());
        isLauncherRaised = true;
    }

    public AdjustLauncherAngle(HardwareMap hwMap) {
        launcherServo = new AxonServo(hwMap.get(
                Servo.class, "launcherServo"),
                null);

        adjustCloseAngle();
    }

    public void adjustAngle(double angle) {
        double newAngle = launcherServo.getTargetAngle() + angle;
        newAngle = Range.clip(newAngle, RobotConfig.getLauncherParkedPosition(), RobotConfig.getLauncherRaisedPosition());
        launcherServo.setTargetAngle(newAngle);
    }

}