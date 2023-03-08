package com.vulpuslabs.vulpes.modules.chebz;

import com.vulpuslabs.vulpes.filter.resampling.ResampleFilter;

public class DownsampledHarmonicOscillator implements HarmonicOscillator {

    private final HarmonicOscillator rawHarmonicOscillator;
    private final ResampleFilter oddsFilter;
    private final ResampleFilter evensFilter;
    private final ResampleFilter bothFilter;
    private final double factor;

    double[] unmodulatedBuffer = new double[10];
    double[] modulatedBuffer = new double[10];
    double[] blendedBuffer = new double[10];

    public DownsampledHarmonicOscillator(int sampleRate, int factor) {
        this.rawHarmonicOscillator = new RawHarmonicOscillator(sampleRate * factor);
        this.factor = factor;

        oddsFilter = new ResampleFilter(sampleRate * factor, 20e3);
        evensFilter = new ResampleFilter(sampleRate * factor, 20e3);
        bothFilter = new ResampleFilter(sampleRate * factor, 20e3);
    }

    @Override
    public boolean isFolding() {
        return rawHarmonicOscillator.isFolding();
    }

    @Override
    public void setIsFolding(boolean isFolding) {
        rawHarmonicOscillator.setIsFolding(isFolding);
    }

    @Override
    public void processSample(double frequency, double oddEvenBalance, double[] coefficients, double[] unmodulated, double[] modulated, double[] blended) {
        rawHarmonicOscillator.processSample(frequency, oddEvenBalance, coefficients, unmodulated, modulated, blended);

        blended[ODDS] = oddsFilter.apply(blended[ODDS]);
        blended[EVENS] = evensFilter.apply(blended[EVENS]);
        blended[BOTH] = bothFilter.apply(blended[BOTH]);

        for (int i=1; i<factor; i++) {
            rawHarmonicOscillator.processSample(frequency, oddEvenBalance, coefficients, unmodulatedBuffer, modulatedBuffer, blendedBuffer);
            oddsFilter.apply(blendedBuffer[ODDS]);
            evensFilter.apply(blendedBuffer[EVENS]);
            bothFilter.apply(blendedBuffer[BOTH]);
        }
    }

    @Override
    public double getSampleAt(double position, double oddEvenBalance, double[] coefficients, double[] unmodulated, double[] modulated, double[] blended) {
        return rawHarmonicOscillator.getSampleAt(position, oddEvenBalance, coefficients, unmodulated, modulated, blended);
    }

    @Override
    public double getPosition() {
        return rawHarmonicOscillator.getPosition();
    }

    @Override
    public void setPosition(double newPosition) {
        rawHarmonicOscillator.setPosition(newPosition);
    }
}
