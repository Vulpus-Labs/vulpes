package com.vulpuslabs.vulpes.buffers;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BufferSizeTest {

    @Test
    public void testFastModInt() {
        assertEquals(0, BufferSize.BUFFER_1k.wrap(0));
        assertEquals(512, BufferSize.BUFFER_1k.wrap(512));
        assertEquals(0, BufferSize.BUFFER_1k.wrap(1024));
        assertEquals(0, BufferSize.BUFFER_1k.wrap(2048));
        assertEquals(1, BufferSize.BUFFER_1k.wrap(1025));
        assertEquals(1, BufferSize.BUFFER_1k.wrap(2049));

        assertEquals(1023, BufferSize.BUFFER_1k.wrap(-1));
        assertEquals(1, BufferSize.BUFFER_1k.wrap(-1023));
        assertEquals(0, BufferSize.BUFFER_1k.wrap(-1024));
        assertEquals(1023, BufferSize.BUFFER_1k.wrap(-1025));
        assertEquals(1, BufferSize.BUFFER_1k.wrap(-2047));
        assertEquals(0, BufferSize.BUFFER_1k.wrap(-2048));
        assertEquals(1023, BufferSize.BUFFER_1k.wrap(-2049));
    }

    @Test
    public void testFastModDouble() {
        assertEquals(0.0, BufferSize.BUFFER_1k.wrap(0.0));
        assertEquals(512.0, BufferSize.BUFFER_1k.wrap(512.0));
        assertEquals(0.0, BufferSize.BUFFER_1k.wrap(1024.0));
        assertEquals(0.0, BufferSize.BUFFER_1k.wrap(2048.0));
        assertEquals(1.0, BufferSize.BUFFER_1k.wrap(1025.0));
        assertEquals(1.0, BufferSize.BUFFER_1k.wrap(2049.0));

        assertEquals(1023.0, BufferSize.BUFFER_1k.wrap(-1.0));
        assertEquals(1.0, BufferSize.BUFFER_1k.wrap(-1023.0));
        assertEquals(0.0, BufferSize.BUFFER_1k.wrap(-1024.0));
        assertEquals(1023.0, BufferSize.BUFFER_1k.wrap(-1025.0));
        assertEquals(1.0, BufferSize.BUFFER_1k.wrap(-2047.0));
        assertEquals(0.0, BufferSize.BUFFER_1k.wrap(-2048.0));
        assertEquals(1023.0, BufferSize.BUFFER_1k.wrap(-2049.0));

        assertEquals(1023.5, BufferSize.BUFFER_1k.wrap(-0.5));
        assertEquals(0.5, BufferSize.BUFFER_1k.wrap(-1023.5));
        assertEquals(0.5, BufferSize.BUFFER_1k.wrap(1024.5));
        assertEquals(0.5, BufferSize.BUFFER_1k.wrap(0.5));

        assertEquals(0.5, 1024.5 % 1024.0);
    }
}
