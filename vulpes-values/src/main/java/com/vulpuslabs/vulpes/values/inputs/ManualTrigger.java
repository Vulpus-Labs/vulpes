package com.vulpuslabs.vulpes.values.inputs;

import java.util.function.BooleanSupplier;

public class ManualTrigger implements BooleanSupplier {

    private boolean wasTriggering;
    private boolean isTriggering;

    public void setValue(double value) {
        isTriggering = value > 0.0;
    }

    @Override
    public boolean getAsBoolean() {
        if (isTriggering &! wasTriggering) {
            wasTriggering = true;
            return true;
        }
        wasTriggering = isTriggering;
        return false;
    }
}
