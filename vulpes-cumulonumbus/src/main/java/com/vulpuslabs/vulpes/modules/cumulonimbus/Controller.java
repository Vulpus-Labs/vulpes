package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.buffers.Buffer;
import com.vulpuslabs.vulpes.buffers.LinearInterpolatingBufferRandomAccess;
import com.vulpuslabs.vulpes.buffers.api.BufferRandomAccess;
import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.api.SampleCount;
import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.random.RandomDouble;

public class Controller {

    private double offset;
    private double diffusion;
    private int length;
    private double speed;
    private int requiredSize;

    private final GranuleTable cloud;
    private final Buffer buffer;
    private final BufferRandomAccess bufferRandomAccess;

    private final RandomDouble randomDouble = new RandomDouble(0);

    public Controller(int maxSize) {
        this.cloud = new GranuleTable(maxSize, this::newGranule);
        this.buffer = new Buffer(BufferSize.BUFFER_64k, SampleCount.MONO);
        this.bufferRandomAccess = new LinearInterpolatingBufferRandomAccess(buffer);
    }

    public double processSample(double inputSample) {
        buffer.writeNext(inputSample);
        return cloud.readSample(bufferRandomAccess, this::cosCurve, false);
    }

    private double cosCurve(double sample, double pos) {
        return sample * (1.0 - Approximate.cosUnit(pos));
    }

    private void newGranule(Granule granule) {
        double diffusedOffset = offset + randomDouble.getAsDouble() * diffusion;

        /*
        Complicated sum. At speed = 1, endPos should be startPos.
        At speed = 2, endPos should be startPos - length:
         */

        //granule.initialise(offset + randomDouble.getAsDouble() * diffusion);
    }
}
