package org.firstinspires.ftc.teamcode.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subassembly.ArtifactLauncher;


@SuppressWarnings("unused")
@TeleOp(name = "ArtifactLauncherTestOpMode", group = "OpMode")
public class ArtifactLauncherTestOpMode extends OpMode {
    private ArtifactLauncher launcher;
    static final double MAX_FLYWHEEL_POWER = 1.0;
    static final double MIN_FLYWHEEL_POWER = 0.1;



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
            launcher.adjustFlywheelPower(+ 0.1 );

        }
        else if (gamepad1.dpadDownWasPressed()){
            launcher.adjustFlywheelPower(- 0.1);
        }

        if (gamepad1.right_trigger > 0.1) {
            launcher.raiseFlipper();
        } else {
            launcher.parkFlipper();
        }
        telemetry.addData("fly wheel power: ", launcher.getFlywheelPower());

    }
}