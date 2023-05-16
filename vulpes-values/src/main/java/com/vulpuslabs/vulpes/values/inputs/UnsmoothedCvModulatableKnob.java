package com.vulpuslabs.vulpes.values.inputs;

import com.vulpuslabs.vulpes.values.events.UIEventConnector;
import com.vulpuslabs.vulpes.values.ranges.Range;

import java.util.function.DoubleSupplier;

public class UnsmoothedCvModulatableKnob implements DoubleSupplier {

    private final double bottom;
    private final double top;
    private final DoubleSupplier cvValue;
    private double knobValue;
    private double modulationAmount;
    private DoubleSupplier outputValue;

    private boolean cvIsConnected;

    public UnsmoothedCvModulatableKnob(double lowerBound,
                                       double upperBound,
                                       DoubleSupplier cvValue) {
        this.bottom = lowerBound;
        this.top = upperBound;
        this.cvValue = Range.CV_BIPOLAR.clampTo(Range.UNIT_UNIPOLAR)
                .transforming(cvValue);
        this.outputValue = this::getKnobValue;
    }

    public void setCvIsConnected(boolean cvIsConnected) {
        this.cvIsConnected = cvIsConnected;
    }

    public double getKnobValue() {
        return knobValue;
    }

    public void setKnobValue(double knobValue) {
        this.knobValue = knobValue;
    }

    public void setModulationAmount(double modulationAmount) {
        this.modulationAmount = modulationAmount;
    }

    @Override
    public double getAsDouble() {
        if (!cvIsConnected) {
            return knobValue;
        }

        var cv = cvValue.getAsDouble();

        if (modulationAmount <= 0.0) {
            var lowerBound = knobValue + ((knobValue - bottom) * modulationAmount);
            return lowerBound + ((knobValue - lowerBound) * cv);
        }

        var upperBound = knobValue + ((top - knobValue) * modulationAmount);
        return knobValue + ((upperBound - knobValue) * cv);
    }
}
