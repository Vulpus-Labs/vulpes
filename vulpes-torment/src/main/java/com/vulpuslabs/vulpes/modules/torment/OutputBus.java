package com.vulpuslabs.vulpes.modules.torment;

import java.util.function.DoubleConsumer;

public class OutputBus {

    private final DoubleConsumer outputJack;

    private boolean outputIsConnected;

    public OutputBus(DoubleConsumer outputJack) {
        this.outputJack = outputJack;
    }

    public void setOutputSample(double outputSample) {
        outputJack.accept(outputSample);
    }

    public void setOutputIsConnected(boolean outputIsConnected) {
        this.outputIsConnected = outputIsConnected;
    }

    public boolean getOutputIsConnected() {
        return outputIsConnected;
    }
}
