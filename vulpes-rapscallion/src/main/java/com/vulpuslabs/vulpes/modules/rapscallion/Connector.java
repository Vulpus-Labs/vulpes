package com.vulpuslabs.vulpes.modules.rapscallion;

import com.vulpuslabs.vulpes.values.events.EventBus;
import com.vulpuslabs.vulpes.values.events.UIEventConnector;
import com.vulpuslabs.vulpes.values.inputs.BooleanOr;
import com.vulpuslabs.vulpes.values.inputs.ManualTrigger;
import com.vulpuslabs.vulpes.values.inputs.TriggerInput;
import voltage.core.VoltageAudioJack;
import voltage.core.VoltageComponent;

import java.util.function.BooleanSupplier;

public class Connector {

    private final UIEventConnector connector;

    public Connector(UIEventConnector connector) {
        this.connector = connector;
    }

    public Controller connect(
            VoltageAudioJack triggerJack,
            VoltageComponent triggerButton,
            VoltageAudioJack lenJack,
            VoltageComponent lenAmountKnob,
            VoltageComponent lenKnob,
            Object autoButton,
            Object octaveUpButton,
            Object revButton,
            VoltageAudioJack pitchJack,
            VoltageComponent pitchAmountKnob,
            VoltageComponent pitchKnob,
            VoltageAudioJack fadeJack,
            VoltageComponent fadeAmountKnob,
            VoltageComponent fadeKnob,
            VoltageAudioJack balanceJack,
            VoltageComponent balanceAmountKnob,
            VoltageComponent balanceKnob,
            VoltageAudioJack posOut,
            VoltageAudioJack loopInL,
            VoltageAudioJack loopInR,
            VoltageAudioJack loopOutL,
            VoltageAudioJack loopOutR,
            VoltageAudioJack feedbackOutTrigger,
            Object rangeSwitch
    ) {
        EventBus eventBus = connector.getEventBus();

        TriggerInput triggerInput = connector.connectTriggerInput(triggerJack);
        ManualTrigger manualTrigger = new ManualTrigger();
        eventBus.registerDoubleObserver(triggerButton, manualTrigger::setValue);
        BooleanSupplier triggerSupplier = new BooleanOr(triggerInput, manualTrigger);

        Controller controller = new Controller(
                triggerSupplier,
                connector.connectUnsmoothedCvModulatableKnob(
                        lenJack, lenAmountKnob, lenKnob),
                connector.connectUnsmoothedCvModulatableKnob(
                        pitchJack, pitchAmountKnob, pitchKnob),
                connector.connectUnsmoothedCvModulatableKnob(
                        fadeJack, fadeAmountKnob, fadeKnob),
                connector.connectUnsmoothedCvModulatableKnob(
                        balanceJack, balanceAmountKnob, balanceKnob),
                connector.connectMonoOutput(posOut),
                connector.connectMonoInput(loopInL),
                connector.connectMonoInput(loopInR),
                connector.connectMonoOutput(loopOutL),
                connector.connectMonoOutput(loopOutR),
                connector.connectMonoOutput(feedbackOutTrigger));

        connector.connectTwoStateSwitch(autoButton, controller::setAutoRetrigger);
        connector.connectTwoStateSwitch(octaveUpButton, controller::setOctaveUp);
        connector.connectTwoStateSwitch(revButton, controller::setReverse);

        eventBus.registerDoubleObserver(rangeSwitch, controller::setRange);
        return controller;
    }
}
