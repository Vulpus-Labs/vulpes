package com.vulpuslabs.vulpes.buffers;

import com.vulpuslabs.vulpes.buffers.api.BufferRandomAccess;
import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.api.SampleCount;

public class Buffer {

    private final BufferSize bufferSize;
    private final SampleCount sampleCount;
    private final double[] data;
    private final BufferBoundedIndex writeIndex;

    public Buffer(BufferSize bufferSize, SampleCount sampleCount) {
        this.bufferSize = bufferSize;
        this.sampleCount = sampleCount;
        this.data = new double[bufferSize.getSize() << sampleCount.getShift()];
        this.writeIndex = new BufferBoundedIndex(bufferSize, 0);
    }

    public int getSize() {
        return bufferSize.getSize();
    }

    public SampleData createSampleData() {
        return new SampleData(sampleCount);
    }

    public void writeNext(double sample) {
        data[writeIndex.getAsInt()] = sample;
        writeIndex.increment();
    }

    public void writeNext(SampleData sampleData) {
        sampleData.writeTo(data, writeIndex.getAsInt() << sampleCount.getShift());
        writeIndex.increment();
    }

    public BufferReadPointer createReadPointer(int offset) {
        BufferReadPointer newPointer = new BufferReadPointer(
                sampleCount,
                data,
                new BufferBoundedIndex(bufferSize, 0));
        movePointerTo(offset, newPointer);
        return newPointer;
    }

    public void movePointerTo(int offset, BufferReadPointer pointer) {
        pointer.moveTo(writeIndex.getAsInt() - offset);
    }

    public double[] exportData() {
        double[] exported = new double[data.length];
        System.arraycopy(data, 0, exported, 0, data.length);
        return exported;
    }
}
