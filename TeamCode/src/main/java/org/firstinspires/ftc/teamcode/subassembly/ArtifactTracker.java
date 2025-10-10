package org.firstinspires.ftc.teamcode.subassembly;

public class ArtifactTracker {
    private ArtifactColor position1 = ArtifactColor.NONE;
    private ArtifactColor position2 = ArtifactColor.NONE;
    private ArtifactColor position3 = ArtifactColor.NONE;

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

    public int getFirstEmptyArtifactPosition() {
        if (position1 == ArtifactColor.NONE) return 1;
        if (position2 == ArtifactColor.NONE) return 2;
        if (position3 == ArtifactColor.NONE) return 3;
        return 0;
    }
    public int getFirstForArtifactColor(ArtifactColor color) {
        if (position1 == color) return 1;
        if (position2 == color) return 2;
        if (position3 == color) return 3;
        return 0;
    }
}

