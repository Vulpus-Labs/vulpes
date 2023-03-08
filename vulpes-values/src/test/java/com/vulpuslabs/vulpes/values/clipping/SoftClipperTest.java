package com.vulpuslabs.vulpes.values.clipping;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SoftClipperTest {

    @Test
    public void testSoftClipping() {
        SoftClipper softClipper = new SoftClipper(0.3);
        for (double d = -6.0; d <= 6.0; d+= 0.1) {
            var clipped = softClipper.apply(d);
            //System.out.println(d + ": " + clipped);
            assertTrue(clipped <= 4.7);
            assertTrue(clipped >= -4.7);
        }
    }
}
