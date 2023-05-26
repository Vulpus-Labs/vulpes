package com.vulpuslabs.vulpes.buffers.mono;

import com.vulpuslabs.vulpes.buffers.BufferBoundedIndex;
import com.vulpuslabs.vulpes.buffers.HermiteCoefficients;
import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;

public class MonoBuffer {

    private final double[] samples;
    private final BufferBoundedIndex writePtr;
    private final BufferBoundedIndex readPtr;
    private final HermiteCoefficients coefficients = new HermiteCoefficients();

    public MonoBuffer(BufferSize bufferSize) {
        this.samples = new double[bufferSize.getSize()];
        this.writePtr = new BufferBoundedIndex(bufferSize, 0);
        this.readPtr = new BufferBoundedIndex(bufferSize, 0);
    }

    public void write(double sample) {
        samples[writePtr.getAsInt()] = sample;
        writePtr.increment();
    }

    private void setReadPtr(int offset) {
        readPtr.accept(writePtr.getAsInt() - (offset + 1));
    }

    public double read(int offset) {
        setReadPtr(offset);
        return read();
    }

    private double read() {
        return samples[readPtr.getAsInt()];
    }

    public double readFractional(double offset) {
        int wholeOffset = (int) offset;
        double fraction = offset - wholeOffset;

        setReadPtr(wholeOffset);
        double first = read();
        readPtr.decrement();
        double second = read();

        return first * (1.0 - fraction) + second * fraction;
    }

    public double readFractionalHermite(double offset) {
        if (offset < 3.0) {
            // not enough samples, go linear
            return readFractional(offset);
        }

        int wholeOffset = (int) Math.ceil(offset);
        coefficients.set(wholeOffset - offset);

        double sample1 = read(wholeOffset);
        readPtr.increment();
        double sample2 = read();
        readPtr.increment();
        double sample3 = read();
        readPtr.increment();
        double sample4 = read();

        return coefficients.apply(sample1, sample2, sample3, sample4);
    }
}
