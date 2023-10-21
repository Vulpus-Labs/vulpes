package com.vulpuslabs.vulpes.filter;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public class BandPassFilter implements DoubleTransformer {

    private final double freqScaling;
    private final BiQuadFilter filter;

    private double qReciprocal;
    private double cosOmega;
    private double halfSinOmega;
    private double alpha;

    public BandPassFilter(int sampleRate) {
        this.freqScaling = 2.0 * Math.PI / sampleRate;
        this.filter = new BiQuadFilter();
        setCentreFrequency(0.0);
        setQ(1.0);
    }

    public void setCentreFrequency(double centreFrequency) {
        double omega = centreFrequency * freqScaling;

        cosOmega = Math.cos(omega);
        halfSinOmega = Math.sin(omega) * 0.5;

        alpha = halfSinOmega * qReciprocal;

        update();
    }

    public void setQ(double q) {
        qReciprocal = 1.0 / q;
        alpha = halfSinOmega * qReciprocal;

        update();
    }

    private void update() {
        filter.configure(
                1 + alpha,
                -cosOmega - cosOmega,
                1 - alpha,
                halfSinOmega,
                0.0,
                -halfSinOmega
        );
    }

    @Override
    public double apply(double value) {
        return filter.apply(value);
    }
}
