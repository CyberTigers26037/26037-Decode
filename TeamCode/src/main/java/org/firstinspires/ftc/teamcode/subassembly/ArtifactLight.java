package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ArtifactLight {
    public Servo artifactLight;

    public ArtifactLight (HardwareMap hwMap) {
        artifactLight = hwMap.get(Servo.class, "artifactLight");
    }

    public void setColor(ArtifactColor color) {
        if (color == ArtifactColor.PURPLE) {
            artifactLight.setPosition(0.722);
        }
        else if (color == ArtifactColor.GREEN) {
            artifactLight.setPosition(0.500);
        }
        else {
            artifactLight.setPosition(0.0);
        }
    }
}
