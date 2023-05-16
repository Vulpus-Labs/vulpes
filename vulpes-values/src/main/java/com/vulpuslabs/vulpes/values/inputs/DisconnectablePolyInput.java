package com.vulpuslabs.vulpes.values.inputs;

import com.vulpuslabs.vulpes.values.api.BooleanConsumer;
import com.vulpuslabs.vulpes.values.api.IndexedDoubleSupplier;

import java.util.function.DoubleSupplier;

public class DisconnectablePolyInput implements IndexedDoubleSupplier {

    private static final IndexedDoubleSupplier CONSTANT_ZERO = (i) -> 0.0;
    private boolean isConnected;
    private final IndexedDoubleSupplier inputReader;
    private IndexedDoubleSupplier valueSupplier;

    public DisconnectablePolyInput(IndexedDoubleSupplier inputReader) {
        this.inputReader = inputReader;
        this.valueSupplier = CONSTANT_ZERO;
    }

    public void setIsConnected(boolean isConnected) {
        if (this.isConnected != isConnected) {
            this.isConnected = isConnected;
            this.valueSupplier = isConnected ? inputReader : CONSTANT_ZERO;
        }
    }

    @Override
    public double getAsDouble(int index) {
        return valueSupplier.getAsDouble(index);
    }

    public boolean isConnected() {
        return isConnected;
    }
}
