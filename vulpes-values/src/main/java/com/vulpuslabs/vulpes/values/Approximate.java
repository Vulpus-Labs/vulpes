package com.vulpuslabs.vulpes.values;

import static java.lang.Math.PI;

public final class Approximate {

    private Approximate() {
    }

    public static final double HALF_PI = PI / 2.0;
    public static final double UNIT_FROM_RADIANS = 0.5 / PI;

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

    // https://math.stackexchange.com/a/1105038
    public static double atan2(double y, double x) {
        if (x == 0.0 && y == 0.0) return 0.0;

        double absX = Math.abs(x);
        double absY = Math.abs(y);
        double a = Math.min(absX, absY) / Math.max(absX, absY);
        double s = a * a;
        double r = ((-0.0464964749 * s + 0.15931422) * s - 0.327622764) * s * a + a;
        if (absY > absX) r = HALF_PI - r;
        if (x < 0) r = PI - r;
        if (y < 0) r = -r;
        return r;
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
