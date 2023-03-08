package com.vulpuslabs.vulpes.modules.crosstalk;

public class FilterConfiguration {

    private static final double TWO_PI = 2.0 * Math.PI;
    private final double sr3;
    private double a0;
    private double b1;

    public FilterConfiguration(int sampleRate, double centreFrequency) {
        sr3 = sampleRate * 3.0;
        setCentreFrequency(centreFrequency);
    }

    public void setCentreFrequency(double centreFrequency) {
        double omega = TWO_PI * centreFrequency;
        double n = 1.0 / (sr3 + omega);
        a0 = 2 * omega * n;
        b1 = (sr3 - omega) * n;
    }

    public double getA0() {
        return a0;
    }

    public double getB1() {
        return b1;
    }
}
