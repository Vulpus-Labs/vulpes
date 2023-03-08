package com.vulpuslabs.vulpes.modules.crosstalk;

import com.vulpuslabs.vulpes.values.api.IndexedDoubleConsumer;

public class OutputBus {

    private final IndexedDoubleConsumer outputJack;
    private boolean outputIsConnected;

    public OutputBus(IndexedDoubleConsumer outputJack) {
        this.outputJack = outputJack;
    }

    public void setOutputIsConnected(boolean isConnected) {
        this.outputIsConnected = isConnected;
    }

    public boolean getOutputIsConnected() {
        return outputIsConnected;
    }

    void writeOutput(int channel, double value) {
        outputJack.accept(channel, value);
    }
}
