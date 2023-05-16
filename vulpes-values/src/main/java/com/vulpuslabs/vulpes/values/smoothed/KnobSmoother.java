package com.vulpuslabs.vulpes.values.smoothed;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class KnobSmoother implements DoubleSupplier, DoubleConsumer {

    private final double rate;
    private final double rateReciprocal;
    private double current;
    private double target;
    private int count;
    private double delta;

    public KnobSmoother(double initialValue, double rate) {
        this.current = initialValue;
        this.rate = rate;
        this.rateReciprocal = 1.0 / rate;
    }

    @Override
    public void accept(double target) {
        this.target = target;
        double diff = target - current;
        double distance = Math.abs(diff);
        if (distance < rate) {
            current = target;
            count = 0;
            return;
        }
        count = (int) Math.ceil(distance * rateReciprocal);
        delta = rate * Math.signum(diff);
    }

    @Override
    public double getAsDouble() {
        if (count == 0) {
            return target;
        }

        count--;
        current += delta;
        return current;
    }
}
