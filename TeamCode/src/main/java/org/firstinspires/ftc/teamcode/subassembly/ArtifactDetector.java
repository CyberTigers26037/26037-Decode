package org.firstinspires.ftc.teamcode.subassembly;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class ArtifactDetector {
    private final NormalizedColorSensor colorSensor;
    private long detectionResumeMillis;

    public ArtifactDetector(HardwareMap hwMap){
        colorSensor = hwMap.get(NormalizedColorSensor.class,"sensor_color_distance");
    }
    public ArtifactColor detectArtifactColor() {
        if (System.currentTimeMillis() < detectionResumeMillis) {
            return ArtifactColor.NONE;
        }
        final float[] hsvValues = new float[3];
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsvValues);
        float hue = hsvValues[0];


        if ((hue >= 200) && (hue <= 240)) {
            return ArtifactColor.PURPLE;
        }
        if ((hue >= 150) && (hue <= 190)) {
            return ArtifactColor.GREEN;
        }
        return ArtifactColor.NONE;
    }

    public void tempStopDetection() {
        detectionResumeMillis = System.currentTimeMillis() + 1000;
    }
}

