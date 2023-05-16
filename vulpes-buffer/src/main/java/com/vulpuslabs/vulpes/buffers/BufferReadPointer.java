package com.vulpuslabs.vulpes.buffers;

import com.vulpuslabs.vulpes.buffers.api.SampleCount;

public class BufferReadPointer {

    private final SampleCount sampleCount;
    private final double[] data;
    private final BufferBoundedIndex index;

    public BufferReadPointer(SampleCount sampleCount,
                             double[] data,
                             BufferBoundedIndex index) {
        this.sampleCount = sampleCount;
        this.data = data;
        this.index = index;
    }

    public SampleData createSampleData() {
        return new SampleData(sampleCount);
    }

    public double readNext() {
        var result = data[index.getAsInt()];
        index.increment();
        return result;
    }

    public double readNextReverse() {
        var result = data[index.getAsInt()];
        index.decrement();
        return result;
    }

    public void readNext(SampleData target) {
        target.readFrom(data, index.getAsInt() << sampleCount.getShift());
        index.increment();
    }

    public void moveTo(int index) {
        this.index.accept(index);
    }

    public void readNextReverse(SampleData target) {
        target.readFrom(data, index.getAsInt() << sampleCount.getShift());
        index.decrement();
    }
}
