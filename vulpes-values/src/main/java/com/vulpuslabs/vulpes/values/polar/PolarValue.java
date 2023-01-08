package com.vulpuslabs.vulpes.values.polar;

import com.vulpuslabs.vulpes.values.FakeTrig;

public class PolarValue {

    private double angle;
    private double radius;
    private double xValue;
    private double yValue;

    public PolarValue(double angle, double radius) {
        this.angle = angle;
        this.radius = radius;
    }

    public void set(double angle, double radius) {
        this.angle = angle;
        this.radius = radius;
        updateCartesian();
    }

    public void move(double angleDelta, double radiusDelta) {
        angle += angleDelta;
        angle -= (int) angle;
        if (angle < 0.0) {
            angle = 1.0 - angle;
        }
        radius += radiusDelta;
        updateCartesian();
    }

    private void updateCartesian() {
        xValue = radius * FakeTrig.fakeCosUnit(angle);
        yValue = radius * FakeTrig.fakeSinUnit(angle);
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
