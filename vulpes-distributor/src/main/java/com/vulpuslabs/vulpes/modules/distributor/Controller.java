package com.vulpuslabs.vulpes.modules.distributor;

import com.vulpuslabs.vulpes.values.inputs.TriggerInput;

import java.util.function.DoubleConsumer;

public class Controller {

    private final TriggerInput triggerInput;
    private final DoubleConsumer[] gateOutputs;
    private final boolean[] isGating = new boolean[15];
    private final Model model;

    public Controller(TriggerInput triggerInput, Model model, DoubleConsumer[] gateOutputs) {
        this.triggerInput = triggerInput;
        this.model = model;
        this.gateOutputs = gateOutputs;
    }

    public void processSample() {
        model.tick(triggerInput.getAsBoolean(), isGating);

        for (int i=0; i<15; i++) {
            gateOutputs[i].accept(isGating[i] ? 5.0 : 0.0);
        }
    }

    public boolean isGating(int index) {
        return isGating[index];
    }
}
