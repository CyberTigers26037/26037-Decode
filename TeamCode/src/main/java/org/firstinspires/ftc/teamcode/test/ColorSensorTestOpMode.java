package org.firstinspires.ftc.teamcode.test;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@SuppressWarnings("unused")
@TeleOp(name = "ColorSensorTestOpMode", group = "Test")
public class ColorSensorTestOpMode extends OpMode {
    private NormalizedColorSensor colorSensor1;
    private NormalizedColorSensor colorSensor2;

    @Override
    public void init() {
        colorSensor1 = hardwareMap.get(NormalizedColorSensor.class,"sensor_color_distance_one");
        colorSensor2 = hardwareMap.get(NormalizedColorSensor.class,"sensor_color_distance_two");
    }

    @Override
    public void loop() {
        final float[] hsvValues1 = new float[3];
        NormalizedRGBA colors1 = colorSensor1.getNormalizedColors();
        Color.colorToHSV(colors1.toColor(), hsvValues1);

        final float[] hsvValues2 = new float[3];
        NormalizedRGBA colors2 = colorSensor2.getNormalizedColors();
        Color.colorToHSV(colors2.toColor(), hsvValues2);

        telemetry.addLine("Color Sensor 1");
        telemetry.addLine()
                .addData("Red", "%.3f", colors1.red)
                .addData("Green", "%.3f", colors1.green)
                .addData("Blue", "%.3f", colors1.blue);
        telemetry.addLine()
                .addData("Hue", "%.3f", hsvValues1[0])
                .addData("Saturation", "%.3f", hsvValues1[1])
                .addData("Value", "%.3f", hsvValues1[2]);
        telemetry.addData("Alpha", "%.3f", colors1.alpha);

        telemetry.addLine();

        telemetry.addLine("Color Sensor 2");
        telemetry.addLine()
                .addData("Red", "%.3f", colors2.red)
                .addData("Green", "%.3f", colors2.green)
                .addData("Blue", "%.3f", colors2.blue);
        telemetry.addLine()
                .addData("Hue", "%.3f", hsvValues2[0])
                .addData("Saturation", "%.3f", hsvValues2[1])
                .addData("Value", "%.3f", hsvValues2[2]);
        telemetry.addData("Alpha", "%.3f", colors2.alpha);
    }
}
