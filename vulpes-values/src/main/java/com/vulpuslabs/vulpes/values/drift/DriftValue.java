package com.vulpuslabs.vulpes.values.drift;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.ranges.Range;

import java.util.Random;

public class DriftValue {

    private final Random random = new Random();
    private final int driftTime;
    private final double driftTimeReciprocal;
    private DoubleTransformer driftTransformer;
    private double current;
    private double delta;
    private int remaining;

    public DriftValue(int driftTime, double min, double max) {
        this.driftTime = driftTime;
        this.driftTimeReciprocal = 1.0 / driftTime;
        Range range = new Range(min, max);
        this.current = range.getCentre();
        driftTransformer = Range.UNIT_UNIPOLAR.to(range);

        this.delta = 0;
        this.remaining = 1;
    }

    public void setRange(double min, double max) {
        driftTransformer = Range.UNIT_UNIPOLAR.to(new Range(min, max));
        this.remaining = 1;
    }

    public double getValue() {
        current += delta;
        remaining -= 1;

        if (remaining == 0) {
            remaining = driftTime;
            var target = driftTransformer.apply(random.nextDouble());
            delta = (target - current) * driftTimeReciprocal;
        }

        return current;
    }
}
