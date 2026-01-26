package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AutoMenu {

    private boolean pickupMiddle = true;

    public void init() {

    }


    public void init_loop(Gamepad gamepad, Telemetry telemetry, String autoName) {
        //telemetry.addData("Pickup Middle", "A = Yes, B = No");
        telemetry.addData("Pickup Middle", pickupMiddle ? "Yes (B to Change)" : "No (A to Change)");
        telemetry.addData("ARE YOU SURE",autoName);
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
