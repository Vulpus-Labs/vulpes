package com.vulpuslabs.vulpes.values.inputs;

import java.util.function.BooleanSupplier;

public class ManualGate implements BooleanSupplier {

    private boolean isTriggering;

    public void setValue(double value) {
        isTriggering = value > 0.0;
    }

    @Override
    public boolean getAsBoolean() {
        return isTriggering;
    }
}
