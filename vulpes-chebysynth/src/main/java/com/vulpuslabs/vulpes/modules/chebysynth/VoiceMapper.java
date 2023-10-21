package com.vulpuslabs.vulpes.modules.chebysynth;

import java.util.*;

public final class VoiceMapper {

    private static final int NONE = -1;

    enum VoiceStatus {
        OFF,
        HELD,
        RELEASING,
        SUSTAINING
    }
    
    private final PolyVoiceEngine voiceEngine;
    private final int polyVoiceCount;
    private final Map<Integer, Integer> currentHolderByNote = new HashMap<>();

    private boolean sustaining;
    private final int[] voiceNotes;
    private final VoiceStatus[] voiceStatuses;

    public VoiceMapper(PolyVoiceEngine voiceEngine) {
        this.voiceEngine = voiceEngine;
        this.polyVoiceCount = voiceEngine.getVoiceCount();

        this.voiceStatuses = new VoiceStatus[polyVoiceCount];
        Arrays.fill(voiceStatuses, VoiceStatus.OFF);

        this.voiceNotes = new int[polyVoiceCount];
        Arrays.fill(voiceNotes, NONE);

        voiceEngine.onNoteComplete(this::onNoteComplete);
    }

    private void hold(int note, int voice, int velocity) {
        currentHolderByNote.put(note, voice);
        voiceNotes[voice] = note;
        voiceStatuses[voice] = VoiceStatus.HELD;
        voiceEngine.noteOn(voice, note, velocity);
    }

    private void sustain(int voice) {
        int note = voiceNotes[voice];
        currentHolderByNote.remove(note);
        voiceStatuses[voice] = VoiceStatus.SUSTAINING;
    }

    private void release(int voice) {
        int note = voiceNotes[voice];
        currentHolderByNote.remove(note);
        voiceStatuses[voice] = VoiceStatus.RELEASING;
        voiceEngine.noteOff(voice);
    }

    public void onNoteComplete(int voiceIndex) {
        voiceStatuses[voiceIndex] = VoiceStatus.OFF;
        int note = voiceNotes[voiceIndex];
        if (note == NONE) return;
        voiceNotes[voiceIndex] = NONE;
        int currentNoteHolder = currentHolderByNote.getOrDefault(note, NONE);
        if (currentNoteHolder == voiceIndex) {
            currentHolderByNote.remove(note);
        }
    }

    public void setSustaining(boolean sustaining) {
        this.sustaining = sustaining;
        if (!sustaining) {
            for (int i = 0; i < polyVoiceCount; i++) {
                if (voiceStatuses[i] == VoiceStatus.SUSTAINING) {
                    release(i);
                }
            }
        }
    }

    public void resetAll() {
        for (int i = 0; i < polyVoiceCount; i++) {
            switch (voiceStatuses[i]) {
                case HELD, SUSTAINING -> release(i);
            }
        }
        sustaining = false;
    }

    public void noteOn(int note, int velocity) {
        // Release all sustained (or held) voices that are playing this note
        for (int i = 0; i < polyVoiceCount; i++) {
            if (voiceNotes[i] != note) continue;
            switch (voiceStatuses[i]) {
                case HELD, SUSTAINING -> release(i);
            }
        }

        int voiceIndex = voiceEngine.tryAllocateNewVoice();
        if (voiceIndex == NONE) {
            return;
        }

        hold(note, voiceIndex, velocity);
    }

    public void noteOff(int note) {
        int voiceIndex = currentHolderByNote.getOrDefault(note, NONE);

        if (voiceIndex == NONE) {
            return;
        }

        if (sustaining) {
            sustain(voiceIndex);
        } else {
            release(voiceIndex);
        }

    }
}
