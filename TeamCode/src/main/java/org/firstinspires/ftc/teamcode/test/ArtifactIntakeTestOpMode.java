package org.firstinspires.ftc.teamcode.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subassembly.ArtifactIntake;

@SuppressWarnings("unused")
@TeleOp(name = "ArtifactIntakeTestOpMode", group = "OpMode")

public class ArtifactIntakeTestOpMode extends OpMode {
    private ArtifactIntake artifactIntake;

    @Override
    public void init() {
        artifactIntake = new ArtifactIntake(hardwareMap);
    }

    @Override
    public void loop(){
        if(gamepad1.aWasPressed()){
            if(!artifactIntake.isRunning()){
                artifactIntake.start();
            }
            else{
                artifactIntake.stop();
            }

        }


        telemetry.addData("Intake: ", artifactIntake.isRunning());
    }
}
