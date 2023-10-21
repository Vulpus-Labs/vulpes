package com.vulpuslabs.vulpes.modules.chebysynth;

import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.smoothed.DCBlocker;
import com.vulpuslabs.vulpes.values.smoothed.SmoothedValue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

public class PolyVoiceEngine {

    private final PolyVoice[] voices;
    private final int[] freeVoices;

    private final HarmonicWeights harmonicWeights;

    private final LinkedList<PolyVoice> activeVoices = new LinkedList<>();

    private final double[] stereo = new double[2];

    private int nextFreeVoice;

    private IntConsumer onNoteComplete;

    private final VoiceControls voiceControls;

    private final DCBlocker leftBlocker = new DCBlocker();
    private final DCBlocker rightBlocker = new DCBlocker();

    public PolyVoiceEngine(int voiceCount,
                           VoiceControls voiceControls,
                           EnvelopeGeneratorControls volumeAdsr,
                           EnvelopeGeneratorControls harmonicAdsr,
                           HarmonicWeights harmonicWeights) {
        this.voiceControls = voiceControls;
        voices = new PolyVoice[voiceCount];
        freeVoices = new int[voiceCount];
        this.harmonicWeights = harmonicWeights;

        for (int i=0; i<voiceCount; i++) {
            freeVoices[i] = i;
            voices[i] = new PolyVoice(
                    i,
                    voiceControls,
                    new EnvelopeGenerator(volumeAdsr),
                    new EnvelopeGenerator(harmonicAdsr),
                    harmonicWeights);
        }
        nextFreeVoice = voiceCount - 1;
    }
    public void noteOn(int voiceIndex, int noteValue, int velocity) {
        voices[voiceIndex].noteOn(noteValue, velocity);
    }

    public void retrigger(int voiceIndex, int velocity) {
        voices[voiceIndex].retrigger(velocity);
    }

    public void noteOff(int voiceIndex) {
        voices[voiceIndex].noteOff();
    }

    public int tryAllocateNewVoice() {
        if (nextFreeVoice == -1) return -1;
        int voice = freeVoices[nextFreeVoice--];
        activeVoices.add(voices[voice]);
        return voice;
    }


    public void tick(DoubleConsumer left, DoubleConsumer right) {
        // Get smoothed harmonic weight values
        harmonicWeights.tick();

        // Get smoothed voice control values
        double harmonicAmount = voiceControls.getHarmonicAmount();
        double pitchBendAmount = voiceControls.getPitchBendAmount();

        // Initialise stereo samples
        stereo[0] = 0.0;
        stereo[1] = 0.0;

        Iterator<PolyVoice> iter = activeVoices.iterator();
        while (iter.hasNext()) {
            PolyVoice voice = iter.next();
            voice.tick(harmonicAmount, pitchBendAmount, stereo);
            if (!voice.isActive()) {
                iter.remove();
                freeVoices[++nextFreeVoice] = voice.getIndex();
                onNoteComplete.accept(voice.getIndex());
            }
        }

        double volumeAmount = voiceControls.getVolume();
        left.accept(Approximate.tanh(leftBlocker.apply(stereo[0] * volumeAmount)) * 5.0);
        if (voiceControls.isStereoMode()) {
            right.accept(Approximate.tanh(rightBlocker.apply(stereo[1] * volumeAmount)) * 5.0);
        }
    }

    public void onNoteComplete(IntConsumer onNoteComplete) {
        this.onNoteComplete = onNoteComplete;
    }

    public int getVoiceCount() {
        return voices.length;
    }
}
