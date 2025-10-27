package org.firstinspires.ftc.teamcode.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subassembly.ArtifactLauncher;


@SuppressWarnings("unused")
@TeleOp(name = "ArtifactLauncherTestOpMode", group = "Test")
public class ArtifactLauncherTestOpMode extends OpMode {
    private ArtifactLauncher launcher;

    @Override
    public void init() {
        launcher = new ArtifactLauncher(hardwareMap);
    }


    @Override
    public void loop() {
        if (gamepad1.left_trigger > 0.1 ) {
            launcher.startFlywheelMotor();
        } else {
            launcher.stopFlywheelMotor();
        }

        if (gamepad1.dpadUpWasPressed()){
            launcher.adjustFlywheelRpm(+ 500 );

        }
        else if (gamepad1.dpadDownWasPressed()){
            launcher.adjustFlywheelRpm(- 500);
        }

        if (gamepad1.right_trigger > 0.1) {
            launcher.raiseFlipper();
        } else {
            launcher.parkFlipper();
        }
        telemetry.addData("Flywheel RPM: ", launcher.getFlywheelRpm());

    }
}