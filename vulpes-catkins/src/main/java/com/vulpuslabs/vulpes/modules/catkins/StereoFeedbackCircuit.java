package com.vulpuslabs.vulpes.modules.catkins;

import com.vulpuslabs.vulpes.buffers.SampleData;

import java.util.function.DoubleSupplier;

import static com.vulpuslabs.vulpes.values.api.stereo.Stereo.LEFT;
import static com.vulpuslabs.vulpes.values.api.stereo.Stereo.RIGHT;

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

    public void accept(SampleData sample) {
        left.accept(sample.getSample(LEFT));
        right.accept(sample.getSample(RIGHT));
    }

    public SampleData getAsStereo(SampleData target) {
        feedbackAmountValue = feedbackAmount.getAsDouble();
        target.setSample(LEFT, left.getAsDouble());
        target.setSample(RIGHT, right.getAsDouble());
        return target;
    }

    private double getFeedbackAmountValue() {
        return feedbackAmountValue;
    }
}
