package com.vulpuslabs.vulpes.values.polar;

import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.ranges.Range;

public class PolarValue {

    private static final Range RADIUS_BOUND = Range.CV_BIPOLAR;
    private double angle;
    private double radius;
    private double xValue;
    private double yValue;

    public PolarValue(double angle, double radius) {
        this.angle = angle;
        this.radius = radius;
        updateCartesian();
    }

    public void set(double angle, double radius) {
        this.angle = angle;
        this.radius = radius;
        updateCartesian();
    }

    public void move(double angleDelta, double radiusDelta) {
        angle += angleDelta;
        angle -= Math.floor(angle);
        radius = RADIUS_BOUND.clamp(radius + radiusDelta);
        updateCartesian();
    }

    private void updateCartesian() {
        xValue = radius * Approximate.cosUnit(angle);
        yValue = radius * Approximate.sinUnit(angle);
    }

    public double getCartesianX() {
        return xValue;
    }

    public double getCartesianY() {
        return yValue;
    }

    public double getAngle() {
        return angle;
    }

    public double getRadius() {
        return radius;
    }
}
