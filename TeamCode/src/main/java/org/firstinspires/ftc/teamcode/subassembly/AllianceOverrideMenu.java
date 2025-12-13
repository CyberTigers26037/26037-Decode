package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@SuppressWarnings("unused")
public class AllianceOverrideMenu {

    private boolean isBlueAlliance;


    public void init(HardwareMap hardwareMap) {
        NumberPlateSensor numberPlateSensor = new NumberPlateSensor(hardwareMap);
        isBlueAlliance = numberPlateSensor.isNumberPlateBlue();
    }

    public void init_loop(Gamepad gamepad, Telemetry telemetry) {
        telemetry.addData("Alliance", "A = Blue, B = Red");
        telemetry.addData("Alliance", isBlueAlliance ? "Blue" : "Red");
        telemetry.update();
        if (gamepad.xWasPressed()) {
            isBlueAlliance = true;
            NumberPlateLightingSystem.getInstance().overrideNumberPlateColor(isBlueAlliance);
        }
        if (gamepad.bWasPressed()) {
            isBlueAlliance = false;
            NumberPlateLightingSystem.getInstance().overrideNumberPlateColor(isBlueAlliance);
        }
    }

    public boolean isBlueAlliance() {
        return isBlueAlliance;
    }
}