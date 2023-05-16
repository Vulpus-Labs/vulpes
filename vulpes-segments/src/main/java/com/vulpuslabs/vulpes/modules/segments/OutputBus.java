package com.vulpuslabs.vulpes.modules.segments;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.DoubleConsumer;

public class OutputBus {

    private final DoubleConsumer triggerOut;
    private final DoubleConsumer startOut;
    private final DoubleConsumer endOut;
    private final DoubleConsumer lengthOut;
    private final DoubleConsumer gateOut;
    private final DoubleConsumer cvOut;

    public OutputBus(
            DoubleConsumer triggerOut,
            DoubleConsumer startOut,
            DoubleConsumer endOut,
            DoubleConsumer lengthOut,
            DoubleConsumer gateOut,
            DoubleConsumer cvOut) {
        this.triggerOut = triggerOut;
        this.startOut = startOut;
        this.endOut = endOut;
        this.lengthOut = lengthOut;
        this.gateOut = gateOut;
        this.cvOut = cvOut;
    }

    public void tick(boolean trigger, double start, double end, double length, double gate, double cv) {
        triggerOut.accept(trigger ? 5.0 : 0.0);
        startOut.accept(start);
        endOut.accept(end);
        lengthOut.accept(length);
        gateOut.accept(gate);
        cvOut.accept(cv);
    }
}
