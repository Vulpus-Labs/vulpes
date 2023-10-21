package com.vulpuslabs.vulpes.values.events;

import com.vulpuslabs.vulpes.values.api.IndexedDoubleSupplier;
import com.vulpuslabs.vulpes.values.inputs.*;
import com.vulpuslabs.vulpes.values.smoothed.KnobSmoother;
import voltage.core.VoltageAudioJack;
import voltage.core.VoltageComponent;

import java.lang.invoke.*;
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

    public DisconnectableInput connectMonoInput(VoltageAudioJack jack) {
        DisconnectableInput input = new DisconnectableInput(jack::GetValue);
        eventBus.registerBooleanObserver(jack, input::setIsConnected);
        return input;
    }

    public TriggerInput connectTriggerInput(VoltageAudioJack jack) {
        TriggerInput triggerInput = new TriggerInput(jack::GetValue);
        eventBus.registerBooleanObserver(jack, triggerInput::setIsConnected);
        return triggerInput;
    }

    public IndexedDoubleSupplier connectPolyInput(Object polyJack) {
        DisconnectablePolyInput input = new DisconnectablePolyInput(getIndexedDoubleSupplier(polyJack, "GetPolyValue"));
        eventBus.registerBooleanObserver(polyJack, input::setIsConnected);
        return input;
    }

    public DisconnectableOutput connectMonoOutput(VoltageAudioJack jack) {
        DisconnectableOutput output = new DisconnectableOutput(jack::SetValue);
        eventBus.registerBooleanObserver(jack, output::setIsConnected);
        return output;
    }

    public void connectUnsmoothedKnob(VoltageComponent knob, DoubleConsumer valueConsumer) {
        eventBus.registerDoubleObserver(knob, valueConsumer);
    }

    public DoubleSupplier connectSmoothedKnob(VoltageComponent knob) {
        KnobSmoother smoother = new KnobSmoother(
                knob.GetValue(),
                0.0005);

        eventBus.registerDoubleObserver(knob, smoother);
        return smoother;
    }

    public DoubleSupplier connectUnsmoothedCvModulatableKnob(VoltageAudioJack cvInput,
                                                             VoltageComponent amountKnob,
                                                             VoltageComponent knob) {
        double minValue = knob.GetMinValue();
        double maxValue = knob.GetMaxValue();

        UnsmoothedCvModulatableKnob result = new UnsmoothedCvModulatableKnob(
                minValue,
                maxValue,
                cvInput::GetValue);

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

    public EventBus getEventBus() {
        return eventBus;
    }

    public DoubleSupplier connectCvModulatableKnob(VoltageAudioJack cvInput, VoltageComponent amountKnob, VoltageComponent knob) {
        double minValue = knob.GetMinValue();
        double maxValue = knob.GetMaxValue();

        return new CvModulatableKnob(
                minValue,
                maxValue,
                connectMonoInput(cvInput),
                connectSmoothedKnob(knob),
                connectSmoothedKnob(amountKnob));
    }

    public GateInput connectGateInput(VoltageAudioJack jack) {
        GateInput gateInput = new GateInput(jack::GetValue);
        eventBus.registerBooleanObserver(jack, gateInput::setIsConnected);
        return gateInput;
    }

    public void connectThreeStateSwitch(Object component, Consumer<ThreePositionSwitchState> observer) {
        eventBus.registerDoubleObserver(component, ThreePositionSwitchState.toDoubleObserver(observer));
    }
}
