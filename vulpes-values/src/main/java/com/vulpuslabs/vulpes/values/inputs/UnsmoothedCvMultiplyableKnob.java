package com.vulpuslabs.vulpes.values.inputs;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.ranges.Range;
import com.vulpuslabs.vulpes.values.smoothed.SmoothedValue;

import java.util.function.DoubleSupplier;

public class UnsmoothedCvMultiplyableKnob implements DoubleSupplier {

    private final DoubleSupplier cvValue;
    private final double minKnobValue;
    private DoubleTransformer transformer;
    private boolean cvIsConnected;
    private double knobValue;

    public UnsmoothedCvMultiplyableKnob(DoubleSupplier cvValue, double minKnobValue, double initialKnobValue) {
        this.cvValue = cvValue;
        this.minKnobValue = minKnobValue;
        this.knobValue = initialKnobValue;
        transformer = Range.CV_BIPOLAR.clampTo(new Range(minKnobValue, initialKnobValue));
    }

    public void setCvIsConnected(boolean cvIsConnected) {
        this.cvIsConnected = cvIsConnected;
        if (cvIsConnected) {
            transformer = Range.CV_BIPOLAR.clampTo(new Range(minKnobValue, knobValue));
        }
    }

    public void setKnobValue(double knobValue) {
        this.knobValue = knobValue;
        if (cvIsConnected) {
            transformer = Range.CV_BIPOLAR.clampTo(new Range(minKnobValue, knobValue));
        }
    }

    @Override
    public double getAsDouble() {
        return cvIsConnected
            ? transformer.apply(cvValue.getAsDouble())
            : knobValue;
    }
}
