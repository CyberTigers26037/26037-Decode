package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.config.RobotConfig;

// 45 resting to about 55?
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
                hwMap.get(AnalogInput.class, "launcherServoEncoder"));

        adjustCloseAngle();
    }

    public boolean isLauncherAtTargetPosition() {
        return launcherServo.isAtTarget();
    }

    public boolean isLauncherRaised() {
        if (!isLauncherAtTargetPosition()) {
            return true;
        }
        return isLauncherRaised;
    }

}