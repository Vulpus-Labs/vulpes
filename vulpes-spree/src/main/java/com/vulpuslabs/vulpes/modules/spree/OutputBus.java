package com.vulpuslabs.vulpes.modules.spree;

import java.util.function.DoubleConsumer;

public class OutputBus {

    private final DoubleConsumer[] outputChannels;

    public OutputBus(DoubleConsumer[] outputChannels) {
        this.outputChannels = outputChannels;
    }

    public void writeValue(int channel, double sample) {
        outputChannels[channel].accept(sample);
    }
}
