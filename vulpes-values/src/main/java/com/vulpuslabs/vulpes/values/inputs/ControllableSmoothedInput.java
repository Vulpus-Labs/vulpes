package com.vulpuslabs.vulpes.values.inputs;

import com.vulpuslabs.vulpes.values.api.ControllableSmoothing;
import com.vulpuslabs.vulpes.values.smoothed.SmoothedValue;

import java.util.function.DoubleSupplier;

public class ControllableSmoothedInput implements DoubleSupplier, ControllableSmoothing {

    private final DoubleSupplier unsmoothedInput;
    private final SmoothedValue.Supplier smoothedInput;
    private DoubleSupplier valueSupplier;

    public ControllableSmoothedInput(DoubleSupplier unsmoothedInput) {
        this.unsmoothedInput = unsmoothedInput;
        this.smoothedInput = SmoothedValue.smooth(unsmoothedInput, 0.01);
        this.valueSupplier = unsmoothedInput;
    }

    @Override
    public void stopSmoothing() {
        valueSupplier = unsmoothedInput;
    }

    @Override
    public void startSmoothing(double decayRate) {
        valueSupplier = smoothedInput;
        smoothedInput.setDecayRate(decayRate);
    }

    @Override
    public double getAsDouble() {
        return valueSupplier.getAsDouble();
    }
}
