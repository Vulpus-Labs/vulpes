package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.buffers.api.BufferRandomAccess;
import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public class Granule {

    private double pos;
    private double delta;
    private int count;

    private double curvePos;

    private double curveDelta;

    public void initialise(double startPos, double endPos, int count) {
        pos = startPos;
        curveDelta = 1.0 / count;
        delta = (endPos - startPos) * curveDelta;
        curvePos = 0;
        this.count = count;
    }

    public void copyFrom(Granule other) {
        this.pos = other.pos;
        this.delta = other.delta;
        this.count = other.count;
        this.curvePos = other.curvePos;
        this.curveDelta = other.curveDelta;
    }

    public double nextSample(BufferRandomAccess bufferRandomAccess, GranuleCurve curve) {
        // Get sample
        double sample = curve.apply(
                bufferRandomAccess.getSampleAtOffset(pos),
                curvePos);

        pos += delta;
        curvePos += curveDelta;
        count--;

        return sample;
    }

    public boolean isFinished() {
        return count == 0;
    }
}
