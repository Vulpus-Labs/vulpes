package com.vulpuslabs.vulpes.buffers;

import com.vulpuslabs.vulpes.buffers.api.BufferRandomAccess;

public class LinearInterpolatingBufferRandomAccess implements BufferRandomAccess {

    private final Buffer buffer;
    private final BufferReadPointer readPointer;

    public LinearInterpolatingBufferRandomAccess(Buffer buffer) {
        this.buffer = buffer;
        readPointer = buffer.createReadPointer(0);
    }

    @Override
    public double getSampleAtOffset(double offset) {
        int wholeOffset = (int) offset;
        double rightAmount = offset - wholeOffset;
        double leftAmount = 1.0 - rightAmount;
        buffer.movePointerTo(wholeOffset + 1, readPointer);

        double left = readPointer.readNextReverse();
        double right = readPointer.readNextReverse();

        return left * leftAmount + right * rightAmount;
    }

    @Override
    public void getSampleAtOffset(double offset, SampleData[] readBuffer, SampleData target) {
        int wholeOffset = (int) offset;
        double rightAmount = offset - wholeOffset;
        double leftAmount = 1.0 - rightAmount;
        buffer.movePointerTo(wholeOffset + 1, readPointer);

        readPointer.readNextReverse(readBuffer[0]);
        readPointer.readNextReverse(readBuffer[1]);

        for (int i=0; i < target.getSampleCount().getCount(); i++) {
            target.setSample(i,
                    readBuffer[0].getSample(i) * leftAmount
                            + readBuffer[1].getSample(i) * rightAmount);
        }
    }
}
