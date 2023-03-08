package com.vulpuslabs.vulpes.filter;

import com.vulpuslabs.vulpes.filter.api.Filter;

abstract class BaseFilter implements Filter {

    public final BiQuadFilter filter = new BiQuadFilter();
    private final int sampleRate;
    private final boolean calculateGainAbs;

    protected BaseFilter(int sampleRate, boolean calculateGainAbs) {
        this.sampleRate = sampleRate;
        this.calculateGainAbs = calculateGainAbs;
    }

    @Override
    public void configure(double centerFreq, double q, double gainDb) {
        var gainAbs = calculateGainAbs ? Math.pow(10, gainDb/ 40) : 0.0;
        var omega = 2 * Math.PI * centerFreq / sampleRate;
        var sn = Math.sin(omega);
        var cs = Math.cos(omega);
        var alpha = sn / (2 * q);
        var beta = Math.sqrt(gainAbs + gainAbs);

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
