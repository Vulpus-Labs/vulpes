package com.vulpuslabs.vulpes.modules.chebysynth;

import com.vulpuslabs.vulpes.values.smoothed.SmoothedValue;
import com.vulpuslabs.vulpes.values.stereo.Pan;

public class VoiceControls {

    private final double[] pitchAdjustments = new double[8];
    private final Pan[] panAdjustments = new Pan[8];
    private final SmoothedValue pitchBendAmount = SmoothedValue.uiKnob();
    private final SmoothedValue harmonicAmount = SmoothedValue.uiKnob();
    private final SmoothedValue volume = SmoothedValue.uiKnob();
    private int voiceCount;
    private double voiceScaling;
    private double pitchSpread;
    private double stereoSpread;
    private double vibratoSpeed;
    private double vibratoDepth;
    private double moddedVibratoDepth;
    private double driftAmount;
    private double modWheelAmount = 1.0;
    private ModWheelSelect modWheelSelect = ModWheelSelect.FEEDBACK;

    private double feedbackAmount = 0.0;

    private double moddedFeedbackAmount = 0.0;
    private boolean stereoMode = false;

    public VoiceControls() {
        for (int i=0; i<8; i++) {
            panAdjustments[i] = new Pan();
        }
        harmonicAmount.accept(1.0);
        pitchBendAmount.accept(1.0);
    }

    public double getFeedbackAmount() {
        return moddedFeedbackAmount;
    }

    public void setFeedbackAmount(double feedbackAmount) {
        this.feedbackAmount = feedbackAmount * 0.2;
        this.moddedFeedbackAmount = modWheelSelect == ModWheelSelect.FEEDBACK
                ? this.feedbackAmount * modWheelAmount
                : this.feedbackAmount;
    }

    public void setPitchSpread(double pitchSpread) {
        this.pitchSpread = pitchSpread / 96;
        updateSpreads();
    }

    public void setStereoSpread(double stereoSpread) {
        this.stereoSpread = stereoSpread;
        updateSpreads();
    }

    public int getVoiceCount() {
        return voiceCount;
    }

    public void setVoiceCount(double knobValue) {
        int newVoiceCount = (int) knobValue;
        if (voiceCount == newVoiceCount) return;
        voiceCount = newVoiceCount;
        voiceScaling = 1.0 / voiceCount;
        updateSpreads();
    }

    public double getVoiceScaling() {
        return voiceScaling;
    }
    public double getPitchAdjustment(int voice) {
        return pitchAdjustments[voice];
    }

    public Pan getPan(int voice) {
        return panAdjustments[voice];
    }

    private void updateSpreads() {
        if (voiceCount == 1) {
            pitchAdjustments[0] = 1.0;
            panAdjustments[0].set(0.5);
            return;
        }

        double pitch = -pitchSpread;
        double pitchDelta = (pitchSpread * 2.0) / (voiceCount - 1);
        double pan = 0.5 - (0.5 * stereoSpread);
        double panDelta = stereoSpread / (voiceCount - 1);
        for (int i = 0; i < voiceCount; i++) {
            pitchAdjustments[i] = Math.pow(2.0, pitch);
            pitch += pitchDelta;
            panAdjustments[i].set(pan);
            pan += panDelta;
        }
    }

    public double getVibratoSpeed() {
        return vibratoSpeed;
    }

    public void setVibratoSpeed(double vibratoSpeed) {
        this.vibratoSpeed = 1 + 9 * vibratoSpeed;
    }

    public double getVibratoDepth() {
        return moddedVibratoDepth;
    }

    public void setVibratoDepth(double vibratoDepth) {
        this.vibratoDepth = vibratoDepth * 0.04;
        moddedVibratoDepth = modWheelSelect == ModWheelSelect.VIBRATO
                ? this.vibratoDepth * modWheelAmount
                : this.vibratoDepth;
    }

    public double getDriftAmount() {
        return driftAmount;
    }

    public void setDriftAmount(double driftAmount) {
        this.driftAmount = driftAmount;
    }

    public void setModWheelSelect(int selected) {
        ModWheelSelect newModWheelSelect = switch(selected) {
            case 0 -> ModWheelSelect.FEEDBACK;
            case 1 -> ModWheelSelect.HARMONICS;
            default -> ModWheelSelect.VIBRATO;
        };
        if (newModWheelSelect != modWheelSelect) {
            modWheelSelect = newModWheelSelect;
            moddedVibratoDepth = modWheelSelect == ModWheelSelect.VIBRATO ? vibratoDepth * modWheelAmount : vibratoDepth;
            moddedFeedbackAmount = modWheelSelect == ModWheelSelect.FEEDBACK ? feedbackAmount * modWheelAmount : feedbackAmount;
            harmonicAmount.accept(modWheelSelect == ModWheelSelect.HARMONICS ? modWheelAmount : 1.0);
        }
    }

    public void setPitchBend(double pitchBendAmount) {
        this.pitchBendAmount.accept(pitchBendAmount);
    }

    public void setModWheelAmount(double modWheelAmount) {
        this.modWheelAmount = modWheelAmount;
        switch (modWheelSelect) {
            case VIBRATO -> moddedVibratoDepth = vibratoDepth * modWheelAmount;
            case FEEDBACK -> moddedFeedbackAmount = feedbackAmount * modWheelAmount;
            case HARMONICS -> harmonicAmount.accept(modWheelAmount);
        }
    }

    public double getVolume() {
        return volume.getAsDouble();
    }

    public void setVolume(double knobValue) {
        volume.accept(knobValue * 0.2);
    }

    public boolean isStereoMode() {
        return stereoMode;
    }

    public void setStereoMode(boolean stereoMode) {
        this.stereoMode = stereoMode;
    }

    public double getHarmonicAmount() {
        return harmonicAmount.getAsDouble();
    }

    public double getPitchBendAmount() {
        return pitchBendAmount.getAsDouble();
    }

    private enum ModWheelSelect {
        FEEDBACK,
        HARMONICS,
        VIBRATO
    }
}
