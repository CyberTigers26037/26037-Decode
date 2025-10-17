package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Device;

import org.firstinspires.ftc.teamcode.config.RobotConfig;

@SuppressWarnings("unused")
@TeleOp(name = "RobotConfigTestOpMode", group = "Test")
public class RobotConfigTestOpMode extends OpMode {
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        String robotSerialNumber = Device.getSerialNumberOrUnknown();

        String robotConfigName = RobotConfig.getRobotName();

        telemetry.addData("Serial", robotSerialNumber);
        telemetry.addData("Name", robotConfigName);
        telemetry.update();
    }
}
