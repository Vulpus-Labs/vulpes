package com.vulpuslabs.vulpes.values;

import java.util.function.DoubleSupplier;

public class Attenuvertor implements DoubleSupplier {

    private final DoubleSupplier sampleReader;
    private final DoubleSupplier controlReader;

    /**
     *
     * @param sampleReader The source of samples.
     * @param controlReader Either a unipolar or a bipolar unit value source. Should be smoothed.
     */
    public Attenuvertor(DoubleSupplier sampleReader, DoubleSupplier controlReader) {
        this.sampleReader = sampleReader;
        this.controlReader = controlReader;
    }

    @Override
    public double getAsDouble() {
        return sampleReader.getAsDouble() * controlReader.getAsDouble();
    }
}
