package org.firstinspires.ftc.teamcode.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subassembly.ArtifactSystem;


@SuppressWarnings("unused")
@TeleOp(name = "ArtifactSystemTestOpMode", group = "Test")
public class ArtifactSystemTestOpMode extends OpMode {
    private ArtifactSystem artifactSystem;


    @Override
    public void init() {
        artifactSystem = new ArtifactSystem(hardwareMap);
    }


    @Override
    public void loop() {
        if (gamepad1.left_trigger > 0.1) {
            artifactSystem.startLauncher();
        }
        else {
            artifactSystem.stopLauncher();
        }


        if (gamepad1.dpadUpWasPressed()) {
            artifactSystem.adjustLauncherRpm(+ 500);
        }


        if (gamepad1.dpadDownWasPressed()) {
            artifactSystem.adjustLauncherRpm(- 500);
        }


        if (gamepad1.aWasPressed()) {
            artifactSystem.toggleIntake();
            artifactSystem.moveCarouselToPosition(1);
        }


        if (gamepad1.right_trigger > 0.1) {
            artifactSystem.raiseFlipper();
        }
        else {
            artifactSystem.parkFlipper();
        }


        if (gamepad1.xWasPressed()) {
            artifactSystem.moveCarouselToPosition(1);
        }
        if (gamepad1.yWasPressed()) {
            artifactSystem.moveCarouselToPosition(2);
        }
        if (gamepad1.bWasPressed()) {
            artifactSystem.moveCarouselToPosition(3);
        }

        artifactSystem.outputTelemetry(telemetry);

    }
}
