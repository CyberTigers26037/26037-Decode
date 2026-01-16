package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.RobotConfig;

// 45 close, 52 far
public class AdjustLauncherAngle {
    private final AxonServo launcherServo;




    public void adjustCloseAngle() {
        launcherServo.setTargetAngle(RobotConfig.getLauncherClosePosition());
    }

    public void adjustFarAngle() {
        launcherServo.setTargetAngle(RobotConfig.getLauncherFarPosition());
    }

    public void adjustFarAngleAuto() {
        launcherServo.setTargetAngle(RobotConfig.getLauncherFarPositionAuto());
    }

    public AdjustLauncherAngle(HardwareMap hwMap) {
        launcherServo = new AxonServo(hwMap.get(
                Servo.class, "launcherServo"),
                null);

        //adjustCloseAngle();
    }

    public void adjustAngle(double angle) {
        double min = RobotConfig.getLauncherParkedPosition();
        double max = RobotConfig.getLauncherRaisedPosition();
        if (RobotConfig.getLauncherParkedPosition() > RobotConfig.getLauncherRaisedPosition()) {
            angle *= -1.0;
            min = RobotConfig.getLauncherRaisedPosition();
            max = RobotConfig.getLauncherParkedPosition();
        }
        double newAngle = launcherServo.getTargetAngle() + angle;
        newAngle = Range.clip(newAngle, min, max);
        launcherServo.setTargetAngle(newAngle);
    }

    public void outputTelemetry(Telemetry telemetry) {
        telemetry.addData("Launcher Angle", launcherServo.getTargetAngle());
    }

}