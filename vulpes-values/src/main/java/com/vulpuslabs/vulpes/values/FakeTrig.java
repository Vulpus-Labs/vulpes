package com.vulpuslabs.vulpes.values;

public final class FakeTrig {

    private FakeTrig() {
    }

    public static final double UNIT_FROM_RADIANS = 0.5 / Math.PI;

    public static double fakeCosUnit(double x) {
        x -= 0.25 + Math.floor(x + 0.25);
        x *= 16 * (Math.abs(x) - 0.5);
        x += 0.225 * x * (Math.abs(x) - 1.0);
        return x;
    }

    public static double fakeSinUnit(double x) {
        x -= 0.5 + Math.floor(x);
        x *= 16 * (Math.abs(x) - 0.5);
        x += 0.225 * x * (Math.abs(x) - 1.0);
        return x;
    }

    public static double fakeCos(double x) {
        return fakeCosUnit(x * UNIT_FROM_RADIANS);
    }

    public static double fakeSin(double x) {
        return fakeSinUnit(x * UNIT_FROM_RADIANS);
    }

}
