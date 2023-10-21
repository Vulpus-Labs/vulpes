package com.vulpuslabs.vulpes.modules.chebysynth;

import com.vulpuslabs.vulpes.values.smoothed.SmoothedValue;

public class HarmonicWeights {

    private final double[] weights = new double[10];
    private final SmoothedValue[] smoothedWeights = new SmoothedValue[10];
    private final double[] coefficients = new double[10];
    private final double[] envelopeWeightings = new double[10];

    public HarmonicWeights() {
        for (int i=0; i<10; i++) {
            smoothedWeights[i] = SmoothedValue.uiKnob();
        }
    }

    public double[] getCoefficients(double envelope) {
        for (int i=0; i<10; i++) {
            coefficients[i] = weights[i] + envelopeWeightings[i] * envelope;
        }
        return coefficients;
    }

    public void tick() {
        for (int i=0; i<10; i++) {
            weights[i] = smoothedWeights[i].getAsDouble();
        }
    }

    public void setWeight(int j, double weight) {
        smoothedWeights[j].accept(weight);
    }

    public void setEnvelopeWeighting(int j, double weighting) {
        envelopeWeightings[j] = weighting;
    }
}
