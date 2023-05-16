package com.vulpuslabs.vulpes.values.stereo;

import static com.vulpuslabs.vulpes.values.Approximate.cosUnit;

public class Pan {

    private double left = 1.0;
    private double right = 1.0;

    public void set(double balance) {
        var curve = cosUnit(0.5 * balance);
        var absCurve = Math.abs(curve);
        var rclamp = 0.5 * (absCurve - Math.abs(curve - 1.0) + 1.0);
        var lclamp = 0.5 * (Math.abs(curve + 1.0) - absCurve - 1.0);

        left = 1.0 + lclamp;
        right = 1.0 - rclamp;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }
}
