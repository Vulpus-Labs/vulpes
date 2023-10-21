package com.vulpuslabs.vulpes.values.complex;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FFTTest {


    @Test
    public void roundtripFFT() {
        FFT fft = new FFT();

        Complex[] x = new Complex[256];
        Complex[] expected = new Complex[256];

        Random random = new Random();
        // original data
        for (int i = 0; i < 256; i++) {
            x[i] = new Complex(random.nextDouble(), 0);
            expected[i] = new Complex(x[i].getReal(), x[i].getImaginary());
        }

        fft.transform(x);
        fft.reverseTransform(x);

        for (int i = 0; i < 256; i++) {
            assertTrue(Math.abs(expected[i].getReal() - x[i].getReal()) < 0.000001);
            assertTrue(Math.abs(expected[i].getImaginary() - x[i].getImaginary()) < 0.000001);
        }
    }
}
