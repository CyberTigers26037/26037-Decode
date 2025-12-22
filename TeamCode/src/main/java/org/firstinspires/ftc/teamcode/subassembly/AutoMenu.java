package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AutoMenu {

    private boolean pickupMiddle;

    public void init() {

    }


    public void init_loop(Gamepad gamepad, Telemetry telemetry) {
        telemetry.addData("Pickup Middle", "A = Yes, B = No");
        telemetry.addData("Pickup Middle", pickupMiddle ? "Yes" : "No");
        if (gamepad.aWasPressed()) {
            pickupMiddle = true;
        }
        if (gamepad.bWasPressed()) {
            pickupMiddle = false;
        }
    }

    public boolean getPickupMiddle() {
        return pickupMiddle;
    }
}
