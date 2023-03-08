package com.vulpuslabs.vulpes.values.inputs;

import com.vulpuslabs.vulpes.values.events.NotificationReceiver;
import com.vulpuslabs.vulpes.values.smoothed.SmoothedValue;

import java.util.function.DoubleSupplier;

public class SmoothedKnob implements DoubleSupplier {

    private final Object component;
    private final SmoothedValue value;

    public SmoothedKnob(Object component, double initialValue, double decayRate) {
        this.component = component;
        this.value = new SmoothedValue(decayRate);
        value.accept(initialValue);
    }

    public SmoothedKnob(Object component, double initialValue) {
        this.component = component;
        this.value = SmoothedValue.uiKnob();
        value.accept(initialValue);
    }

    public SmoothedKnob register(NotificationReceiver receiver) {
        receiver.register(component, value);
        return this;
    }

    @Override
    public double getAsDouble() {
        return value.getAsDouble();
    }
}
