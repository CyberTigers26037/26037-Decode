package org.firstinspires.ftc.teamcode.util;

public class Timer {
    private long timerStartedMillis;
    private boolean running;
    private final double durationSeconds;

    public Timer(double durationSeconds) {
        this(durationSeconds, false);
    }

    public Timer(double durationSeconds, boolean autoStart) {
        this.durationSeconds = durationSeconds;

        if (autoStart) {
            start();
        }
    }

    public void start() {
        running = true;
        timerStartedMillis = System.currentTimeMillis();
    }

    public void stop() {
        running = false;
    }

    public double getElapsedSeconds() {
        return (System.currentTimeMillis() - timerStartedMillis) / 1000.0;
    }

    public boolean isExpired() {
        return (running && (getElapsedSeconds() > durationSeconds));
    }

    public boolean isRunning() {
        return running;
    }
}
