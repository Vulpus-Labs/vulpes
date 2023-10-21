package com.vulpuslabs.vulpes.modules.distributor;

import com.vulpuslabs.vulpes.values.events.EventBus;

public class Connector {

    private final Model model;
    private final EventBus eventBus;

    public Connector(Model model, EventBus eventBus) {
        this.model = model;
        this.eventBus = eventBus;
    }

    public void connect(Object gateTimeKnob, Object delayTimeKnob, Object gateJitterKnob, Object delayJitterKnob, Object[] balanceKnobs) {
        eventBus.registerDoubleObserver(gateTimeKnob, model::setGateLength);
        eventBus.registerDoubleObserver(delayTimeKnob, model::setDelayLength);
        eventBus.registerDoubleObserver(gateJitterKnob, model::setGateJitter);
        eventBus.registerDoubleObserver(delayJitterKnob, model::setDelayJitter);

        for (int i=0; i<10; i++) {
            final int j = i;
            eventBus.registerDoubleObserver(balanceKnobs[i], (amt) -> model.setBalance(j, amt));
        }
    }
}
