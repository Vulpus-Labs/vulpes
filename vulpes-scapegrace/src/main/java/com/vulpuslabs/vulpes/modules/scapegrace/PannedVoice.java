package com.vulpuslabs.vulpes.modules.scapegrace;

import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;
import com.vulpuslabs.vulpes.values.stereo.Pan;

import static com.vulpuslabs.vulpes.buffers.api.Stereo.LEFT;
import static com.vulpuslabs.vulpes.buffers.api.Stereo.RIGHT;

public class PannedVoice implements ReplayVoice {

    private final ReplayVoice unpanned;
    private final Pan pan;

    public PannedVoice(ReplayVoice unpanned, Pan pan) {
        this.unpanned = unpanned;
        this.pan = pan;
    }

    @Override
    public int getSampleLength() {
        return unpanned.getSampleLength();
    }

    @Override
    public boolean isFinished() {
        return unpanned.isFinished();
    }

    @Override
    public void accept(StereoSample sampleData) {
        unpanned.accept(sampleData);
        sampleData.set(sampleData.getLeft() * pan.getLeft(),
                sampleData.getRight() * pan.getRight());
    }
}
