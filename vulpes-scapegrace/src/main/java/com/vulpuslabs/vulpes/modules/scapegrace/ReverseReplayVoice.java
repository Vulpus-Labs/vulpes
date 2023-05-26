package com.vulpuslabs.vulpes.modules.scapegrace;

public class ReverseReplayVoice implements ReplayVoice {

    private final int sampleLength;
    private final BufferReadPointer pointer;
    private int replayCount;

    public ReverseReplayVoice(int sampleLength, BufferReadPointer pointer) {
        this.sampleLength = sampleLength;
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
        pointer.readNextReverse(sampleData);
        replayCount -= 1;
    }
}
