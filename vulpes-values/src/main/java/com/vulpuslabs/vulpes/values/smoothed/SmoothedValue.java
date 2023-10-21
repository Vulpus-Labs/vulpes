package com.vulpuslabs.vulpes.values.smoothed;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class SmoothedValue implements DoubleConsumer, DoubleSupplier {

    private static final double UI_KNOB_DECAY_RATE = 0.005;

    public static SmoothedValue uiKnob() {
        return new SmoothedValue(UI_KNOB_DECAY_RATE);
    }

    public static Supplier smooth(DoubleSupplier unsmoothed, double decayRate) {
        return new Supplier(unsmoothed, decayRate);
    }

    private double a0;
    private double b1;
    private double x;
    private double y1;

    public SmoothedValue(double decayRate) {
        this.a0 = decayRate;
        this.b1 = 1.0 - a0;
    }

    public SmoothedValue(double decayRate, double initial) {
        this.a0 = decayRate;
        this.b1 = 1.0 - a0;
        this.y1 = initial;
    }

    public void setDecayRate(double decayRate) {
        this.a0 = decayRate;
        this.b1 = 1.0 - a0;
    }

    @Override
    public void accept(double value) {
        x = value;
    }

    @Override
    public double getAsDouble() {
        var y = (a0 * x) + (b1 * y1);
        y1 = y;
        return y;
    }

    public static class Supplier implements DoubleSupplier {

        private DoubleSupplier unsmoothed;
        private final SmoothedValue smoothed;

        public Supplier(DoubleSupplier unsmoothed, double a0) {
            this.unsmoothed = unsmoothed;
            smoothed = new SmoothedValue(a0);
        }

        public void setDecayRate(double decayRate) {
            smoothed.setDecayRate(decayRate);
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
