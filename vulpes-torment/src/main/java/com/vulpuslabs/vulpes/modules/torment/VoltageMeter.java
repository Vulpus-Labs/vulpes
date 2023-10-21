package com.vulpuslabs.vulpes.modules.torment;

public class VoltageMeter {

    private double peak;

    public void accept(double unitSample) {
        peak = Math.max(Math.abs(unitSample), peak);
    }

    public double getValue() {
        var result = peak;
        peak = 0.0;
        return result;
    }
}
