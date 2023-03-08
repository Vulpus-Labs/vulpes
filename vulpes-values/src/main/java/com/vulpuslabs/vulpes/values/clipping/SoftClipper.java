package com.vulpuslabs.vulpes.values.clipping;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public class SoftClipper implements DoubleTransformer {

    private final double clipTop;
    private final double clipBottom;
    private final double cubeScale;

    public SoftClipper(double amount) {
        clipTop = 5.0 - amount;
        clipBottom = -5.0 + amount;
        cubeScale = amount / (5.0 * 5.0 * 5.0);
    }

    @Override
    public double apply(double value) {
        if (value > 5.0) {
            return clipTop;
        } else if (value < -5.0) {
            return clipBottom;
        } else {
            return value - (value * value * value * cubeScale);
        }
    }
}
