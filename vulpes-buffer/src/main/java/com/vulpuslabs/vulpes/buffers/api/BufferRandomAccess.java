package com.vulpuslabs.vulpes.buffers.api;

import com.vulpuslabs.vulpes.buffers.SampleData;

public interface BufferRandomAccess {

    double getSampleAtOffset(double offset);

    void getSampleAtOffset(double offset, SampleData[] readBuffer, SampleData target);

}
