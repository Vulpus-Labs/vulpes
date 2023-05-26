package com.vulpuslabs.vulpes.modules.scapegrace;

import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;

import static com.vulpuslabs.vulpes.buffers.api.Stereo.LEFT;
import static com.vulpuslabs.vulpes.buffers.api.Stereo.RIGHT;

public class FadingVoice implements ReplayVoice {

    private final ReplayVoice voice;
    private final int sampleLength;
    private final int fadeInEnd;
    private final int fadeOutStart;
    private final double fadeDelta;
    private double fadeAmount;
    private int fadePos = 0;

    public FadingVoice(ReplayVoice voice, double fadeInPercent, double fadeOutPercent) {
        this.voice = voice;
        this.sampleLength = voice.getSampleLength();
        this.fadeInEnd = (int) (sampleLength * fadeInPercent / 100.0);
        this.fadeOutStart = sampleLength - (int) (sampleLength * fadeOutPercent / 100.0);
        fadeAmount = 0.0;
        fadeDelta = 1.0 / fadeInEnd;
    }

    @Override
    public int getSampleLength() {
        return sampleLength;
    }

    @Override
    public void accept(StereoSample sampleData) {
        voice.accept(sampleData);

        if (fadePos < fadeInEnd) {
            double amount = (fadeAmount * 2.0) - (fadeAmount * fadeAmount);
            sampleData.multiply(amount);
            fadeAmount += fadeDelta;
        }

        if (fadePos == fadeOutStart) {
            fadeAmount = 1.0;
        }

        if (fadePos >= fadeOutStart) {
            double amount = (fadeAmount * 2.0) - (fadeAmount * fadeAmount);
            sampleData.multiply(amount);
            fadeAmount -= fadeDelta;
        }

        fadePos++;
    }

    @Override
    public boolean isFinished() {
        return voice.isFinished();
    }

}
