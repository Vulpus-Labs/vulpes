package com.vulpuslabs.vulpes.buffers;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import org.junit.jupiter.api.Test;

public class BufferPointerTest {

    private final BufferBoundedIndex unit = new BufferBoundedIndex(BufferSize.BUFFER_1k, 0);

    @Test
    public void wrapsAnIntPointerIntoRange() {

    }
}
