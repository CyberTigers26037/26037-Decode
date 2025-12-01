package org.firstinspires.ftc.teamcode.subassembly;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AprilTagLimelight {
    public enum ObeliskOrder {
        NONE, PPG, PGP, GPP
    }
    private Limelight3A limelight;
    private static final double LIMELIGHT_MOUNT_ANGLE_DEGREES = 10.0;
    private static final double LIMELIGHT_LENS_HEIGHT_INCHES  = 11.25;
    private static final double GOAL_HEIGHT_INCHES            = 29.75;

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

    public ObeliskOrder findObeliskArtifactOrder() {
        if (limelight == null) return ObeliskOrder.NONE;

        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            for (LLResultTypes.FiducialResult detection : result.getFiducialResults()) {
                int id = detection.getFiducialId();
                if (id == 21) {
                    return ObeliskOrder.GPP;
                } else if (id == 22) {
                    return ObeliskOrder.PGP;
                } else if (id == 23) {
                    return ObeliskOrder.PPG;
                }
            }
        }

        return ObeliskOrder.NONE;
    }

    public Double detectGoalAngle() {
        if (limelight == null) return null;

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            return result.getTx();
        }
        return null;
    }

    public Double detectGoalDistance() {
        if (limelight == null) return null;

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            double targetOffsetAngle_Vertical = result.getTy();
            double angleToGoalDegrees = LIMELIGHT_MOUNT_ANGLE_DEGREES + targetOffsetAngle_Vertical;
            double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

            return (GOAL_HEIGHT_INCHES - LIMELIGHT_LENS_HEIGHT_INCHES) / Math.tan(angleToGoalRadians);

        }
        return null;
    }

    public void outputTelemetry(Telemetry telemetry) {
        if (limelight == null) return;

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {

            telemetry.addData("Tx", result.getTx());
            telemetry.addData("Ty", result.getTy());
            telemetry.addData("Distance: ", detectGoalDistance());
        }
    }
}
