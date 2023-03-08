package com.vulpuslabs.vulpes.values;

public final class FakeTrig {

    private FakeTrig() {
    }

    public static final double UNIT_FROM_RADIANS = 0.5 / Math.PI;
    public static final double RADIANS_FROM_UNIT = 2.0 * Math.PI;

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

    public static double fakeTanh(double x) {
        double xpow2 = x * x;
        double xpow4 = xpow2 * xpow2;
        double xpow6 = xpow4 * xpow2;
        return x * (10395.0 + 1260.0 * xpow2 + 21.0 * xpow4) /
                (10395.0 + 4725.0 * xpow2 + 210.0 * xpow4 + 4.0 * xpow6);
    }

    public static double fakeCos(double x) {
        return fakeCosUnit(x * UNIT_FROM_RADIANS);
    }

    public static double fakeSin(double x) {
        return fakeSinUnit(x * UNIT_FROM_RADIANS);
    }

}
