package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;

import java.util.function.DoubleSupplier;

public class StereoFeedbackCircuit {

    private final FeedbackCircuit left;
    private final FeedbackCircuit right;
    private double feedbackAmountValue;

    public StereoFeedbackCircuit() {
        left = new FeedbackCircuit();
        right = new FeedbackCircuit();
    }

    public void accept(StereoSample sample) {
        left.accept(sample.getLeft());
        right.accept(sample.getRight());
    }

    public void getAsStereo(StereoSample target) {
        target.set(left.getAsDouble(), right.getAsDouble());
    }

}
