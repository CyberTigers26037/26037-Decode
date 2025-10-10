package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subassembly.ArtifactSystem;
import org.firstinspires.ftc.teamcode.subassembly.MecanumDrive;

@SuppressWarnings("unused")
@TeleOp(name="DecodeTeleOp")
public class DecodeTeleOp extends OpMode {
    private ArtifactSystem artifactSystem;
    private MecanumDrive drive;
    private final double flywheelPower = 0.5;

    @Override
    public void init() {
        artifactSystem = new ArtifactSystem(hardwareMap);
        drive = new MecanumDrive();
        drive.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.left_trigger > 0.1) {
            artifactSystem.startLauncher();
        }
        else {
            artifactSystem.stopLauncher();
        }
        if (gamepad1.rightBumperWasPressed()) {
            artifactSystem.moveCarouselToFireFirstPurple();
        }
        if (gamepad1.leftBumperWasPressed()) {
            artifactSystem.moveCarouselToFireFirstGreen();
        }

        if (gamepad1.dpadUpWasPressed()) {
            artifactSystem.adjustLauncherPower(+ 0.1);
        }


        if (gamepad1.dpadDownWasPressed()) {
            artifactSystem.adjustLauncherPower(- 0.1);
        }


        if (gamepad1.aWasPressed()) {
            artifactSystem.toggleIntake();
            artifactSystem.moveCarouselToPosition(1);
        }

        if (gamepad1.rightStickButtonWasPressed()) {
            artifactSystem.resetCarouselDetection();
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

        artifactSystem.loop();

        artifactSystem.outputTelemetry(telemetry);

        double axial = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double yaw = gamepad1.right_stick_x;

        drive.drive(axial, lateral, yaw);




    }
}
