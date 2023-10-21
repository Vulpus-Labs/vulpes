package com.vulpuslabs.vulpes.values.inputs;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class GateInput implements BooleanSupplier {

    private final DoubleSupplier input;
    private boolean isConnected;

    public GateInput(DoubleSupplier input) {
        this.input = input;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean hasTriggered() {
        return isConnected && input.getAsDouble() > 0.0;
    }

    @Override
    public boolean getAsBoolean() {
        return hasTriggered();
    }
}
