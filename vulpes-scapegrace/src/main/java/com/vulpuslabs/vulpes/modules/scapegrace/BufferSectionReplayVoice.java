package com.vulpuslabs.vulpes.modules.scapegrace;

import java.nio.Buffer;

public class BufferSectionReplayVoice implements ReplayVoice {

    private final int sampleLength;
    private final Buffer buffer;

    private final double offset;
    private int replayCount;

    public BufferSectionReplayVoice(int samples, BufferReadPointer pointer) {
        this.sampleLength = samples;
        this.pointer = pointer;
        this.replayCount = sampleLength;
    }

    @Override
    public int getSampleLength() {
        return sampleLength;
    }

    @Override
    public boolean isFinished() {
        return replayCount == 0;
    }

    @Override
    public void accept(SampleData sampleData) {
        pointer.readNext(sampleData);
        replayCount--;
    }

}
