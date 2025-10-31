package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subassembly.AxonServo;

@SuppressWarnings("unused")
@TeleOp(group = "Tuning")
public class CarouselAndFlipperTuningOpMode extends OpMode {
    private boolean adjustingCarousel;
    private boolean adjustingFlipper;
    private AxonServo carouselServo;
    private AxonServo flipperServo;
    private double targetAngle;

    @Override
    public void init() {
        carouselServo = new AxonServo(
                hardwareMap.get(Servo.class, "carouselServo"),
                hardwareMap.get(AnalogInput.class, "carouselServoEncoder"));
        flipperServo = new AxonServo(
                hardwareMap.get(Servo.class, "flipperServo"),
                hardwareMap.get(AnalogInput.class, "flipperServoEncoder"));
    }

    @Override
    public void loop() {
        if (adjustingCarousel) {
            telemetry.addLine("Adjusting Carousel");
        }
        else if (adjustingFlipper) {
            telemetry.addLine("Adjusting Flipper");
        }
        else {
            telemetry.addLine("A for Carousel, B for Flipper");
            if (gamepad1.aWasPressed()) adjustingCarousel = true;
            if (gamepad1.bWasPressed()) adjustingFlipper = true;
        }

        AxonServo servo = null;
        if (adjustingCarousel) {
            servo = carouselServo;
        }
        else if (adjustingFlipper) {
            servo = flipperServo;
        }

        if (servo != null) {
            if (!servo.hasTargetAngle()) {
                // Center the servo
                servo.setTargetAngle(180);
            }
            if (Math.abs(gamepad1.right_stick_x) > 0.1) {
                servo.adjustTargetAngle(gamepad1.right_stick_x);
            }

            telemetry.addData("Target Angle", servo.getTargetAngle());
            telemetry.addData("Actual Angle", servo.getCurrentAngle());
            telemetry.addData("Voltage", servo.getVoltage());
            telemetry.addData("Max Voltage", servo.getRawMaxVoltage());
            telemetry.addData("At Target", servo.isAtTarget());
        }
    }
}
