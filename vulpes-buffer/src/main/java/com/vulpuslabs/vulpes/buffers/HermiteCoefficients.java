package com.vulpuslabs.vulpes.buffers;

public class HermiteCoefficients {

    private double h0;
    private double h1;
    private double h2;
    private double h3;

    public void set(double d) {
        double dMinus1 = d - 1;
        double dMinus2 = dMinus1 - 1;
        double dMinus3 = dMinus2 - 1;

        double halfDTimesDMinus3 = d * dMinus3 * 0.5;
        double oneSixthDMinus1TimesDMinus2 = dMinus1 * dMinus2 / 6.0;

        h0 = -oneSixthDMinus1TimesDMinus2 * dMinus3;
        h1 = halfDTimesDMinus3 * dMinus2;
        h2 = -halfDTimesDMinus3 * dMinus1;
        h3 = d * oneSixthDMinus1TimesDMinus2;
    }

    public double apply(double n0, double n1, double n2, double n3) {
        return (n0 * h0) + (n1 * h1) + (n2 * h2) + (n3 * h3);
    }

}
