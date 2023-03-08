package com.vulpuslabs.vulpes.modules.crosstalk;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.random.RandomDouble;
import com.vulpuslabs.vulpes.values.ranges.Range;

import java.util.function.DoubleSupplier;

public class Controller {

    @FunctionalInterface
    interface BleedFunction {
        double getBleed(int channel);
    }

    private final InputBus inputBus;
    private final OutputBus outputBus;
    private final DoubleSupplier noise = Range.UNIT_UNIPOLAR.to(Range.CV_BIPOLAR)
            .transforming(new RandomDouble(1000));
    private int channelCount;
    private int offset;
    private boolean isNoiseEnabled;
    private BleedFunction bleedFunction;
    private final FilterConfiguration filterConfiguration;
    private DoubleTransformer[] filters;
    private double[] inputSamples;

    public Controller(InputBus inputBus, OutputBus outputBus, int channelCount) {
        this.inputBus = inputBus;
        this.outputBus = outputBus;
        this.filterConfiguration = new FilterConfiguration(48000, 10e3);
        setChannelCount(channelCount);
    }

    public void setChannelCount(int channelCount) {
        if (channelCount != this.channelCount) {
            this.channelCount = channelCount;
            this.offset = channelCount >> 1;
            inputSamples = new double[channelCount];
            filters = new SimpleTiltFilter[channelCount];
            for (int i = 0; i < channelCount; i++) {
                filters[i] = SimpleTiltFilter.create(4.0, filterConfiguration);
            }
            bleedFunction = getBleedFunction();
        }
    }

    public void setNoiseEnabled(boolean isNoiseEnabled) {
        this.isNoiseEnabled = isNoiseEnabled;
        bleedFunction = getBleedFunction();
    }

    private BleedFunction getBleedFunction() {
        boolean channelCountIsEven = (channelCount & 1) == 0;
        if (isNoiseEnabled) {
            return channelCountIsEven
                    ? this::getCrosstalkSampleEvenNoisy
                    : this::getCrosstalkSampleOddNoisy;
        }
        return channelCountIsEven
                ? this::getCrosstalkSampleEvenClean
                : this::getCrosstalkSampleOddClean;
    }

    private double getCrosstalkSampleEvenClean(int channel) {
        int source = (channel + offset) % channelCount;
        return inputSamples[source];
    }

    private double getCrosstalkSampleOddClean(int channel) {
        int source = (channel + offset) % channelCount;
        int next = (source + 1) % channelCount;
        return (inputSamples[source] + inputSamples[next]) * 0.5;
    }

    private double getCrosstalkSampleEvenNoisy(int channel) {
        return getCrosstalkSampleEvenClean(channel) + noise.getAsDouble() * 0.005;
    }

    private double getCrosstalkSampleOddNoisy(int channel) {
        return getCrosstalkSampleOddClean(channel) + noise.getAsDouble() * 0.005;
    }

    public void processSample() {
        if (!outputBus.getOutputIsConnected()) {
            return;
        }

        inputBus.readSamples(channelCount, inputSamples);

        double bleedAmount = inputBus.getBleedAmount();
        double ringmodAmount = inputBus.getRingmodAmount() * 0.2;

        double centreFreq = inputBus.getCentreFrequency();
        filterConfiguration.setCentreFrequency(centreFreq);

        double mix = inputBus.getMix();
        double oneMinusMix = 1.0 - mix;

        for (int c = 0; c < channelCount; c++) {
            double crosstalk = 0;
            for (int j = 0; j < channelCount; j++) {
                if (c != j) {
                    crosstalk += inputSamples[c] * inputSamples[j];
                }
            }
            crosstalk *= ringmodAmount;
            crosstalk += bleedFunction.getBleed(c) * bleedAmount;

            outputBus.writeOutput(c, inputSamples[c] * oneMinusMix + filters[c].apply(crosstalk) * mix);
        }
    }

    public void processBypassedSample() {
        inputBus.passThroughTo(channelCount, outputBus);
    }
}
