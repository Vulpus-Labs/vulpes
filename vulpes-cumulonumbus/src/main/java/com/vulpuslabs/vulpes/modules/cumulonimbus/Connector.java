package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.values.events.UIEventConnector;
import com.vulpuslabs.vulpes.values.inputs.BooleanOr;
import com.vulpuslabs.vulpes.values.inputs.DisconnectableInput;
import com.vulpuslabs.vulpes.values.inputs.ManualTrigger;
import com.vulpuslabs.vulpes.values.inputs.TriggerInput;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class Connector {

    private final UIEventConnector connector;

    public Connector(UIEventConnector connector) {
        this.connector = connector;
    }

    public DisconnectableInput leftInput(Object leftInput) {
        return connector.connectMonoInput(leftInput);
    }

    public DisconnectableInput rightInput(Object rightInput) {
        return connector.connectMonoInput(rightInput);
    }

    public DoubleSupplier position(Object positionKnob, Object positionAmountKnob, Object positionCvInput) {
        return connector.connectUnsmoothedCvModulatableKnob(positionCvInput, positionAmountKnob, positionKnob);
    }

    public DoubleSupplier length(Object lengthKnob, Object lengthAmountKnob, Object lengthCvInput) {
        return connector.connectUnsmoothedCvModulatableKnob(lengthCvInput, lengthAmountKnob, lengthKnob);
    }

    public DoubleSupplier pitch(Object pitchKnob, Object pitchAmountKnob, Object pitchCvInput) {
        return connector.connectUnsmoothedCvModulatableKnob(pitchCvInput, pitchAmountKnob, pitchKnob);
    }

    public DoubleSupplier fade(Object fadeKnob, Object fadeAmountKnob, Object fadeCvInput) {
        return connector.connectUnsmoothedCvModulatableKnob(fadeCvInput, fadeAmountKnob, fadeKnob);
    }

    public DoubleSupplier mix(Object mixKnob, Object mixAmountKnob, Object mixCvInput) {
        return connector.connectCvModulatableKnob(mixCvInput, mixAmountKnob, mixKnob);
    }

    public BooleanSupplier trigger(Object triggerButton, Object triggerCvInput) {
        ManualTrigger manualTrigger = new ManualTrigger();
        connector.getEventBus().registerDoubleObserver(triggerButton, manualTrigger::setValue);
        TriggerInput triggerInput = connector.connectTriggerInput(triggerCvInput);
        return new BooleanOr(manualTrigger, triggerInput);
    }

}
