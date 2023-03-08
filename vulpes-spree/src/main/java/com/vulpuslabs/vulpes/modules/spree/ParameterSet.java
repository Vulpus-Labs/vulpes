package com.vulpuslabs.vulpes.modules.spree;

import com.vulpuslabs.vulpes.values.drift.DriftValue;

public class ParameterSet {

    private double time;
    private double width;
    private double frequencyHz;
    private double feedback;
    private double mix;

    public void set(double time, double width, double frequencyHz, double feedback, double mix) {
        this.time = time * 48;
        this.width = width;
        this.frequencyHz = frequencyHz;
        this.feedback = feedback;
        this.mix = mix;
    }

    public double getTime(DriftValue driftValue) {
        return 192 + ((time - 192) * driftValue.getValue());
    }

    public double getDepth(DriftValue driftValue) {
        return 80.0 * width * driftValue.getValue();
    }

    public double getFrequencyHz(DriftValue driftValue) {
        return frequencyHz * driftValue.getValue();
    }

    public double getFeedback(DriftValue driftValue) {
        return feedback * driftValue.getValue();
    }

    public double getMix() {
        return mix;
    }
}
