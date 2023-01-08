package com.vulpuslabs.vulpes.values.ranges;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public class Range {

    public static final Range CV_BIPOLAR = new Range(-5.0, 5.0);
    public static final Range CV_UNIPOLAR = new Range(0, 5.0);

    public static final Range UNIT_UNIPOLAR = new Range(0.0, 1.0);
    public static final Range UNIT_BIPOLAR = new Range(-1.0, 1.0);

    private final double lowerBound;
    private final double upperBound;
    private final double size;

    public Range(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.size = upperBound - lowerBound;
    }

    public double clamp(double value) {
        return Math.min(upperBound, Math.max(lowerBound, value));
    }

    public DoubleTransformer clamper() {
        return this::clamp;
    }

    public DoubleTransformer to(Range targetRange) {
        final var scaling = targetRange.size / this.size;
        final var boundShift = targetRange.lowerBound - (lowerBound * scaling);
        return (value) -> boundShift + (value * scaling);
    }

    public DoubleTransformer clampTo(Range targetRange) {
        var scaling = targetRange.size / this.size;
        var boundShift = targetRange.lowerBound - (lowerBound * scaling);
        return (value) -> boundShift + (clamp(value) * scaling);
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }
}
