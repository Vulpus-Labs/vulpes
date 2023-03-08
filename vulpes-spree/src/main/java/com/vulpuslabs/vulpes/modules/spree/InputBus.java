package com.vulpuslabs.vulpes.modules.spree;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.api.IndexedDoubleSupplier;
import com.vulpuslabs.vulpes.values.inputs.CvModulatableKnob;

import java.util.function.DoubleSupplier;

public class InputBus {

    private static final double slowHzConv = 0.125;

    private final IndexedDoubleSupplier polyInput;

    private final DoubleSupplier timeKnob;
    private final DoubleSupplier depthKnob;
    private final DoubleSupplier speedKnob;
    private DoubleSupplier frequencyHzSupplier;
    private final DoubleSupplier feedbackKnob;
    private final DoubleSupplier mixKnob;

    public InputBus(IndexedDoubleSupplier polyInput,    
                    DoubleSupplier timeKnob,
                    DoubleSupplier depthKnob,
                    DoubleSupplier speedKnob,
                    DoubleSupplier feedbackKnob,
                    DoubleSupplier mixKnob) {
        this.polyInput = polyInput;
        this.timeKnob = timeKnob;
        this.depthKnob = depthKnob;
        DoubleTransformer speedTransformer = (speedHz) -> speedHz;

        this.speedKnob = speedKnob;
        this.frequencyHzSupplier = speedTransformer.transforming(speedKnob);
        this.feedbackKnob = feedbackKnob;
        this.mixKnob = mixKnob;
    }

    public void setIsSlow(boolean isSlow) {
        DoubleTransformer speedTransformer = isSlow
                ? (speedHz) -> speedHz * slowHzConv
                : (speedHz) -> speedHz;
        this.frequencyHzSupplier = speedTransformer.transforming(speedKnob);
    }

    public void setParameters(ParameterSet parameterSet) {
        parameterSet.set(
                timeKnob.getAsDouble(),
                depthKnob.getAsDouble(),
                frequencyHzSupplier.getAsDouble(),
                feedbackKnob.getAsDouble(),
                mixKnob.getAsDouble()
        );
    }

    public double getInput(int channel) {
        return polyInput.getAsDouble(channel);
    }

}
