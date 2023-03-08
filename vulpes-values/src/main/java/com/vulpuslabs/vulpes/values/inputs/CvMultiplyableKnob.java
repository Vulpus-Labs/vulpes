package com.vulpuslabs.vulpes.values.inputs;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.ranges.Range;
import com.vulpuslabs.vulpes.values.smoothed.SmoothedValue;

import java.util.function.DoubleSupplier;

public class CvMultiplyableKnob implements DoubleSupplier {

    private final DoubleSupplier cvValue;
    private final double minKnobValue;
    private DoubleTransformer transformer;
    private final SmoothedValue smoothedValue;
    private boolean cvIsConnected;
    private double knobValue;

    public CvMultiplyableKnob(DoubleSupplier cvValue, double minKnobValue, double initialKnobValue, double decayRate) {
        this.cvValue = cvValue;
        this.minKnobValue = minKnobValue;
        this.smoothedValue = new SmoothedValue(decayRate);
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

    public double getUnsmoothed() {
        return cvIsConnected
                ? transformer.apply(cvValue.getAsDouble())
                : knobValue;
    }

    @Override
    public double getAsDouble() {
        smoothedValue.accept(getUnsmoothed());

        return smoothedValue.getAsDouble();
    }
}
