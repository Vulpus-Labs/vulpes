package com.vulpuslabs.vulpes.values.oscillators;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.api.Oscillator;

import java.util.function.DoubleSupplier;

public class OscillatorFromGenerator implements Oscillator {

    private final double sampleRateReciprocal;
    private final DoubleTransformer generator;
    private double pos;
    private double delta;
    private double frequencyHz;

    public OscillatorFromGenerator(double sampleRate, DoubleTransformer generator) {
        this.sampleRateReciprocal = 1.0 / sampleRate;
        this.generator = generator;
        this.delta = 440.0 * sampleRateReciprocal;
    }

    public void setFrequencyHz(double frequencyHz) {
        this.frequencyHz = frequencyHz;
        delta = frequencyHz * sampleRateReciprocal;
    }

    @Override
    public double getFrequencyHz() {
        return frequencyHz;
    }

    @Override
    public double getAtPosition(double pos) {
        return generator.apply(pos);
    }

    @Override
    public double getAsDouble() {
        var result = generator.apply(pos);
        pos += delta;
        pos -= Math.floor(pos);
        return result;
    }

    @Override
    public double getPosition() {
        return pos;
    }

    @Override
    public void setPosition(double position) {
        this.pos = position;
    }
}
