package com.vulpuslabs.vulpes.modules.chebysynth;

public class EnvelopeGeneratorSegment {

    private final boolean enabled;
    private final double initial;
    private final int sampleLength;
    private final double delta;

    public EnvelopeGeneratorSegment(boolean enabled, double initial, int sampleLength, double delta) {
        this.enabled = enabled;
        this.initial = initial;
        this.sampleLength = sampleLength;
        this.delta = delta;
    }

    public boolean enabled() {
        return enabled;
    }

    public double initial() {
        return initial;
    }

    public int sampleLength() {
        return sampleLength;
    }

    public double delta() {
        return delta;
    }
}
