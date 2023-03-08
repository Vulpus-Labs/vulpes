package com.vulpuslabs.vulpes.modules.chebz;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public class RawHarmonicOscillator implements HarmonicOscillator {

    private final ChebyshevOscillator oscillator;
    private boolean isFolding;
    
    private DoubleTransformer waveFolding;

    public RawHarmonicOscillator(int sampleRate) {
        this.oscillator = new ChebyshevOscillator(sampleRate, 440.0);
        isFolding = false;
        this.waveFolding = DoubleTransformer.IDENTITY;
    }

    @Override
    public boolean isFolding() {
        return isFolding;
    }

    @Override
    public void setIsFolding(boolean isFolding) {
        if (isFolding != this.isFolding) {
            this.isFolding = isFolding;
            waveFolding = isFolding ? this::fold : DoubleTransformer.IDENTITY;
        }
    }

    private double fold(double x) {
        return 4.0 * (Math.abs(0.25 * x + 0.25 - Math.round(0.25 * x + 0.25)) - 0.25);
    }

    @Override
    public void processSample(double frequencyHz, double oddEvenBalance, double[] coefficients, double[] unmodulated, double[] modulated, double[] blended) {
        oscillator.getSamples(frequencyHz, unmodulated);

        sumHarmonics(coefficients, oddEvenBalance, unmodulated, modulated, blended);
    }

    @Override
    public double getSampleAt(double position, double oddEvenBalance, double[] coefficients, double[] unmodulated, double[] modulated, double[] blended) {
        oscillator.getSamplesAt(position, unmodulated);

        sumHarmonics(coefficients, oddEvenBalance, unmodulated, modulated, blended);

        return blended[BOTH];
    }

    @Override
    public double getPosition() {
        return oscillator.getPosition();
    }

    @Override
    public void setPosition(double newPosition) {
        oscillator.setPosition(newPosition);
    }

    private void sumHarmonics(double[] coefficients, double oddEvenBalance, double[] unmodulated, double[] modulated, double[] blended) {
        for (int i=0; i<10; i++) {
            modulated[i] = unmodulated[i] * coefficients[i];
        }

        var odds = modulated[0] + modulated[2] + modulated[4] + modulated[6] + modulated[8];
        var evens = modulated[1] + modulated[3] + modulated[5] + modulated[7] + modulated[9];
        var both = odds + ((evens - odds) * oddEvenBalance);

        blended[ODDS] = waveFolding.apply(odds);
        blended[EVENS] = waveFolding.apply(evens);
        blended[BOTH] = waveFolding.apply(both);
    }
}
