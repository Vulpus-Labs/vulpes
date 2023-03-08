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
    private final DoubleTransformer clamper;

    public Range(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.size = upperBound - lowerBound;
        if (lowerBound == -upperBound) {
            clamper = (value) -> 0.5 * (Math.abs(value + upperBound) - Math.abs(value + lowerBound) );
        } else if (lowerBound == 0) {
            clamper = (value) -> 0.5 * (Math.abs(value) - Math.abs(value - upperBound) + upperBound);
        } else if (upperBound == 0) {
            clamper = (value) -> 0.5 * (Math.abs(value - lowerBound) - Math.abs(value) + lowerBound);
        } else {
            clamper = (value) -> Math.max(lowerBound, Math.min(upperBound, value));
        }
    }

    public double clamp(double value) {
        return clamper.apply(value);
    }

    public DoubleTransformer clamper() {
        return clamper;
    }

    public DoubleTransformer to(double otherLowerBound, double otherUpperBound) {
        final var otherSize = otherUpperBound - otherLowerBound;
        final var scaling = otherSize / size;
        final var boundShift = otherLowerBound - (lowerBound * scaling);
        return (value) -> boundShift + (value * scaling);
    }

    public DoubleTransformer to(Range targetRange) {
        final var scaling = targetRange.size / this.size;
        final var boundShift = targetRange.lowerBound - (lowerBound * scaling);
        return (value) -> boundShift + (value * scaling);
    }

    public DoubleTransformer clampTo(Range targetRange) {
        var scaling = targetRange.size / this.size;
        var boundShift = targetRange.lowerBound - (lowerBound * scaling);
        return clamper.andThen((value) -> boundShift + (value * scaling));
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public double getCentre() {
        return 0.5 * (lowerBound + upperBound);
    }
}
