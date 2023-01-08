package com.vulpuslabs.vulpes.buffers;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.api.SampleCount;
import org.junit.jupiter.api.Test;

import static com.vulpuslabs.vulpes.buffers.api.Stereo.LEFT;
import static com.vulpuslabs.vulpes.buffers.api.Stereo.RIGHT;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StereoBufferTest {

    @Test
    public void readTwoSamplesBehind() {
        var buffer = new Buffer(BufferSize.BUFFER_4b, SampleCount.STEREO);
        var readPointer = buffer.createReadPointer(-2);
        var writeData = buffer.createSampleData();
        var readData = readPointer.createSampleData();

        writeData.setSamples(-1, 1);
        buffer.writeNext(writeData);
        readPointer.readNext(readData);

        assertEquals(0, readData.getSample(LEFT));
        assertEquals(0, readData.getSample(RIGHT));

        writeData.setSamples(-2, 2);
        buffer.writeNext(writeData);
        readPointer.readNext(readData);

        assertEquals(0, readData.getSample(LEFT));
        assertEquals(0, readData.getSample(RIGHT));

        writeData.setSamples(-3, 3);
        buffer.writeNext(writeData);
        readPointer.readNext(readData);

        assertEquals(-1, readData.getSample(LEFT));
        assertEquals(1, readData.getSample(RIGHT));

        writeData.setSamples(-4, 4);
        buffer.writeNext(writeData);
        readPointer.readNext(readData);

        assertEquals(-2, readData.getSample(LEFT));
        assertEquals(2, readData.getSample(RIGHT));

        writeData.setSamples(-5, 5);
        buffer.writeNext(writeData);
        readPointer.readNext(readData);

        assertEquals(-3, readData.getSample(LEFT));
        assertEquals(3, readData.getSample(RIGHT));

        assertArrayEquals(new double[] { -5, 5, -2, 2, -3, 3, -4, 4},
                buffer.exportData());
    }
}
