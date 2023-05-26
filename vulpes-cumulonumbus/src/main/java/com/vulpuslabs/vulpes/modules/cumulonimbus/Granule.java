package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.buffers.stereo.StereoBuffer;
import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;
import com.vulpuslabs.vulpes.values.Approximate;

public class Granule {

    private double pos;
    private double delta;
    private int count;

    private int fadeSamples;
    private int fadeCount;
    private double fade;
    private double fadeDelta;

    public void initialise(double startPos, double endPos, int count, double fadePercent) {
        pos = startPos;
        delta = (endPos - startPos) / count;
        this.count = count;

        fadeSamples = (int) Math.ceil(count * fadePercent * 0.5);
        fadeCount = fadeSamples;
        fade = 0;
        fadeDelta = 1.0 / fadeSamples;
    }

    public void nextSample(StereoBuffer buffer, StereoSample sample, double freezeDelta) {
        buffer.readFractional(pos, sample);
        sample.multiply(Approximate.sinusoid(fade));

        pos += delta;
        pos += freezeDelta;
        count--;

        if (count == fadeSamples) {
            fadeDelta = -fadeDelta;
            fadeCount = count;
        }

        if (fadeCount > 0) {
            fade += fadeDelta;
            fadeCount--;
        }
    }

    public boolean isFinished() {
        return count == 0;
    }
}
