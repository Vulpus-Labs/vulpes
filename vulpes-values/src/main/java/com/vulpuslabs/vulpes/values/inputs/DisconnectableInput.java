package com.vulpuslabs.vulpes.values.inputs;

import com.vulpuslabs.vulpes.values.api.BooleanConsumer;

import java.util.function.DoubleSupplier;

public class DisconnectableInput implements DoubleSupplier {

    private static final DoubleSupplier CONSTANT_ZERO = () -> 0.0;
    private boolean isConnected;
    private BooleanConsumer onConnectionStatusChanged;
    private final DoubleSupplier inputReader;
    private DoubleSupplier valueSupplier;

    public DisconnectableInput(DoubleSupplier inputReader) {
        this.onConnectionStatusChanged = BooleanConsumer.DO_NOTHING;
        this.inputReader = inputReader;
        this.valueSupplier = CONSTANT_ZERO;
    }

    public void setIsConnected(boolean isConnected) {
        if (this.isConnected != isConnected) {
            this.isConnected = isConnected;
            valueSupplier = isConnected ? inputReader : CONSTANT_ZERO;
            onConnectionStatusChanged.accept(isConnected);
        }
    }

    public DisconnectableInput onConnectionStatusChanged(BooleanConsumer handler) {
        onConnectionStatusChanged = handler;
        return this;
    }

    @Override
    public double getAsDouble() {
        return valueSupplier.getAsDouble();
    }

    public boolean isConnected() {
        return isConnected;
    }
}
