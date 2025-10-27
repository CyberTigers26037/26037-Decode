package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class NumberPlateSensor {
    private final TouchSensor numberPlateSensor;

    public NumberPlateSensor(HardwareMap hwMap) {
        numberPlateSensor = hwMap.tryGet(TouchSensor.class, "Number Plate Sensor");
    }

    public boolean isNumberPlateBlue () {
        if (numberPlateSensor == null) return false;

        return numberPlateSensor.isPressed();
    }

    public boolean isPresent() {
        return (numberPlateSensor != null);
    }
}
