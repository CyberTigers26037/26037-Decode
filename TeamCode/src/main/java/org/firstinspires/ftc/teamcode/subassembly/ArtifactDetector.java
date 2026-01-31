package org.firstinspires.ftc.teamcode.subassembly;

import android.graphics.Color;

import com.qualcomm.hardware.broadcom.BroadcomColorSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class ArtifactDetector {
    private final NormalizedColorSensor colorSensor1;
    private final NormalizedColorSensor colorSensor2;


    public ArtifactDetector(HardwareMap hwMap){
        colorSensor1 = hwMap.get(NormalizedColorSensor.class,"sensor_color_distance_one");
        colorSensor2 = hwMap.get(NormalizedColorSensor.class,"sensor_color_distance_two");
    }

    public ArtifactColor detectArtifactColor() {
        final float[] hsvValues1 = new float[3];
        final float[] hsvValues2 = new float[3];
        NormalizedRGBA colors1 = colorSensor1.getNormalizedColors();
        NormalizedRGBA colors2 = colorSensor2.getNormalizedColors();
        Color.colorToHSV(colors1.toColor(), hsvValues1);
        Color.colorToHSV(colors2.toColor(), hsvValues2);
        float hue1 = hsvValues1[0];
        float hue2 = hsvValues2[0];

        if (((hue1 >= 200) && (hue1 <= 240)) || ((hue2 >= 200) && (hue2 <= 240))) {
            return ArtifactColor.PURPLE;
        }

        if (((hue1 >= 150) && (hue1 < 180)) || ((hue2 >= 150) && (hue2 < 180))) {
            return ArtifactColor.GREEN;
        }

        return ArtifactColor.NONE;
    }


    private void reinitializeColorSensorIfNecessary(NormalizedColorSensor normalizedColorSensor, boolean force) {
        if (!(normalizedColorSensor instanceof RevColorSensorV3)) return;

        RevColorSensorV3 colorSensorV3 = (RevColorSensorV3) normalizedColorSensor;
        I2cDeviceSynchSimple deviceClient = colorSensorV3.getDeviceClient();
        byte mainStatus = deviceClient.read8(BroadcomColorSensor.Register.MAIN_CTRL.bVal);
        if ((mainStatus == 0) || force) {
            // The color sensor is in the uninitialized state... reinitialize it.
            colorSensorV3.initialize();
        }
    }

    public void reinitialize(boolean force){
        reinitializeColorSensorIfNecessary(colorSensor1, force);
        reinitializeColorSensorIfNecessary(colorSensor2, force);

    }


}