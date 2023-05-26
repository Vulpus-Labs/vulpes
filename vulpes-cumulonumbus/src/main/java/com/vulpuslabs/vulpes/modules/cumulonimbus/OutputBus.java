package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.values.api.DoubleBiConsumer;

import java.util.function.DoubleConsumer;

public class OutputBus implements DoubleBiConsumer {

    private final DoubleConsumer outputLeft;
    private final DoubleConsumer outputRight;

    public OutputBus(DoubleConsumer outputLeft, DoubleConsumer outputRight) {
        this.outputLeft = outputLeft;
        this.outputRight = outputRight;
    }

    @Override
    public void accept(double first, double second) {
        outputLeft.accept(first);
        outputRight.accept(second);
    }
}
