package com.vulpuslabs.vulpes.filter.resampling;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public class Resampler implements DoubleTransformer {

    private final int factor;
    private final ResampleFilter upFilter;
    private final ResampleFilter downFilter;
    private final DoubleTransformer processor;

    public Resampler(int baseSampleRate,
                     int factor,
                     double freq,
                     DoubleTransformer processor) {
        this.factor = factor;
        int sampleRate = baseSampleRate * this.factor;

        this.upFilter = new ResampleFilter(sampleRate, freq);
        this.downFilter = new ResampleFilter(sampleRate, freq);

        this.processor = processor;
    }

    @Override
    public double apply(double value) {
        double result = downFilter.apply(
                processor.apply(
                        factor * upFilter.apply(value)));

        for (int i = 1; i < factor; i++) {
            downFilter.apply(
                    processor.apply(
                            factor * upFilter.apply(0)));
        }

        return result;
    }
}
