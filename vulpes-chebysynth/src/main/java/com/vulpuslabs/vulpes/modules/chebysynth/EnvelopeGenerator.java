package com.vulpuslabs.vulpes.modules.chebysynth;

public class EnvelopeGenerator {

    public EnvelopeGenerator(EnvelopeGeneratorControls controls) {
        this.controls = controls;
    }

    /**
     * Attack + Decay ramps up to 1 then down to sustain level.
     * Attack-only ramps up to sustain level (or no-play if sustain disabled)
     * Decay-only ramps down from 1 to sustain level.
     * No attack/decay sustains then releases.
     * Attack or decay may be interrupted by gate-off, going straight into release .
     *
     * Note-off marks sustain as bypassed; A+D complete normally then go to release.
     */
    private enum ADSRState {
        NOT_PLAYING,
        ATTACK,
        DECAY,
        SUSTAIN,
        RELEASE
    }

    private static final double SMOOTH_AMOUNT = 0.05;
    private static final double ONE_MINUS_SMOOTH_AMOUNT = 1.0 - SMOOTH_AMOUNT;

    private final EnvelopeGeneratorControls controls;


    private EnvelopeGeneratorSegment attackSegment;
    private EnvelopeGeneratorSegment decaySegment;
    private EnvelopeGeneratorSegment sustainSegment;
    private EnvelopeGeneratorSegment releaseSegment;

    private ADSRState state = ADSRState.NOT_PLAYING;

    private boolean noteOffReceived;
    private int segmentCount;
    private double envelopeValue;
    private double targetEnvelopeValue;
    private double targetDelta;

    public void start() {
        this.attackSegment = controls.getAttackSegment();
        this.decaySegment = controls.getDecaySegment();
        this.sustainSegment = controls.getSustainSegment();
        this.releaseSegment = controls.getReleaseSegment();

        noteOffReceived = false;

        if (attackSegment.enabled()) {
            state = ADSRState.ATTACK;
            initialiseSegment(attackSegment);
        } else if (decaySegment.enabled()) {
            state = ADSRState.DECAY;
            initialiseSegment(decaySegment);
        } else {
            sustainOrRelease();
        }
    }

    private void initialiseSegment(EnvelopeGeneratorSegment segment) {
        segmentCount = segment.sampleLength();
        targetEnvelopeValue = segment.initial();
        targetDelta = segment.delta();
    }

    public void stop() {
        noteOffReceived = true;
    }

    public boolean isAtRest() {
        return state == ADSRState.NOT_PLAYING && envelopeValue < 0.0001;
    }

    public double tick() {
        envelopeValue = (SMOOTH_AMOUNT * targetEnvelopeValue) + (ONE_MINUS_SMOOTH_AMOUNT * envelopeValue);

        if (state == ADSRState.SUSTAIN) {
            if (noteOffReceived) {
                releaseOrStop();
            }
            return envelopeValue;
        }

        targetEnvelopeValue += targetDelta;
        segmentCount -= 1;
        if (segmentCount == 0) {
            switch (state) {
                case ATTACK -> {
                    if (decaySegment.enabled()) {
                        state = ADSRState.DECAY;
                        initialiseSegment(decaySegment);
                    } else sustainOrRelease();
                }
                case DECAY -> {
                    sustainOrRelease();
                }
                case RELEASE -> {
                    stopPlaying();
                }
                default -> {}
            }
        }

        return envelopeValue;
    }

    private void sustainOrRelease() {
        if (sustainSegment.enabled() && !noteOffReceived) {
            state = ADSRState.SUSTAIN;
            initialiseSegment(sustainSegment);
        } else {
            releaseOrStop();
        }
    }

    private void releaseOrStop() {
        if (releaseSegment.enabled()) {
            state = ADSRState.RELEASE;
            initialiseSegment(releaseSegment);
        } else {
            stopPlaying();
        }
    }

    private void stopPlaying() {
        state = ADSRState.NOT_PLAYING;
        targetEnvelopeValue = 0;
        targetDelta = 0;
        segmentCount = 0;
    }
}
