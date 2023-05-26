package com.vulpuslabs.vulpes.buffers.stereo;

@FunctionalInterface
public interface StereoBufferFractionalReader {

    void read(double offset, StereoSample target);

}
