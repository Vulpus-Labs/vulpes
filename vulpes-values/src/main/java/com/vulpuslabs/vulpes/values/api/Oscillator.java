package com.vulpuslabs.vulpes.values.api;

import java.util.function.DoubleSupplier;

public interface Oscillator extends DoubleSupplier {

    double getFrequencyHz();
    void setFrequencyHz(double frequencyHz);
    double getAtPosition(double pos);

    double getPosition();
    void setPosition(double position);
}
