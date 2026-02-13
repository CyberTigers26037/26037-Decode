package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.geometry.Pose;

public class ConfigurablePose {
    public double x;
    public double y;
    public double angle;
    public ConfigurablePose(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public Pose getPose() {
        return new Pose(x, y, Math.toRadians(angle));
    }
}
