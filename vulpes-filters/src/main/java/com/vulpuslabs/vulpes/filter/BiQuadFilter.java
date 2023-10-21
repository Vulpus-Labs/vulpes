package com.vulpuslabs.vulpes.filter;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public class BiQuadFilter implements DoubleTransformer {

    private double a1;
    private double a2;
    private double b0;
    private double b1;
    private double b2;

    private double xp;
    private double xpp;
    private double yp;
    private double ypp;

    public void configure(double a0, double a1, double a2, double b0, double b1, double b2) {
        double a0inv = 1.0 / a0;
        this.a1 = a1 * a0inv;
        this.a2 = a2 * a0inv;
        this.b0 = b0 * a0inv;
        this.b1 = b1 * a0inv;
        this.b2 = b2 * a0inv;
    }

    @Override
    public double apply(double sample) {
        double y = (b0 * sample) + (b1 * xp) + (b2 * xpp) - (a1 * yp) - (a2 * ypp);
        xpp = xp;
        xp = sample;
        ypp = yp;
        yp = y;
        return y;
    }
}
