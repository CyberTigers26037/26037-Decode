package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ArtifactIntake {
    private final DcMotorEx intakeMotor;
    private boolean isRunning;
    private boolean isRunningInReverse;

    public ArtifactIntake(HardwareMap hwMap) {
        intakeMotor = hwMap.get(DcMotorEx.class, "intakeMotor");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void start(){
        intakeMotor.setPower(1.0);
        isRunning = true;
        isRunningInReverse = false;
    }

    public void stop(){
        intakeMotor.setPower(0.0);
        isRunning = false;
        isRunningInReverse = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void startReverse() {
        intakeMotor.setPower(-1.0);
        isRunning = true;
        isRunningInReverse = true;
    }

    public boolean isRunningInReverse() {
        return isRunningInReverse;
    }
}
