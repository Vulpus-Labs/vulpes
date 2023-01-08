package com.vulpuslabs.vulpes.buffers;

import com.vulpuslabs.vulpes.buffers.api.SampleCount;

public final class SampleData {

    private final SampleCount sampleCount;
    private final double[] data;

    public SampleData(SampleCount sampleCount) {
        this.sampleCount = sampleCount;
        this.data = new double[sampleCount.getCount()];
    }

    public SampleCount getSampleCount() {
        return sampleCount;
    }

    public void readFrom(double[] source, int start) {
        System.arraycopy(source, start, data, 0, sampleCount.getCount());
    }

    public void writeTo(double[] destination, int start) {
        System.arraycopy(data, 0, destination, start, sampleCount.getCount());
    }

    public double getSample(int index) {
        return data[index];
    }

    public void setSample(int index, double sample) {
        data[index] = sample;
    }

    public void setSamples(double...samples) {
        System.arraycopy(samples, 0, data, 0, sampleCount.getCount());
    }
}
