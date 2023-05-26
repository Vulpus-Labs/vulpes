package com.vulpuslabs.vulpes.modules.scapegrace;

import com.vulpuslabs.vulpes.values.stereo.Pan;

import static com.vulpuslabs.vulpes.buffers.api.Stereo.LEFT;
import static com.vulpuslabs.vulpes.buffers.api.Stereo.RIGHT;

public class AutoPannedVoice implements ReplayVoice {

    private final ReplayVoice unpanned;
    private final Pan pan;
    private final double delta;
    private double balance;

    public AutoPannedVoice(ReplayVoice unpanned, double initial, double target) {
        this.unpanned = unpanned;
        this.pan = new Pan();
        this.delta = (target - initial) / unpanned.getSampleLength();
        this.balance = initial;
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
    public void accept(SampleData sampleData) {
        unpanned.accept(sampleData);

        pan.set(balance);
        balance += delta;

        sampleData.setSample(LEFT, sampleData.getSample(LEFT) * pan.getLeft());
        sampleData.setSample(RIGHT, sampleData.getSample(RIGHT) * pan.getLeft());
    }
}
