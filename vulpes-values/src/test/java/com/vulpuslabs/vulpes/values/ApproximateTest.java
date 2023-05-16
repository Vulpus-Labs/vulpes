package com.vulpuslabs.vulpes.values;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApproximateTest {

    @Test
    void testFakeCosAccuracy() {

        var maxError = 0.0;
        for (int i=0; i <= 1000; i++) {
            var radians = i  * Math.PI / 500;
            var realCos = Math.cos(radians);
            var fakeCos = Approximate.cosRadians(radians);
            var errAmount = Math.abs(realCos - fakeCos);
            maxError = Math.max(maxError, errAmount);
        }

        assertTrue(maxError < 0.0011);
    }

    @Test
    void testFakeSinAccuracy() {

        var maxError = 0.0;
        for (int i=0; i <= 1000; i++) {
            var radians = i  * Math.PI / 500;
            var realSin = Math.sin(radians);
            var fakeSin = Approximate.sinRadians(radians);
            var errAmount = Math.abs(realSin - fakeSin);
            maxError = Math.max(maxError, errAmount);
        }

        assertTrue(maxError < 0.0011);
    }
}