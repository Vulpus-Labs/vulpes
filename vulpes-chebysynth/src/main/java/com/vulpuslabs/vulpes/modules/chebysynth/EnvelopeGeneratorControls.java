package com.vulpuslabs.vulpes.modules.chebysynth;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Formatter;

public class EnvelopeGeneratorControls {

    private static final int MAX_ATTACK_LENGTH_SAMPLES = 48000 * 4;
    private static final int MAX_DECAY_LENGTH_SAMPLES = 48000 * 4;
    private static final int MAX_RELEASE_LENGTH_SAMPLES = 48000 * 4;

    private static final EnvelopeGeneratorSegment DISABLED_SEGMENT = new EnvelopeGeneratorSegment(false, 0, 0, 0);

    private int attackLengthSamples;
    private int decayLengthSamples;
    private double sustainLevel;
    private int releaseLengthSamples;

    private EnvelopeGeneratorSegment attackSegment = DISABLED_SEGMENT;
    private EnvelopeGeneratorSegment decaySegment = DISABLED_SEGMENT;
    private EnvelopeGeneratorSegment sustainSegment = DISABLED_SEGMENT;
    private EnvelopeGeneratorSegment releaseSegment = DISABLED_SEGMENT;

    public void setAttackLength(double attackLengthFraction) {
        this.attackLengthSamples = (int) (MAX_ATTACK_LENGTH_SAMPLES * attackLengthFraction);
        calculateSegments();
    }

    private void calculateSegments() {
        sustainSegment = sustainLevel == 0
                ? DISABLED_SEGMENT
                : new EnvelopeGeneratorSegment(true, sustainLevel, 0, 0);

        releaseSegment = !sustainSegment.enabled() || releaseLengthSamples == 0
                ? DISABLED_SEGMENT
                : new EnvelopeGeneratorSegment(
                        true, sustainLevel, releaseLengthSamples, -sustainLevel / releaseLengthSamples);

        decaySegment = decayLengthSamples == 0
            ? DISABLED_SEGMENT
            : new EnvelopeGeneratorSegment(
                true, 1.0, decayLengthSamples, (sustainLevel - 1.0) / decayLengthSamples);

        if (attackLengthSamples == 0 || (!decaySegment.enabled() && !sustainSegment.enabled())) {
            attackSegment = DISABLED_SEGMENT;
        } else {
            double attackTarget = decaySegment.enabled() ? 1.0 : sustainLevel;
            attackSegment = new EnvelopeGeneratorSegment(
                    true, 0, attackLengthSamples, attackTarget / attackLengthSamples);
        }
    }

    public void setDecayLength(double decayLengthFraction) {
        this.decayLengthSamples = (int) (MAX_DECAY_LENGTH_SAMPLES * decayLengthFraction);
        calculateSegments();
    }

    public void setSustainLevel(double sustainLevel) {
        this.sustainLevel = sustainLevel;
        calculateSegments();
    }

    public void setReleaseLength(double releaseLengthFraction) {
        this.releaseLengthSamples = (int) (MAX_RELEASE_LENGTH_SAMPLES * releaseLengthFraction);
        calculateSegments();
    }

    public EnvelopeGeneratorSegment getAttackSegment() {
        return attackSegment;
    }

    public EnvelopeGeneratorSegment getDecaySegment() {
        return decaySegment;
    }

    public EnvelopeGeneratorSegment getSustainSegment() {
        return sustainSegment;
    }

    public EnvelopeGeneratorSegment getReleaseSegment() {
        return releaseSegment;
    }

    public String getTooltipA() {
        return formatLength(attackLengthSamples);
    }

    public String getTooltipD() {
        return formatLength(decayLengthSamples);
    }

    public String getTooltipR() {
        return formatLength(releaseLengthSamples);
    }
    
    private String formatLength(int decayLengthSamples) {
        return new Formatter().format("%.2f", (double) decayLengthSamples / 48000) + "s";
    }
}
