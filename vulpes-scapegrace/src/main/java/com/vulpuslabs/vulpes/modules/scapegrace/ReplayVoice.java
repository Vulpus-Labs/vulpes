package com.vulpuslabs.vulpes.modules.scapegrace;

import com.vulpuslabs.vulpes.buffers.SampleData;

import java.util.function.Consumer;

public interface ReplayVoice extends Consumer<SampleData> {

    int getSampleLength();
    boolean isFinished();

}
