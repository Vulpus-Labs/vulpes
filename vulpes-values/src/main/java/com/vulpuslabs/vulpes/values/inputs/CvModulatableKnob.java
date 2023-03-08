package com.vulpuslabs.vulpes.values.inputs;

import com.vulpuslabs.vulpes.values.events.NotificationReceiver;
import com.vulpuslabs.vulpes.values.ranges.Range;
import com.vulpuslabs.vulpes.values.smoothed.SmoothedValue;

import java.util.function.DoubleSupplier;

public class CvModulatableKnob implements DoubleSupplier {

    private final double bottom;
    private final double top;
    private final DoubleSupplier cvValue;
    private final DoubleSupplier knobValue;
    private final DoubleSupplier modulationAmount;
    private DoubleSupplier outputValue;

    public CvModulatableKnob(double lowerBound,
                             double upperBound,
                             DisconnectableInput cvValue,
                             DoubleSupplier knobValue,
                             DoubleSupplier modulationAmount) {
        this.bottom = lowerBound;
        this.top = upperBound;
        this.cvValue = cvValue;
        this.knobValue = knobValue;
        this.modulationAmount = modulationAmount;
        this.outputValue = knobValue;

        cvValue.onConnectionStatusChanged(this::setCvIsConnected);
    }

    public void setCvIsConnected(boolean cvIsConnected) {
        if (cvIsConnected) {
            outputValue = this::getTransformedCvValue;
        } else {
            outputValue = knobValue;
        }
    }

    private double getTransformedCvValue() {
        var cv = (cvValue.getAsDouble() * 0.1) + 0.5;

        var mod = modulationAmount.getAsDouble();
        var knob = knobValue.getAsDouble();

        if (mod <= 0.0) {
            var lowerBound = knob + ((knob - bottom) * mod);
            return lowerBound + ((knob - lowerBound) * cv);
        }

        var upperBound = knob + ((top - knob) * mod);
        return knob + ((upperBound - knob) * cv);
    }

    @Override
    public double getAsDouble() {
        return outputValue.getAsDouble();
    }
}
