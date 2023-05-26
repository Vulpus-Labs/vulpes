package com.vulpuslabs.vulpes.buffers;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class BufferBoundedIndex implements IntConsumer, IntSupplier {

    private final BufferSize bufferSize;
    private int currentValue;

    public BufferBoundedIndex(BufferSize bufferSize, int currentValue) {
        this.bufferSize = bufferSize;
        this.currentValue = bufferSize.wrap(currentValue);
    }

    public void increment() {
        currentValue = bufferSize.wrap(currentValue + 1);
    }

    public void decrement() {
        currentValue = bufferSize.wrap(currentValue + bufferSize.getLastIndex());
    }

    @Override
    public void accept(int value) {
        currentValue = bufferSize.wrap(value);
    }

    @Override
    public int getAsInt() {
        return currentValue;
    }

}
