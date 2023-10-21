package com.vulpuslabs.vulpes.values.complex;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComplexTest {

    @Test
    public void polarToComplexRoundtrip() {
        Random random = new Random();
        Complex c = new Complex();
        Polar p = new Polar();

        double totalRealError = 0.0;

        for (int i=0; i<1000; i++) {
            double expectedReal = random.nextDouble();
            double expectedImaginary = random.nextDouble();
            c.set(expectedReal, expectedImaginary);

            c.toPolar(p);
            p.toComplex(c);

            totalRealError += Math.abs(c.getReal() - expectedReal);
        }
        double avgRealError = totalRealError / 1000;
        System.out.println(avgRealError);
        assertTrue(avgRealError < 1e-2);
    }
}
