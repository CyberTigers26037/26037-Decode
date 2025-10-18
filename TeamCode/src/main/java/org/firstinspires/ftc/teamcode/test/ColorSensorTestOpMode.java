package org.firstinspires.ftc.teamcode.test;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@SuppressWarnings("unused")
@TeleOp(name = "ColorSensorTestOpMode", group = "Test")
public class ColorSensorTestOpMode extends OpMode {
    private NormalizedColorSensor colorSensor;

    @Override
    public void init() {
        colorSensor = hardwareMap.get(NormalizedColorSensor.class,"sensor_color_distance");
    }

    @Override
    public void loop() {
        final float[] hsvValues = new float[3];
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsvValues);

        telemetry.addData("Red", "%.3f", colors.red);
        telemetry.addData("Green", "%.3f", colors.green);
        telemetry.addData("Blue", "%.3f", colors.blue);
        telemetry.addLine();
        telemetry.addData("Hue", "%.3f", hsvValues[0]);
        telemetry.addData("Saturation", "%.3f", hsvValues[1]);
        telemetry.addData("Value", "%.3f", hsvValues[2]);
        telemetry.addLine();
        telemetry.addData("Alpha", "%.3f", colors.alpha);
    }
}
