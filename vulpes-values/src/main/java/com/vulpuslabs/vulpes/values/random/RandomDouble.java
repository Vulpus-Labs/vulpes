package com.vulpuslabs.vulpes.values.random;

import java.util.Random;
import java.util.function.DoubleSupplier;

public class RandomDouble implements DoubleSupplier {

    // Marsaglia's UNIversal random generator extended to double precision
    // G. Marsaglia, W.W. Tsang / Statistics & Probability Letters 66 (2004) 183 – 187
    private final double[] U = new double[98];
    static final double r = 9007199254740881.0 / 9007199254740992.;
    static final double d = 362436069876.0 / 9007199254740992.0;
    private double c = 0.;
    private int i = 97, j = 33;

    public RandomDouble(long seed) {
        Random r = new Random(seed);
        while (true) {
            try {
                fillU(r.nextInt(), r.nextInt());
                break;
            } catch (Exception e) {
                // loop again
            }
        }
    }

    @Override
    public double getAsDouble() {
        double x;

        x = U[i] - U[j];
        x = U[i] = (x < 0.0) ? x + 1.0 : x;

        if (--i == 0) i = 97;
        if (--j == 0) j = 97;

        c = c - d;
        if (c < 0.0) c = c + r;

        x = x - c;
        return (x < 0.) ? x + 1 : x;
    }

    //A two-seed function for filling the static array U[98] one bit at a time
    private void fillU(int seed1, int seed2) {
        double s, t;
        int x, y, i, j;
        x = seed1;
        y = seed2;

        for (i = 1; i < 98; i++) {
            s = 0.0;
            t = 0.5;

            for (j = 1; j < 54; j++) {
                x = (6969 * x) % 65543;
                // typo in the paper:
                //y=(8888*x) % 65579;
                //used forthe demo in the last page of the paper.
                y = (8888 * y) % 65579;
                if (((x ^ y) & 32) > 0)
                    s = s + t;
                t = .5 * t;
            }
            if (x == 0)
                throw new IllegalArgumentException("x");
            if (y == 0)
                throw new IllegalArgumentException("y");
            U[i] = s;
        }
    }

}
