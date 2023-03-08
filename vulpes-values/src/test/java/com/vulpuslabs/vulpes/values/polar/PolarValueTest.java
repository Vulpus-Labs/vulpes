package com.vulpuslabs.vulpes.values.polar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolarValueTest {

    @Test
    public void testPolarToCartesian() {
        var p = new PolarValue(0.25, 1.0);
        assertEquals(0.0, p.getCartesianX());
        assertEquals(1.0, p.getCartesianY());

        p.set(0.5, 1.0);
        assertEquals(-1.0, p.getCartesianX());
        assertEquals(0.0, p.getCartesianY());

        p.set(-0.25, 1.0);
        assertEquals(0.0, p.getCartesianX());
        assertEquals(-1.0, p.getCartesianY());

        p.set(-0.5, 1.0);
        assertEquals(-1.0, p.getCartesianX());
        assertEquals(0.0, p.getCartesianY());
    }
}
