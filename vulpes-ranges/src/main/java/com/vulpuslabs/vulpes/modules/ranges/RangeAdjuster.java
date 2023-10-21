package com.vulpuslabs.vulpes.modules.ranges;

import com.vulpuslabs.vulpes.values.events.UIEventConnector;
import voltage.core.VoltageAudioJack;
import voltage.core.VoltageComponent;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class RangeAdjuster {

    public static RangeAdjuster connect(
            UIEventConnector connector,
            VoltageAudioJack input,
            VoltageAudioJack output,
            VoltageComponent addKnob,
            VoltageComponent multiplyKnob) {

        RangeAdjuster adjuster = new RangeAdjuster(
                connector.connectMonoInput(input),
                connector.connectMonoOutput(output),
                connector.connectSmoothedKnob(addKnob),
                connector.connectSmoothedKnob(multiplyKnob)
        );

        connector.getEventBus().registerBooleanObserver(output, adjuster::setIsConnected);

        return adjuster;
    }

    private final DoubleSupplier input;
    private final DoubleConsumer output;
    private final DoubleSupplier addKnob;
    private final DoubleSupplier multiplyKnob;

    private boolean isConnected;

    public RangeAdjuster(
            DoubleSupplier input,
            DoubleConsumer output,
            DoubleSupplier addKnob,
            DoubleSupplier multiplyKnob) {
        this.input = input;
        this.output = output;
        this.addKnob = addKnob;
        this.multiplyKnob = multiplyKnob;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public void processSample() {
        if (!isConnected) return;

        output.accept(
                (input.getAsDouble() *
                        multiplyKnob.getAsDouble())
                        + addKnob.getAsDouble());
    }
}
