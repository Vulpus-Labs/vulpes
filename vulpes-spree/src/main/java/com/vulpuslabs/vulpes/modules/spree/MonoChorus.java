package com.vulpuslabs.vulpes.modules.spree;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.mono.MonoBuffer;
import com.vulpuslabs.vulpes.filter.legacy.HighShelfFilter;
import com.vulpuslabs.vulpes.filter.legacy.api.Filter;
import com.vulpuslabs.vulpes.values.lfo.FakeSinLfo;
import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.clipping.SoftClipper;
import com.vulpuslabs.vulpes.values.lfo.TriangleLfo;
import com.vulpuslabs.vulpes.values.oscillators.OscillatorFromGenerator;

public class MonoChorus {

    private final MonoBuffer buffer;
    private final DoubleTransformer feedbackFilter;
    private OscillatorFromGenerator lfo;
    private double feedbackSample;

    public MonoChorus() {
        buffer = new MonoBuffer(BufferSize.BUFFER_4k);
        lfo = new FakeSinLfo();
        Filter highShelf = new HighShelfFilter(48000);
        highShelf.configure(10000, 0.0, -3.0);
        SoftClipper softClipper = new SoftClipper(1.0 / 3.0);
        feedbackFilter = softClipper.andThen(highShelf);
    }

    public void setTriangle() {
        this.lfo = new TriangleLfo(lfo.getPosition());
    }

    public void setSine() {
        this.lfo = new FakeSinLfo(lfo.getPosition());
    }

    public double processSample(double sample, double time, double width, double frequencyHz, double feedback, double mix) {
        buffer.write(
                feedbackFilter.apply(sample + (feedbackSample * feedback)));

        lfo.setFrequencyHz(frequencyHz);
        feedbackSample = buffer.readFractionalHermite((lfo.getAsDouble() * width)
                + width + time);

        var amount = feedbackSample - sample;
        return sample + (amount * mix);
    }
}
