package com.vulpuslabs.vulpes.modules.torment;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public class Resampler {

    private final int factor;
    private final ResampleFilter carrierUpFilter;
    private final ResampleFilter modulationUpFilter;
    private final ResampleFilter downFilter;
    private Controller.Tormentor processor;

    public Resampler(int baseSampleRate,
                     int factor,
                     double freq,
                     Controller.Tormentor processor) {
        this.factor = factor;
        int sampleRate = baseSampleRate * this.factor;

        this.carrierUpFilter = new ResampleFilter(sampleRate, freq);
        this.modulationUpFilter = new ResampleFilter(sampleRate, freq);
        this.downFilter = new ResampleFilter(sampleRate, freq);

        this.processor = processor;
    }

    public void setTormentor(Controller.Tormentor processor) {
        this.processor = processor;
    }

    public double apply(double carrier, double modulation) {
        var upFilteredCarrier = factor * carrierUpFilter.apply(carrier);
        var upFilteredModulation = factor * modulationUpFilter.apply(modulation);
        double result = downFilter.apply(
                processor.torment(upFilteredCarrier, upFilteredModulation));

        for (int i = 1; i < factor; i++) {
            downFilter.apply(
                    processor.torment(
                            factor * carrierUpFilter.apply(0),
                            factor * modulationUpFilter.apply(0)));
        }

        return result;
    }
}
