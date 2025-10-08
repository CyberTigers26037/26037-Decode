package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ArtifactIntake {
    private final DcMotorEx intakeMotor;
    private boolean isRunning;

    public ArtifactIntake(HardwareMap hwMap) {
        intakeMotor = hwMap.get(DcMotorEx.class, "intakeMotor");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void start(){
        intakeMotor.setPower(0.5);
        isRunning = true;
    }

    public void stop(){
        intakeMotor.setPower(0.0);
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
