package com.vulpuslabs.vulpes.values.smoothed;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class SmoothedValue implements DoubleConsumer, DoubleSupplier {

    public static Supplier smooth(DoubleSupplier unsmoothed, int durationMs) {
        return new Supplier(unsmoothed, durationMs);
    }

    private final int durationSamples;
    private final double reciprocal;
    private double current;
    private double distance;
    private double target;
    private double delta;
    private double deltaSig;
    private int samplesRemaining;

    public SmoothedValue(int durationMs) {
        this.durationSamples = durationMs * 48;
        this.reciprocal = 1.0 / durationSamples;
    }

    @Override
    public void accept(double value) {
        // No change
        if (value == this.target) {
            return;
        }

        // Finished moving
        var newDistance = value - current;
        var newDeltaSig = Math.signum(newDistance);

        if (samplesRemaining == 0) {
            target = value;
            deltaSig = newDeltaSig;
            samplesRemaining = durationSamples;
            distance = newDistance;
            delta = newDistance * reciprocal;
            return;
        }

        // Already in motion: retarget
        if (Math.abs(newDistance) < Math.abs(distance)) {
            if (newDeltaSig != deltaSig) {
                delta -= delta;
                deltaSig = newDeltaSig;
            }
            distance = newDistance;
            target = value;
            return;
        }

        target = value;
        deltaSig = newDeltaSig;
        distance = newDistance;
        samplesRemaining = durationSamples;
        delta = newDistance * reciprocal;
    }

    @Override
    public double getAsDouble() {
        if (samplesRemaining == 0) {
            return target;
        }

        if (Math.abs(distance) < Math.abs(delta)) {
            samplesRemaining = 0;
            current = target;
            return current;
        }

        samplesRemaining -= 1;
        current += delta;
        distance -= delta;
        return current;
    }

    public static class Supplier implements DoubleSupplier {

        private DoubleSupplier unsmoothed;
        private final SmoothedValue smoothed;

        public Supplier(DoubleSupplier unsmoothed, int durationMs) {
            this.unsmoothed = unsmoothed;
            smoothed = new SmoothedValue(durationMs);
        }

        public void setUnsmoothed(DoubleSupplier unsmoothed) {
            this.unsmoothed = unsmoothed;
        }

        @Override
        public double getAsDouble() {
            smoothed.accept(unsmoothed.getAsDouble());
            return smoothed.getAsDouble();
        }
    }
}
