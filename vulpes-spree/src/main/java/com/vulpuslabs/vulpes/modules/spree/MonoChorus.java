package com.vulpuslabs.vulpes.modules.spree;

import com.vulpuslabs.vulpes.buffers.Buffer;
import com.vulpuslabs.vulpes.buffers.BufferReadPointer;
import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.api.SampleCount;
import com.vulpuslabs.vulpes.filter.HighShelfFilter;
import com.vulpuslabs.vulpes.filter.api.Filter;
import com.vulpuslabs.vulpes.values.lfo.FakeSinLfo;
import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.clipping.SoftClipper;
import com.vulpuslabs.vulpes.values.lfo.TriangleLfo;
import com.vulpuslabs.vulpes.values.oscillators.OscillatorFromGenerator;

public class MonoChorus {

    private final Buffer buffer;
    private final BufferReadPointer readPointer;
    private final DoubleTransformer feedbackFilter;
    private OscillatorFromGenerator lfo;
    private double feedbackSample;

    public MonoChorus() {
        buffer = new Buffer(BufferSize.BUFFER_4k, SampleCount.MONO);
        readPointer = buffer.createReadPointer(0);
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
        buffer.writeNext(
                feedbackFilter.apply(sample + (feedbackSample * feedback)));

        lfo.setFrequencyHz(frequencyHz);
        feedbackSample = getSampleAtOffset((lfo.getAsDouble() * width)
                + width + time);

        var amount = feedbackSample - sample;
        return sample + (amount * mix);
    }

    private double getSampleAtOffset(double fractionalOffset) {
        int wholeOffset = (int) Math.ceil(fractionalOffset);
        buffer.movePointerTo(wholeOffset + 1, readPointer);

        double n0 = readPointer.readNext();
        double n1 = readPointer.readNext();
        double n2 = readPointer.readNext();
        double n3 = readPointer.readNext();

        double dMinus1 = wholeOffset + 1.0 - fractionalOffset;
        if (dMinus1 == 0.0) {
            return n1;
        }

        double dMinus2 = dMinus1 - 1;
        double dMinus3 = dMinus2 - 1;
        double d = dMinus1 + 1;

        double halfDTimesDMinus3 = d * dMinus3 * 0.5;
        double oneSixthDMinus1TimesDMinus2 = dMinus1 * dMinus2 / 6.0;

        double h0 = -oneSixthDMinus1TimesDMinus2 * dMinus3;
        double h1 = halfDTimesDMinus3 * dMinus2;
        double h2 = -halfDTimesDMinus3 * dMinus1;
        double h3 = d * oneSixthDMinus1TimesDMinus2;

        return (n0 * h0) + (n1 * h1) + (n2 * h2) + (n3 * h3);
    }
}
