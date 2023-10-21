package com.vulpuslabs.vulpes.filter;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public class HighPassFilter implements DoubleTransformer {

    private final double freqScaling;
    private final BiQuadFilter filter;

    private double twoQReciprocal;
    private double cosOmega;
    private double sinOmega;
    private double alpha;

    public HighPassFilter(int sampleRate) {
        this.freqScaling = 2.0 * Math.PI / sampleRate;
        this.filter = new BiQuadFilter();
        this.cosOmega = Math.cos(0.0);
        this.sinOmega = Math.sin(0.0);
    }

    public void setCentreFrequency(double centreFrequency) {
        double omega = centreFrequency * freqScaling;

        cosOmega = Math.cos(omega);
        sinOmega = Math.sin(omega);

        alpha = sinOmega * twoQReciprocal;

        update();
    }

    public void setQ(double q) {
        twoQReciprocal = 1.0 / (q + q);
        alpha = sinOmega * twoQReciprocal;

        update();
    }

    private void update() {
        var onePlusCosOmega = 1 + cosOmega;
        var onePlusCosOmegaDivideTwo = onePlusCosOmega * 0.5;

        filter.configure(
                1 + alpha,
                -cosOmega - cosOmega,
                1 - alpha,
                onePlusCosOmegaDivideTwo,
                onePlusCosOmega,
                onePlusCosOmegaDivideTwo
        );
    }

    @Override
    public double apply(double value) {
        return filter.apply(value);
    }
}
