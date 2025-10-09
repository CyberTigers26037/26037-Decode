package org.firstinspires.ftc.teamcode.subassembly;

public class ArtifactTracker {
    private ArtifactColor position1;
    private ArtifactColor position2;
    private ArtifactColor position3;

    public void loadArtifactAtPosition(int position, ArtifactColor color) {
         switch (position) {
            case 1: position1 = color; break;
            case 2: position2 = color; break;
            case 3: position3 = color; break;
         }
    }

    public void removeArtifactFromPosition(int position) {
        switch (position) {
            case 1: position1 = ArtifactColor.NONE; break;
            case 2: position2 = ArtifactColor.NONE; break;
            case 3: position3 = ArtifactColor.NONE; break;
        }
    }

    public ArtifactColor getArtifactAtPosition(int position) {
        switch (position) {
            case 1: return position1;
            case 2: return position2;
            case 3: return position3;
        }
        return ArtifactColor.NONE;
    }
}

