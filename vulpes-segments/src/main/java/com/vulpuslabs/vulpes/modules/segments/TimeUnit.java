package com.vulpuslabs.vulpes.modules.segments;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public enum TimeUnit {

    MILLISECONDS(48, "ms"),
    SECONDS(48000, "s");

    static NumberFormat TWO_DP = new DecimalFormat("0.00");

    private int samplesPerUnit;
    private String unit;

    TimeUnit(int samplesPerUnit, String unit) {
        this.samplesPerUnit = samplesPerUnit;
        this.unit = unit;
    }

    public String display(int samples) {
        double value = samples / (double) samplesPerUnit;
        return String.format("%.2f%s", value, unit);
    }

}
