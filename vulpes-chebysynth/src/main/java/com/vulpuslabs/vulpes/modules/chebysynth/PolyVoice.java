package com.vulpuslabs.vulpes.modules.chebysynth;

import com.vulpuslabs.vulpes.values.api.Oscillator;
import com.vulpuslabs.vulpes.values.lfo.FakeSinLfo;
import com.vulpuslabs.vulpes.values.stereo.Pan;

public class PolyVoice {

    private static final double ONE_OVER_TWELVE = 1.0 / 12.0;

    private final int index;
    private final VoiceControls voiceControls;

    private final EnvelopeGenerator volumeAdsr;
    private final EnvelopeGenerator harmonicAdsr;

    private final ChebyshevOscillator[] oscillators;
    private final Oscillator pitchLfo = new FakeSinLfo();
    private final HarmonicWeights harmonicWeights;

    private double baseFrequency;
    private double amplitude;

    private boolean isActive;

    public PolyVoice(int index, VoiceControls voiceControls, EnvelopeGenerator volumeAdsr, EnvelopeGenerator harmonicAdsr, HarmonicWeights harmonicWeights) {
        this.index = index;
        this.voiceControls = voiceControls;
        this.volumeAdsr = volumeAdsr;
        this.harmonicAdsr = harmonicAdsr;
        this.oscillators = new ChebyshevOscillator[8];
        for (int i=0; i<8; i++) {
            oscillators[i] = new ChebyshevOscillator(48000);
        }
        this.harmonicWeights = harmonicWeights;
        this.pitchLfo.setFrequencyHz(5);
    }

    public int getIndex() {
        return index;
    }

    public void noteOn(int midiNote, int velocity) {
        baseFrequency = 440 * Math.pow(2, (midiNote - 69) * ONE_OVER_TWELVE);
        activateVoice(velocity);

        for (int i = 0; i < voiceControls.getVoiceCount(); i++) {
            oscillators[i].setPosition(0);
        }

        pitchLfo.setPosition(0);
    }

    private void activateVoice(int velocity) {
        volumeAdsr.start();
        harmonicAdsr.start();
        amplitude = velocity / 128.0;
        isActive = true;
    }

    public void retrigger(int velocity) {
       activateVoice(velocity);
    }

    public void noteOff() {
        volumeAdsr.stop();
        harmonicAdsr.stop();
    }

    public void tick(double harmonicAmount, double pitchBendAmount, double[] stereoSample) {
        double coefficientMod = harmonicAdsr.tick() * harmonicAmount;
        double[] coefficients = harmonicWeights.getCoefficients(coefficientMod);
        double env = volumeAdsr.tick();

        double frequency = baseFrequency * pitchBendAmount;

        double vibratoDepth = voiceControls.getVibratoDepth();
        if (vibratoDepth > 0.0) {
            pitchLfo.setFrequencyHz(voiceControls.getVibratoSpeed());
            double pitchModulation = vibratoDepth * pitchLfo.getAsDouble();
            pitchModulation += 1;
            frequency *= pitchModulation;
        }

        double left = 0;
        double right = 0;
        for (int i = 0; i < voiceControls.getVoiceCount(); i++) {
            ChebyshevOscillator oscillator = oscillators[i];

            double sample = oscillator.getSample(
                    voiceControls.getFeedbackAmount(),
                    frequency * voiceControls.getPitchAdjustment(i),
                    voiceControls.getDriftAmount(),
                    coefficients);

            if (voiceControls.isStereoMode()) {
                Pan pan = voiceControls.getPan(i);
                left += pan.getLeft() * sample;
                right += pan.getRight() * sample;
            } else {
                left += sample;
            }
        }
        
        double scale = env * amplitude * voiceControls.getVoiceScaling();
        
        stereoSample[0] += left * scale;
        if (voiceControls.isStereoMode()) {
            stereoSample[1] += right * scale;
        }

        isActive = !volumeAdsr.isAtRest();
    }

    public boolean isActive() {
        return isActive;
    }
}
