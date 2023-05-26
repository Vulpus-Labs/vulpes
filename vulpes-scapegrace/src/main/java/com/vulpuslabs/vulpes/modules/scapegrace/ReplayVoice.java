package com.vulpuslabs.vulpes.modules.scapegrace;

import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;

import java.util.function.Consumer;

public interface ReplayVoice extends Consumer<StereoSample> {

    int getSampleLength();
    boolean isFinished();

}
