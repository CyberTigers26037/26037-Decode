package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ArtifactLight {
    public final Servo artifactLight;
    private LightColor currentColor;

    public LightColor getColor() {
        return currentColor;
    }

    public enum LightColor {
        NONE, PURPLE, GREEN, YELLOW
    }

    public ArtifactLight (HardwareMap hwMap) {
        currentColor = LightColor.NONE;
        artifactLight = hwMap.get(Servo.class, "artifactLight");
    }

    private LightColor artifactColorToLightColor(ArtifactColor color) {
        switch (color) {
            case PURPLE: return LightColor.PURPLE;
            case GREEN: return LightColor.GREEN;
            default: return LightColor.NONE;
        }
    }

    public void setColor(ArtifactColor color) {
        setColor(artifactColorToLightColor(color));
    }

    public void setColor(LightColor color) {
        if (color == LightColor.PURPLE) {
            artifactLight.setPosition(0.722);
        }
        else if (color == LightColor.GREEN) {
            artifactLight.setPosition(0.500);
        }
        else if (color == LightColor.YELLOW) {
            artifactLight.setPosition(0.388);
        }
        else {
            artifactLight.setPosition(0.0);
        }
        currentColor = color;
    }


}
