package com.vulpuslabs.vulpes.filter.legacy;

import com.vulpuslabs.vulpes.filter.BiQuadFilter;
import com.vulpuslabs.vulpes.filter.legacy.api.Filter;

abstract class BaseFilter implements Filter {

    private static final double DB_SCALING = 1.0 / 40.0;

    public final BiQuadFilter filter = new BiQuadFilter();
    private final double freqScaling;
    private final boolean calculateGainAbs;

    protected BaseFilter(int sampleRate, boolean calculateGainAbs) {
        this.freqScaling = 2.0 * Math.PI / sampleRate;
        this.calculateGainAbs = calculateGainAbs;
    }

    @Override
    public void configure(double centerFreq, double q, double gainDb) {
        double gainAbs = 0.0;
        double beta = 0.0;

        if (calculateGainAbs) {
            gainAbs = Math.pow(10, gainDb * DB_SCALING);
            beta = Math.sqrt(gainAbs + gainAbs);
        }

        var omega = centerFreq * freqScaling;
        var sn = Math.sin(omega);
        var cs = Math.cos(omega);

        var alpha = sn / (q + q);

        configureFilter(gainAbs, omega, sn, cs, alpha, beta);
    }

    protected final void configureBiquadFilter(double a0, double a1, double a2, double b0, double b1, double b2) {
        filter.configure(a0, a1, a2, b0, b1, b2);
    }

    abstract protected void configureFilter(
            double gainAbs,
            double omega,
            double sn,
            double cs,
            double alpha,
            double beta);

    @Override
    public double apply(double sample) {
        return filter.apply(sample);
    }

}
