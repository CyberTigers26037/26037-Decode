package org.firstinspires.ftc.teamcode.tuning;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.util.Timer;

@SuppressWarnings("unused")
@TeleOp(group = "Tuning")
public class LauncherTuningOpMode extends OpMode {
    private enum TUNING_PARAMETER { P, I, D }

    private Timer inputTimer;
    private TelemetryManager telemetryM;
    private int motorRpm = 3000;
    private static final int MIN_RPM =  100;
    private static final int MAX_RPM = 6000;
    private static final double MOTOR_TICKS_PER_REVOLUTION = 28;
    private static final double SECONDS_PER_MINUTE = 60;

    private DcMotorEx motor;
    private TUNING_PARAMETER tuningParameter = TUNING_PARAMETER.P;

    @Override
    public void init() {
        inputTimer = new Timer(0.1, true);

        motor = hardwareMap.get(DcMotorEx.class, "flywheelMotor");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    @Override
    public void loop() {
        if (gamepad1.dpadLeftWasPressed()) {
            motorRpm -= 100;
        }
        if (gamepad1.dpadRightWasPressed()) {
            motorRpm += 100;
        }

        if (gamepad1.xWasPressed()) tuningParameter = TUNING_PARAMETER.P;
        if (gamepad1.yWasPressed()) tuningParameter = TUNING_PARAMETER.I;
        if (gamepad1.bWasPressed()) tuningParameter = TUNING_PARAMETER.D;

        telemetry.addLine("DPAD Left/Right to adjust Target RPM");
        telemetry.addLine("Right Trigger to run motor");
        telemetry.addLine("X to tune P, Y to tune I, B to tune D");
        telemetry.addLine();

        double tuningAdjustment = 0;
        if (gamepad1.dpad_up && inputTimer.isExpired()) {
            tuningAdjustment = 1;
            inputTimer.start();
        }
        else if (gamepad1.dpad_down && inputTimer.isExpired()) {
            tuningAdjustment = -1;
            inputTimer.start();
        }

        PIDFCoefficients pidf = motor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);

        if (tuningAdjustment != 0) {
            switch (tuningParameter) {
                case P:
                    pidf.p += tuningAdjustment;
                    break;
                case I:
                    pidf.i += tuningAdjustment;
                    break;
                case D:
                    pidf.d += tuningAdjustment;
                    break;
            }

            motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        }

        if (gamepad1.right_trigger > 0.1) {
            motor.setVelocity(rpmToTps(motorRpm));
        }
        else {
            motor.setVelocity(0);
        }

        telemetry.addData("Target RPM", motorRpm);
        telemetry.addData("Actual RPM", tpsToRpm(motor.getVelocity()));
        telemetry.addLine();

        telemetry.addData("Currently Tuning", tuningParameter);
        telemetry.addLine("DPAD Up/Down to Adjust");
        telemetry.addLine();

        telemetry.addData("P", pidf.p);
        telemetry.addData("I", pidf.i);
        telemetry.addData("D", pidf.d);
        telemetry.addData("F", pidf.f);

        telemetryM.addData("Target RPM", motorRpm);
        telemetryM.addData("Actual RPM", tpsToRpm(motor.getVelocity()));
        telemetryM.update();
    }

    private double rpmToTps(int rpm) {
        // Converts revolutions per minute (RPM) to ticks per second (TPS)
        return rpm * MOTOR_TICKS_PER_REVOLUTION / SECONDS_PER_MINUTE;
    }

    private int tpsToRpm(double tps) {
        // Converts ticks per second (TPS) to revolutions per minute (RPM)
        return (int)(tps / MOTOR_TICKS_PER_REVOLUTION * SECONDS_PER_MINUTE);
    }
}
