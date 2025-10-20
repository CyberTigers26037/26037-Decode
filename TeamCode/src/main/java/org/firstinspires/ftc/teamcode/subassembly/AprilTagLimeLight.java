package org.firstinspires.ftc.teamcode.subassembly;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class AprilTagLimeLight {
    private Limelight3A limelight;

    public void init(HardwareMap hardwareMap) {
        // If the robot does not have a limelight camera configured, this will return null
        // and we won't do any limelight camera operations
        limelight = hardwareMap.tryGet(Limelight3A.class, "limelight");
    }

    public void beginDetectingObelisk() {
        if (limelight == null) return;

        limelight.pipelineSwitch(0);
        limelight.start();
    }

    public void beginDetectingTeamBlue() {
        if (limelight == null) return;

        limelight.pipelineSwitch(1);
        limelight.start();
    }

    public void beginDetectingTeamRed() {
        if (limelight == null) return;

        limelight.pipelineSwitch(2);
        limelight.start();
    }


    public String findObeliskArtifactOrder() {
        if (limelight == null) return null;

        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            for (LLResultTypes.FiducialResult detection : result.getFiducialResults()) {
                int id = detection.getFiducialId();
                if (id == 21) {
                    return "Green Purple Purple";
                } else if (id == 22) {
                    return "Purple Green Purple";
                } else if (id == 23) {
                    return "Purple Purple Green";
                }
            }
        }

        return null;
    }

    public Double detectGoalAngle() {
        if (limelight == null) return null;

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            return result.getTx();
        }
        return null;
    }




}
