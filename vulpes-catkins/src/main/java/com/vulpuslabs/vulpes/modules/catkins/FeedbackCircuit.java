package com.vulpuslabs.vulpes.modules.catkins;

import com.vulpuslabs.vulpes.filter.legacy.HighShelfFilter;
import com.vulpuslabs.vulpes.filter.legacy.LowShelfFilter;
import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.smoothed.DCBlocker;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class FeedbackCircuit implements DoubleSupplier, DoubleConsumer {

    private final DoubleSupplier feedbackAmount;
    private final DoubleTransformer cleanup;
    private double filterSample;

    public FeedbackCircuit(DoubleSupplier feedbackAmount) {
        this.feedbackAmount = feedbackAmount;
        var highShelf = new HighShelfFilter(48000);
        var lowShelf = new LowShelfFilter(48000);
        highShelf.configure(20e3, 0.0, -6.0);
        lowShelf.configure(100, 0.0, -6.0);
        cleanup = new DCBlocker()
                .andThen(lowShelf)
                .andThen(highShelf)
                .andThen(this::saturate);
    }

    private double saturate(double value) {
        return 5.0 * Approximate.tanh(value * 0.2);
    }

    @Override
    public void accept(double value) {
        filterSample = cleanup.apply(value);
    }

    @Override
    public double getAsDouble() {
        return filterSample * feedbackAmount.getAsDouble();
    }
}
