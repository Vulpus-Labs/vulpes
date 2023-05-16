package com.vulpuslabs.vulpes.buffers;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BufferSizeTest {

    @Test
    public void testWrapInt() {
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

}
