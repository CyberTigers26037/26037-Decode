package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subassembly.NumberPlateSensor;

@SuppressWarnings("unused")
@Autonomous(preselectTeleOp = "AutoOp")
public class AutoOp extends OpMode {

    private NumberPlateSensor numberPlateSensor;
    private boolean isBlueAlliance;

    @Override
    public void init() {
        numberPlateSensor = new NumberPlateSensor(hardwareMap);
        isBlueAlliance = numberPlateSensor.isNumberPlateBlue();
    }

    @Override
    public void init_loop() {
        Gamepad driverOp = gamepad1;

        telemetry.addData("Alliance", "A = Blue, B = Red");
        telemetry.addData("Alliance", isBlueAlliance ? "Blue" : "Red");
        telemetry.update();
        if (driverOp.aWasPressed()) {
            isBlueAlliance = true;
        }
        if (driverOp.bWasPressed()) {
            isBlueAlliance = false;
        }
    }

    @Override
    public void loop() {
        telemetry.addData("Alliance", isBlueAlliance ? "Blue" : "Red");
        telemetry.addData("Status", "Running");
        telemetry.update();
    }
}