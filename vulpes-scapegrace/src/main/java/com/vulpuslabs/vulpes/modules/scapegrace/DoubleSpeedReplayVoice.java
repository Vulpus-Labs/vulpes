package com.vulpuslabs.vulpes.modules.scapegrace;

public class DoubleSpeedReplayVoice implements ReplayVoice {

    private final int sampleLength;
    private final ReplayVoice voice;
    private int replayCount;

    public DoubleSpeedReplayVoice(ReplayVoice voice) {
        this.voice = voice;
        this.sampleLength = voice.getSampleLength() >> 1;
        this.replayCount = this.sampleLength;
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
        voice.accept(sampleData);
        voice.accept(sampleData);
        replayCount--;
    }
}
