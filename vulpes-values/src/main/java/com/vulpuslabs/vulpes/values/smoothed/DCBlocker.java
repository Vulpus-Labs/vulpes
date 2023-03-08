package com.vulpuslabs.vulpes.values.smoothed;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public class DCBlocker implements DoubleTransformer {

    private double xm1;
    private double ym1;

    @Override
    public double apply(double value) {
        var y = value - xm1 + 0.995 * ym1;
        xm1 = value;
        ym1 = y;
        return y;
    }
}
