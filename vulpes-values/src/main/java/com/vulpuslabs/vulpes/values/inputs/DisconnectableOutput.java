package com.vulpuslabs.vulpes.values.inputs;

import com.vulpuslabs.vulpes.values.api.BooleanConsumer;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class DisconnectableOutput implements DoubleConsumer {

    private static final DoubleConsumer NO_OP = (x) -> {};
    private boolean isConnected;
    private BooleanConsumer onConnectionStatusChanged;
    private final DoubleConsumer inputWriter;
    private DoubleConsumer valueConsumer;

    public DisconnectableOutput(DoubleConsumer inputWriter) {
        this.onConnectionStatusChanged = BooleanConsumer.DO_NOTHING;
        this.inputWriter = inputWriter;
        this.valueConsumer = NO_OP;
    }

    public void setIsConnected(boolean isConnected) {
        if (this.isConnected != isConnected) {
            this.isConnected = isConnected;
            valueConsumer = isConnected ? inputWriter : NO_OP;
            onConnectionStatusChanged.accept(isConnected);
        }
    }

    public DisconnectableOutput onConnectionStatusChanged(BooleanConsumer handler) {
        onConnectionStatusChanged = handler;
        return this;
    }

    @Override
    public void accept(double value) {
        valueConsumer.accept(value);
    }

    public boolean isConnected() {
        return isConnected;
    }
}
