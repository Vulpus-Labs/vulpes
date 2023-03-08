package com.vulpuslabs.vulpes.modules.crosstalk;

import com.vulpuslabs.vulpes.values.api.IndexedDoubleSupplier;

import java.util.function.DoubleSupplier;

public class InputBus {

    private final DoubleSupplier amount;
    private final DoubleSupplier ringmodAmount;
    private final DoubleSupplier centreFreq;
    private final DoubleSupplier mix;
    private final IndexedDoubleSupplier inputJack;

    public InputBus(DoubleSupplier amount,
    	DoubleSupplier ringmodAmount,
    	DoubleSupplier centreFreq,
        DoubleSupplier mix,
    	IndexedDoubleSupplier inputJack) {
        this.amount = amount;
        this.ringmodAmount = ringmodAmount;
        this.centreFreq = centreFreq;
        this.mix = mix;
        this.inputJack = inputJack;
    }

    public double getBleedAmount() {
        return amount.getAsDouble();
    }

    public double getRingmodAmount() {
        return ringmodAmount.getAsDouble();
    }
    
    public double getCentreFrequency() {
    	return centreFreq.getAsDouble();
   }

    public double getMix() {
        return mix.getAsDouble();
    }

    public void readSamples(int channelCount, double[] inputSamples) {
        for (int i = 0; i < channelCount; i++) {
            inputSamples[i] = inputJack.getAsDouble(i);
        }
    }

    public void passThroughTo(int channelCount, OutputBus outputBus) {
        for (int i = 0; i < channelCount; i++) {
            outputBus.writeOutput(i, inputJack.getAsDouble(i));
        }
    }
}
