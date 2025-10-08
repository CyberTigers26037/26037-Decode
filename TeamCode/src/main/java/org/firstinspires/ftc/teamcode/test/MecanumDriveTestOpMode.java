package org.firstinspires.ftc.teamcode.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subassembly.MecanumDrive;


@SuppressWarnings("unused")
@TeleOp(name ="MecanumDriveTestOpMode", group = "OpMode")
public class MecanumDriveTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode(){

        MecanumDrive drive = new MecanumDrive();
        drive.init(hardwareMap);



        waitForStart();

        while (opModeIsActive()){
            double axial = -gamepad1.left_stick_y;
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            drive.drive(axial, lateral, yaw);
        }
    }
}