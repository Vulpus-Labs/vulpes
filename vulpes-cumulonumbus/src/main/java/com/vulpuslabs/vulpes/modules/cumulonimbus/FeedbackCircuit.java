package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.filter.legacy.HighShelfFilter;
import com.vulpuslabs.vulpes.filter.legacy.LowShelfFilter;
import com.vulpuslabs.vulpes.values.smoothed.DCBlocker;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class FeedbackCircuit implements DoubleSupplier, DoubleConsumer {

    private double filterSample;

    private final HighShelfFilter highShelf;
    private final LowShelfFilter lowShelf;
    private final DCBlocker dcBlocker = new DCBlocker();

    public FeedbackCircuit() {
        highShelf = new HighShelfFilter(48000);
        lowShelf = new LowShelfFilter(48000);
        highShelf.configure(20e3, 0.0, -6.0);
        lowShelf.configure(100, 0.0, -6.0);
    }

    @Override
    public void accept(double value) {
        filterSample = highShelf.apply(lowShelf.apply(dcBlocker.apply(value)));
    }

    @Override
    public double getAsDouble() {
        return filterSample;
    }
}
