package com.vulpuslabs.vulpes.buffers.stereo;

import com.vulpuslabs.vulpes.buffers.BufferBoundedIndex;
import com.vulpuslabs.vulpes.buffers.HermiteCoefficients;
import com.vulpuslabs.vulpes.buffers.api.BufferSize;

public class StereoBuffer {

    private final double[] samples;
    private final BufferBoundedIndex writePtr;
    private final BufferBoundedIndex readPtr;

    private final StereoSample bufferSample1 = new StereoSample();
    private final StereoSample bufferSample2 = new StereoSample();
    private final StereoSample bufferSample3 = new StereoSample();
    private final StereoSample bufferSample4 = new StereoSample();
    private final HermiteCoefficients coefficients = new HermiteCoefficients();

    public StereoBuffer(BufferSize bufferSize) {
        this.samples = new double[bufferSize.getSize() << 1];
        this.writePtr = new BufferBoundedIndex(bufferSize, 0);
        this.readPtr = new BufferBoundedIndex(bufferSize, 0);
    }

    public void write(StereoSample sample) {
        write(sample.getLeft(), sample.getRight());
    }

    public void write(double left, double right) {
        int ptr = writePtr.getAsInt() << 1;
        samples[ptr++] = left;
        samples[ptr] = right;
        writePtr.increment();
    }

    private void setReadPtr(int offset) {
        readPtr.accept(writePtr.getAsInt() - (offset + 1));
    }

    public void read(int offset, StereoSample target) {
        setReadPtr(offset);
        read(target);
    }

    private void read(StereoSample target) {
        int ptr = readPtr.getAsInt() << 1;
        target.set(samples[ptr++], samples[ptr]);
    }

    public void readFractional(double offset, StereoSample target) {
        int wholeOffset = (int) offset;
        double fraction = offset - wholeOffset;

        setReadPtr(wholeOffset);
        read(target);
        readPtr.decrement();
        read(bufferSample1);

        target.mix(bufferSample1, fraction);
    }

    public void readFractionalHermite(double offset, StereoSample target) {
        if (offset < 3.0) {
            // not enough samples, go linear
            readFractional(offset, target);
            return;
        }

        int wholeOffset = (int) Math.ceil(offset);
        coefficients.set(wholeOffset - offset);

        setReadPtr(wholeOffset);
        read(bufferSample1);
        readPtr.increment();
        read(bufferSample2);
        readPtr.increment();
        read(bufferSample3);
        readPtr.increment();
        read(bufferSample4);

        target.set(
                coefficients.apply(
                        bufferSample1.getLeft(),
                        bufferSample2.getLeft(),
                        bufferSample3.getLeft(),
                        bufferSample4.getLeft()
                ),
                coefficients.apply(
                        bufferSample1.getRight(),
                        bufferSample2.getRight(),
                        bufferSample3.getRight(),
                        bufferSample4.getRight()
                )
        );
    }
}
