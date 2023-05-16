package com.vulpuslabs.vulpes.values.events;

import com.vulpuslabs.vulpes.values.api.IndexedDoubleSupplier;
import com.vulpuslabs.vulpes.values.inputs.*;
import com.vulpuslabs.vulpes.values.smoothed.KnobSmoother;

import java.lang.invoke.*;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;

public class UIEventConnector {

    private static final MethodType RETURNS_DOUBLE = MethodType.methodType(double.class);
    private static final MethodType RETURNS_INDEXED_DOUBLE = MethodType.methodType(double.class, int.class);
    private static final MethodType ACCEPTS_DOUBLE = MethodType.methodType(void.class, double.class);
    private final EventBus eventBus;

    public UIEventConnector(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public DisconnectableInput connectMonoInput(Object jack) {
        DisconnectableInput input = new DisconnectableInput(getDoubleSupplier(jack, "GetValue"));
        eventBus.registerBooleanObserver(jack, input::setIsConnected);
        return input;
    }

    public TriggerInput connectTriggerInput(Object jack) {
        DoubleSupplier supplier = getDoubleSupplier(jack, "GetValue");
        TriggerInput triggerInput = new TriggerInput(supplier);
        eventBus.registerBooleanObserver(jack, triggerInput::setIsConnected);
        return triggerInput;
    }

    public IndexedDoubleSupplier connectPolyInput(Object polyJack) {
        DisconnectablePolyInput input = new DisconnectablePolyInput(getIndexedDoubleSupplier(polyJack, "GetPolyValue"));
        eventBus.registerBooleanObserver(polyJack, input::setIsConnected);
        return input;
    }

    public DisconnectableOutput connectMonoOutput(Object jack) {
        DisconnectableOutput output = new DisconnectableOutput(getDoubleConsumer(jack, "SetValue"));
        eventBus.registerBooleanObserver(jack, output::setIsConnected);
        return output;
    }

    public void connectUnsmoothedKnob(Object knob, DoubleConsumer valueConsumer) {
        eventBus.registerDoubleObserver(knob, valueConsumer);
    }

    public DoubleSupplier connectSmoothedKnob(Object knob) {
        DoubleSupplier knobValueSupplier = getDoubleSupplier(knob, "GetValue");

        KnobSmoother smoother = new KnobSmoother(
                knobValueSupplier.getAsDouble(),
                0.0005);

        eventBus.registerDoubleObserver(knob, smoother);
        return smoother;
    }

    public DoubleSupplier connectUnsmoothedCvModulatableKnob(Object cvInput,
                                                             Object amountKnob,
                                                             Object knob) {
        double minValue = getDouble(knob, "GetMinValue");
        double maxValue = getDouble(knob, "GetMaxValue");

        UnsmoothedCvModulatableKnob result = new UnsmoothedCvModulatableKnob(
                minValue,
                maxValue,
                getDoubleSupplier(cvInput, "GetValue"));

        eventBus.registerDoubleObserver(amountKnob, result::setModulationAmount);
        eventBus.registerDoubleObserver(knob, result::setKnobValue);
        eventBus.registerBooleanObserver(cvInput, result::setCvIsConnected);

        return result;
    }

    public void connectToggles(IntConsumer consumer, Object...toggles) {
        for (int i=0; i<toggles.length; i++) {
            int finalI = i;
            eventBus.registerDoubleObserver(toggles[i], (v) -> {
                if (v == 1.0) consumer.accept(finalI);
            });
        }
    }

    public void connectTwoStateSwitch(Object component, Consumer<TwoPositionSwitchState> observer) {
        eventBus.registerDoubleObserver(component, TwoPositionSwitchState.toDoubleObserver(observer));
    }

    public DoubleSupplier getDoubleSupplier(Object target, String methodName) {
        var lookup = MethodHandles.lookup();
        try {
            MethodHandle handle = lookup.findVirtual(
                    target.getClass(),
                    methodName,
                    RETURNS_DOUBLE);

            CallSite site = LambdaMetafactory.metafactory(
                    MethodHandles.lookup(),
                    "getAsDouble",
                    MethodType.methodType(DoubleSupplier.class, target.getClass()),
                    RETURNS_DOUBLE,
                    handle,
                    RETURNS_DOUBLE);

            MethodHandle factory = site.getTarget();
            factory = factory.bindTo(target);
            return (DoubleSupplier) factory.invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public IndexedDoubleSupplier getIndexedDoubleSupplier(Object target, String methodName) {
        var lookup = MethodHandles.lookup();
        try {
            MethodHandle handle = lookup.findVirtual(
                    target.getClass(),
                    methodName,
                    RETURNS_INDEXED_DOUBLE);

            CallSite site = LambdaMetafactory.metafactory(
                    MethodHandles.lookup(),
                    "getAsDouble",
                    MethodType.methodType(IndexedDoubleSupplier.class, target.getClass()),
                    RETURNS_INDEXED_DOUBLE,
                    handle,
                    RETURNS_INDEXED_DOUBLE);

            MethodHandle factory = site.getTarget();
            factory = factory.bindTo(target);
            return (IndexedDoubleSupplier) factory.invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public DoubleConsumer getDoubleConsumer(Object target, String methodName) {
        var lookup = MethodHandles.lookup();
        try {
            MethodHandle handle = lookup.findVirtual(
                    target.getClass(),
                    methodName,
                    ACCEPTS_DOUBLE);

            CallSite site = LambdaMetafactory.metafactory(
                    MethodHandles.lookup(),
                    "accept",
                    MethodType.methodType(DoubleConsumer.class, target.getClass()),
                    ACCEPTS_DOUBLE,
                    handle,
                    ACCEPTS_DOUBLE);

            MethodHandle factory = site.getTarget();
            factory = factory.bindTo(target);
            return (DoubleConsumer) factory.invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private double getDouble(Object target, String methodName) {
        try {
            return (double) target.getClass().getMethod(methodName).invoke(target);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public EventBus getEventBus() {
        return eventBus;
    }

}
