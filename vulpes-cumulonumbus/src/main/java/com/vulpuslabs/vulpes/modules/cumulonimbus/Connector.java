package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.values.events.UIEventConnector;
import com.vulpuslabs.vulpes.values.inputs.*;
import voltage.core.VoltageAudioJack;
import voltage.core.VoltageComponent;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class Connector {

    private final UIEventConnector connector;

    public Connector(UIEventConnector connector) {
        this.connector = connector;
    }

    public DisconnectableInput leftInput(VoltageAudioJack leftInput) {
        return connector.connectMonoInput(leftInput);
    }

    public DisconnectableInput rightInput(VoltageAudioJack rightInput) {
        return connector.connectMonoInput(rightInput);
    }

    public DoubleSupplier position(VoltageComponent positionKnob, VoltageComponent positionAmountKnob, VoltageAudioJack positionCvInput) {
        return connector.connectUnsmoothedCvModulatableKnob(positionCvInput, positionAmountKnob, positionKnob);
    }

    public DoubleSupplier length(VoltageComponent lengthKnob, VoltageComponent lengthAmountKnob, VoltageAudioJack lengthCvInput) {
        return connector.connectUnsmoothedCvModulatableKnob(lengthCvInput, lengthAmountKnob, lengthKnob);
    }

    public DoubleSupplier pitch(VoltageComponent pitchKnob, VoltageComponent pitchAmountKnob, VoltageAudioJack pitchCvInput) {
        return connector.connectUnsmoothedCvModulatableKnob(pitchCvInput, pitchAmountKnob, pitchKnob);
    }

    public DoubleSupplier fade(VoltageComponent fadeKnob, VoltageComponent fadeAmountKnob, VoltageAudioJack fadeCvInput) {
        return connector.connectUnsmoothedCvModulatableKnob(fadeCvInput, fadeAmountKnob, fadeKnob);
    }

    public DoubleSupplier feedback(VoltageComponent feedbackKnob, VoltageComponent feedbackAmountKnob, VoltageAudioJack feedbackCvInput) {
        return connector.connectCvModulatableKnob(feedbackCvInput, feedbackAmountKnob, feedbackKnob);
    }

    public DoubleSupplier pan(VoltageComponent panKnob, VoltageComponent panAmountKnob, VoltageAudioJack panAmountCv) {
        return connector.connectUnsmoothedCvModulatableKnob(panAmountCv, panAmountKnob, panKnob);
    }

    public DoubleSupplier level(VoltageComponent levelKnob) {
        return connector.connectSmoothedKnob(levelKnob);
    }

    public DoubleSupplier mix(VoltageComponent mixKnob) {
        return connector.connectSmoothedKnob(mixKnob);
    }

    public BooleanSupplier trigger(VoltageComponent triggerButton, VoltageAudioJack triggerCvInput) {
        ManualTrigger manualTrigger = new ManualTrigger();
        connector.getEventBus().registerDoubleObserver(triggerButton, manualTrigger::setValue);
        TriggerInput triggerInput = connector.connectTriggerInput(triggerCvInput);
        return new BooleanOr(manualTrigger, triggerInput);
    }

    public BooleanSupplier freezeGate(VoltageComponent freezeButton, VoltageAudioJack freezeCvInput) {
        ManualGate manualGate = new ManualGate();
        connector.getEventBus().registerDoubleObserver(freezeButton, manualGate::setValue);
        GateInput gateInput = connector.connectGateInput(freezeCvInput);
        return new BooleanOr(manualGate, gateInput);
    }

}
