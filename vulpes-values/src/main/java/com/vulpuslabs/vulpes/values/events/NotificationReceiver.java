package com.vulpuslabs.vulpes.values.events;

import com.vulpuslabs.vulpes.values.api.BooleanConsumer;
import com.vulpuslabs.vulpes.values.api.ControllableSmoothing;
import com.vulpuslabs.vulpes.values.inputs.ControllableSmoothedInput;
import com.vulpuslabs.vulpes.values.inputs.DisconnectableInput;
import com.vulpuslabs.vulpes.values.outputs.DisconnectableOutput;
import com.vulpuslabs.vulpes.values.smoothed.SmoothedValue;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class NotificationReceiver {

    private final Map<Object, DoubleConsumer> valueObservers = new IdentityHashMap<>();
    private final Map<Object, BooleanConsumer> statusObservers = new IdentityHashMap<>();

    public void registerControllableSmoothing(
            Object controller,
            ControllableSmoothing controllable,
            double fastDecayRate,
            double slowDecayRate) {
        register(controller, (double value) -> {
            if (value == 0.0) {
                controllable.stopSmoothing();
            } else if (value == 1.0) {
                controllable.startSmoothing(fastDecayRate);
            } else {
                controllable.startSmoothing(slowDecayRate);
            }
        });
    }

    public DoubleSupplier registerSmoothableInput(
            Object controller,
            DoubleSupplier input,
            double fastDecayRate,
            double slowDecayRate) {
        var controllable = new ControllableSmoothedInput(input);
        registerControllableSmoothing(controller, controllable, fastDecayRate, slowDecayRate);
        return controllable;
    }

    public void registerKnob(Object knob, double initialValue, DoubleConsumer valueConsumer) {
        register(knob, valueConsumer);
        valueConsumer.accept(initialValue);
    }

    public DoubleSupplier registerSmoothedKnob(Object knob, double initialValue) {
        var smoothedValue = SmoothedValue.uiKnob();
        smoothedValue.accept(initialValue);
        register(knob, smoothedValue);
        return smoothedValue;
    }

    public DisconnectableInput registerInput(Object jack, DoubleSupplier jackReader) {
        var input = new DisconnectableInput(jackReader);
        register(jack, input::setIsConnected);
        return input;
    }

    public DisconnectableOutput registerOutput(Object jack, DoubleConsumer jackWriter) {
        var output = new DisconnectableOutput(jackWriter);
        register(jack, output::setIsConnected);
        return output;
    }

    public NotificationReceiver registerTwoStateSwitch(Object component, BooleanConsumer valueObserver) {
        valueObservers.put(component, (v) -> valueObserver.accept(v == 1.0));
        return this;
    }

    public NotificationReceiver register(Object component, DoubleConsumer valueObserver) {
        valueObservers.put(component, valueObserver);
        return this;
    }

    public NotificationReceiver register(Object component, BooleanConsumer valueObserver) {
        statusObservers.put(component, valueObserver);
        return this;
    }

    private boolean newDoubleValue(Object component, double newValue) {
        var observer = valueObservers.get(component);
        if (observer != null) {
            observer.accept(newValue);
            return true;
        } else {
            return false;
        }
    }

    public boolean knobValueChanged(Object component, double newValue) {
        return newDoubleValue(component, newValue);
    }

    public boolean jackConnected(Object component) {
        var observer = statusObservers.get(component);
        if (observer != null) {
            observer.accept(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean jackDisconnected(Object component) {
        var observer = statusObservers.get(component);
        if (observer != null) {
            observer.accept(false);
            return true;
        } else {
            return false;
        }
    }

    public boolean switchChanged(Object component, double newValue) {
        return newDoubleValue(component, newValue);
    }
}
