package org.firstinspires.ftc.teamcode.subassembly;

import android.content.Context;

import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop;

@SuppressWarnings("unused")
public class NumberPlateLightingSystem implements OpModeManagerNotifier.Notifications {
    private static NumberPlateLightingSystem instance;

    private static final double OFF = 0.0;
    private static final double BLUE = 0.611;
    private static final double RED = 0.279;

    private boolean initialized;
    private Servo rgbLight;
    private NumberPlateSensor numberPlateSensor;

    @OnCreateEventLoop
    public static void attachEventLoop(Context context, FtcEventLoop eventLoop) {
        OpModeManagerImpl opModeManager = eventLoop.getOpModeManager();
        opModeManager.registerListener(NumberPlateLightingSystem.getInstance());
    }

    public static NumberPlateLightingSystem getInstance() {
        if (instance == null) {
            instance = new NumberPlateLightingSystem();
        }

        return instance;
    }

    @Override
    public void onOpModePreInit(OpMode opMode) {
        rgbLight = opMode.hardwareMap.tryGet(Servo.class, "allianceLight");
        numberPlateSensor = new NumberPlateSensor(opMode.hardwareMap);

        if (rgbLight == null) {
            opMode.telemetry.addLine("Unable to init lighting system, cannot find allianceLight");
            return;
        }

        if (!numberPlateSensor.isPresent()) {
            opMode.telemetry.addLine("Unable to init lighting system, cannot find Number Plate Sensor");
            return;
        }

        initialized = true;
    }

    @Override
    public void onOpModePreStart(OpMode opMode) {
        if (!initialized) return;

        if (numberPlateSensor.isNumberPlateBlue()) {
            rgbLight.setPosition(BLUE);
        }
        else {
            rgbLight.setPosition(RED);
        }
    }

    @Override
    public void onOpModePostStop(OpMode opMode) {
        if (!initialized) return;

        rgbLight.setPosition(OFF);
    }
}