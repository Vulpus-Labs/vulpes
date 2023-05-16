package com.vulpuslabs.vulpes.buffers;

import com.vulpuslabs.vulpes.buffers.api.BufferRandomAccess;

public class FancyBufferRandomAccess implements BufferRandomAccess {

    private final Buffer buffer;
    private final BufferReadPointer readPointer;
    private final HermiteCoefficients coefficients = new HermiteCoefficients();

    public FancyBufferRandomAccess(Buffer buffer) {
        this.buffer = buffer;
        readPointer = buffer.createReadPointer(0);
    }

    @Override
    public double getSampleAtOffset(double offset) {
        int wholeOffset = (int) Math.ceil(offset);
        buffer.movePointerTo(wholeOffset + 4, readPointer);

        coefficients.set(wholeOffset - offset);

        return coefficients.apply(
                readPointer.readNext(),
                readPointer.readNext(),
                readPointer.readNext(),
                readPointer.readNext());
    }

    @Override
    public void getSampleAtOffset(double offset, SampleData[] readBuffer, SampleData target) {
        int wholeOffset = (int) Math.ceil(offset);
        buffer.movePointerTo(wholeOffset + 4, readPointer);

        coefficients.set(wholeOffset - offset);

        readPointer.readNext(readBuffer[0]);
        readPointer.readNext(readBuffer[1]);
        readPointer.readNext(readBuffer[2]);
        readPointer.readNext(readBuffer[3]);

        for (int i=0; i < target.getSampleCount().getCount(); i++) {
            target.setSample(i, coefficients.apply(
                    readBuffer[0].getSample(i),
                    readBuffer[1].getSample(i),
                    readBuffer[2].getSample(i),
                    readBuffer[3].getSample(i)));
        }
    }
}
