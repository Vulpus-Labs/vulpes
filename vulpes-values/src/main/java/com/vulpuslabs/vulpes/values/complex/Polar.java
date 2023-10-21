package com.vulpuslabs.vulpes.values.complex;

import com.vulpuslabs.vulpes.values.Approximate;

public class Polar {

    private double magnitude;
    private double theta;

    public Polar() {

    }

    public Polar(double magnitude, double theta) {
        this.magnitude = magnitude;
        this.theta = theta;
    }

    public void set(double magnitude, double theta) {
        this.magnitude = magnitude;
        this.theta = theta;
    }

    public void toComplex(Complex target) {
        target.set(
                magnitude * Math.cos(theta),
                magnitude * Math.sin(theta)
        );
    }

    public double getMagnitude() {
        return magnitude;
    }

    public double getTheta() {
        return theta;
    }

    public void add(Polar other) {
        magnitude += other.magnitude;
        theta += other.theta;
    }

    public void scaleAndAdd(double scale, Polar other) {
        magnitude += (other.magnitude * scale);
        theta += other.theta;
    }
}
