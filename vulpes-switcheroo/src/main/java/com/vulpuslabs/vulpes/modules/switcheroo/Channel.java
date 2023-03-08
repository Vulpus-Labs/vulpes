package com.vulpuslabs.vulpes.modules.switcheroo;

import com.vulpuslabs.vulpes.values.inputs.CvMultiplyableKnob;

import java.util.function.DoubleSupplier;

public class Channel {

    private final DoubleSupplier input;
    private final CvMultiplyableKnob volume;
    private boolean inputIsConnected;

    public Channel(DoubleSupplier input, DoubleSupplier cvValue, double initialKnobValue) {
        this.input = input;
        this.volume = new CvMultiplyableKnob(cvValue, 0.0, initialKnobValue, 0.1);
    }

    public void setInputIsConnected(boolean inputIsConnected) {
        this.inputIsConnected = inputIsConnected;
    }

    public void setVolumeKnobValue(double value) {
        volume.setKnobValue(value);
    }

    public void setVolumeCVIsConnected(boolean cvIsConnected) {
        volume.setCvIsConnected(cvIsConnected);
    }

    public double getValue() {
        if (!inputIsConnected) {
            return 0.0;
        }

        return input.getAsDouble() * volume.getAsDouble();
    }

}
