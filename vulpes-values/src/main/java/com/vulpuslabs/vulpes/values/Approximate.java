package com.vulpuslabs.vulpes.values;

public final class Approximate {

    private Approximate() {
    }

    public static final double UNIT_FROM_RADIANS = 0.5 / Math.PI;

    public static double cosUnit(double x) {
        x -= 0.25 + Math.floor(x + 0.25);
        x *= 16 * (Math.abs(x) - 0.5);
        x += 0.225 * x * (Math.abs(x) - 1.0);
        return x;
    }

    public static double sinUnit(double x) {
        x -= 0.5 + Math.floor(x);
        x *= 16 * (Math.abs(x) - 0.5);
        x += 0.225 * x * (Math.abs(x) - 1.0);
        return x;
    }

    public static double tanh(double x) {
        double xpow2 = x * x;
        double xpow4 = xpow2 * xpow2;
        double xpow6 = xpow4 * xpow2;
        return x * (10395.0 + 1260.0 * xpow2 + 21.0 * xpow4) /
                (10395.0 + 4725.0 * xpow2 + 210.0 * xpow4 + 4.0 * xpow6);
    }

    public static double sinusoid(double x) {
        double squared = x * x;
        double cubed = squared * x;
        return 3 * squared - 2 * cubed;
    }

    public static double cosRadians(double x) {
        return cosUnit(x * UNIT_FROM_RADIANS);
    }

    public static double sinRadians(double x) {
        return sinUnit(x * UNIT_FROM_RADIANS);
    }

}
