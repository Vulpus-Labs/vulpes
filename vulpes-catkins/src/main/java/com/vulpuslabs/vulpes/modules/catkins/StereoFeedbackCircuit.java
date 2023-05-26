package com.vulpuslabs.vulpes.modules.catkins;

import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;

import java.util.function.DoubleSupplier;

public class StereoFeedbackCircuit {

    private final FeedbackCircuit left;
    private final FeedbackCircuit right;
    private final DoubleSupplier feedbackAmount;
    private double feedbackAmountValue;

    public StereoFeedbackCircuit(DoubleSupplier feedbackAmount) {
        left = new FeedbackCircuit(this::getFeedbackAmountValue);
        right = new FeedbackCircuit(this::getFeedbackAmountValue);
        this.feedbackAmount = feedbackAmount;
    }

    public void accept(StereoSample sample) {
        left.accept(sample.getLeft());
        right.accept(sample.getRight());
    }

    public void getAsStereo(StereoSample target) {
        feedbackAmountValue = feedbackAmount.getAsDouble();
        target.set(left.getAsDouble(), right.getAsDouble());
    }

    private double getFeedbackAmountValue() {
        return feedbackAmountValue;
    }
}
