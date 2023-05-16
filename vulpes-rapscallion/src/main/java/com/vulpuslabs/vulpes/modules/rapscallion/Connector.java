package com.vulpuslabs.vulpes.modules.rapscallion;

import com.vulpuslabs.vulpes.values.events.EventBus;
import com.vulpuslabs.vulpes.values.events.UIEventConnector;
import com.vulpuslabs.vulpes.values.inputs.BooleanOr;
import com.vulpuslabs.vulpes.values.inputs.ManualTrigger;
import com.vulpuslabs.vulpes.values.inputs.TriggerInput;

import java.util.function.BooleanSupplier;

public class Connector {

    private final UIEventConnector connector;

    public Connector(UIEventConnector connector) {
        this.connector = connector;
    }

    public Controller connect(
            Object triggerJack,
            Object triggerButton,
            Object lenJack,
            Object lenAmountKnob,
            Object lenKnob,
            Object octaveUpButton,
            Object revButton,
            Object pitchJack,
            Object pitchAmountKnob,
            Object pitchKnob,
            Object fadeJack,
            Object fadeAmountKnob,
            Object fadeKnob,
            Object posOut,
            Object loopIn,
            Object loopOut,
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
                connector.connectMonoOutput(posOut),
                connector.connectMonoInput(loopIn),
                connector.connectMonoOutput(loopOut));

        connector.connectTwoStateSwitch(octaveUpButton, controller::setOctaveUp);
        connector.connectTwoStateSwitch(revButton, controller::setReverse);

        eventBus.registerDoubleObserver(rangeSwitch, controller::setRange);
        return controller;
    }
}
