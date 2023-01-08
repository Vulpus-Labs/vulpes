package com.vulpuslabs.vulpes.buffers.api;

public enum SampleCount {

    MONO(0),
    STEREO(1),
    POLY(4);

    private final int shift;
    private final int count;

    private SampleCount(int shift) {
        this.shift = shift;
        this.count = 1 << shift;
    }

    public int getShift() {
        return shift;
    }

    public int getCount() {
        return count;
    }
}
