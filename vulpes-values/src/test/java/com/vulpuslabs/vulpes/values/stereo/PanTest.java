package com.vulpuslabs.vulpes.values.stereo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PanTest {

    @Test
    public void testPan() {
        Pan pan = new Pan();
        pan.set(0);
        assertEquals(1.0, pan.getLeft());
        assertEquals(0.0, pan.getRight());

        pan.set(0.5);
        assertEquals(1.0, pan.getLeft());
        assertEquals(1.0, pan.getRight());

        pan.set(1.0);
        assertEquals(0.0, pan.getLeft());
        assertEquals(1.0, pan.getRight());
    }
}
