package com.vulpuslabs.vulpes.values.inputs;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class TriggerInput implements BooleanSupplier {

    private final DoubleSupplier input;
    private boolean isConnected;
    private boolean wasTriggering;

    public TriggerInput(DoubleSupplier input) {
        this.input = input;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean hasTriggered() {
        boolean isTriggering = (isConnected && input.getAsDouble() > 0.0);
        if (isTriggering &! wasTriggering) {
            wasTriggering = true;
            return true;
        }
        wasTriggering = isTriggering;
        return false;
    }

    @Override
    public boolean getAsBoolean() {
        return hasTriggered();
    }
}
