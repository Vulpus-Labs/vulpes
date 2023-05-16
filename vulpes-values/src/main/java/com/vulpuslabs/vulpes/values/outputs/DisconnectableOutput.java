package com.vulpuslabs.vulpes.values.outputs;

import com.vulpuslabs.vulpes.values.api.BooleanConsumer;
import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.inputs.DisconnectableInput;

import java.util.function.DoubleConsumer;

public class DisconnectableOutput implements DoubleConsumer {

    private static final DoubleConsumer DISCONNECTED_OUTPUT = (value) -> {};
    private DoubleConsumer connectedOutput;
    private DoubleConsumer output;
    private BooleanConsumer onConnectionStatusChanged;

    public DisconnectableOutput(DoubleConsumer connectedOutput) {
        this.connectedOutput = connectedOutput;
        this.output = DISCONNECTED_OUTPUT;
        this.onConnectionStatusChanged = BooleanConsumer.DO_NOTHING;
    }

    // Transform is applied only if output is connected.
    public DisconnectableOutput transform(DoubleTransformer transformer) {
        connectedOutput = transformer.transforming(connectedOutput);
        return this;
    }

    public DisconnectableOutput onConnectionStatusChanged(BooleanConsumer handler) {
        onConnectionStatusChanged = handler;
        return this;
    }

    @Override
    public void accept(double value) {
        output.accept(value);
    }

    public void setIsConnected(boolean isConnected) {
        output = isConnected ? connectedOutput : DISCONNECTED_OUTPUT;
        onConnectionStatusChanged.accept(isConnected);
    }
}
