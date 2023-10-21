package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.buffers.stereo.StereoBuffer;
import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;
import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.api.DoubleBiConsumer;
import com.vulpuslabs.vulpes.values.stereo.Pan;

public class Granule {

    private double pos;
    private double delta;
    private int count;

    private int fadeSamples;
    private int fadeCount;
    private double fade;
    private double fadeDelta;

    private final Pan pan = new Pan();

    public void initialise(double startPos, double endPos, int count, double panBalance, double fadePercent) {
        pos = startPos;
        delta = (endPos - startPos) / count;
        this.count = count;
        this.pan.set(panBalance);

        fadeSamples = (int) Math.ceil(count * fadePercent * 0.5);
        fadeCount = fadeSamples;
        fade = 0;
        fadeDelta = 1.0 / fadeSamples;
    }

    public void nextSample(StereoBuffer buffer, StereoSample sample, double freezeDelta) {
        buffer.readFractional(Math.abs(pos), sample);
        sample.multiply(Approximate.sinusoid(fade));
        sample.pan(pan);

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

    public void getPosition(DoubleBiConsumer posAndFade) {
        posAndFade.accept(pos, Approximate.sinusoid(fade));
    }
}
