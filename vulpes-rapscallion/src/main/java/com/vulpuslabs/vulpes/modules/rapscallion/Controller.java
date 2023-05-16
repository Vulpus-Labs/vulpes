package com.vulpuslabs.vulpes.modules.rapscallion;

import com.vulpuslabs.vulpes.values.events.TwoPositionSwitchState;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class Controller {

    private static final int[] RANGES = new int[] {
            10,
            100,
            500,
            1000,
            10000
    };

    private static final String[] RANGE_NAMES = new String[] {
            "10ms",
            "100ms",
            "500ms",
            "1s",
            "10s"
    };

    private static final double ONE_OVER_TWELVE = 1.0 / 12.0;

    private final BooleanSupplier triggerIn;
    private final DoubleConsumer modOut;
    private final DoubleSupplier loopIn;
    private final DoubleConsumer loopOut;

    private int rangeMs;
    private final DoubleSupplier length;

    private boolean isOctaveUp;

    private boolean isReverse;
    private final DoubleSupplier pitch;
    private final DoubleSupplier fade;

    private int sampleCount;
    private double modValue;
    private double modDelta;

    private double fadeValue;

    private double fadeDelta;

    private int fadeCount;

    private int fadeOutStart;

    private boolean pitchModIsConnected;

    public void setOctaveUp(TwoPositionSwitchState octaveUp) {
        isOctaveUp = octaveUp == TwoPositionSwitchState.ON;
    }

    public void setReverse(TwoPositionSwitchState reverse) {
        isReverse = reverse == TwoPositionSwitchState.ON;
    }

    public Controller(BooleanSupplier triggerIn,
                      DoubleSupplier length,
                      DoubleSupplier pitch,
                      DoubleSupplier fade,
                      DoubleConsumer modOut, DoubleSupplier loopIn, DoubleConsumer loopOut) {
        this.triggerIn = triggerIn;
        this.length = length;
        this.pitch = pitch;
        this.fade = fade;
        this.modOut = modOut;
        this.loopIn = loopIn;
        this.loopOut = loopOut;
    }

    public void processSample() {
        modOut.accept(modValue);

        if (sampleCount == 0) {
            if (triggerIn.getAsBoolean()) {
                triggerNew();
            } else {
                loopOut.accept(0.0);
                return;
            }
        }

        sampleCount -= 1;
        modValue += modDelta;

        double loopInSample = loopIn.getAsDouble();

        if (sampleCount == fadeOutStart) {
            fadeCount = sampleCount;
            fadeValue = 1.0;
            fadeDelta = -fadeDelta;
        }

        if (fadeCount == -1) {
            loopOut.accept(loopInSample);
            return;
        }

        double fadeValueSquared = fadeValue * fadeValue;
        double fadeValueCubed = fadeValueSquared * fadeValue;
        double fadeAmount = 3 * fadeValueSquared - 2 * fadeValueCubed;

        loopOut.accept(loopInSample * fadeAmount);
        fadeValue += fadeDelta;
        fadeCount -= 1;
    }

    private void triggerNew() {
        double lengthValue = length.getAsDouble();
        double pitchValue = pitch.getAsDouble();

        // Value between -1/12 and 13/12
        double pitchOffsetOctaves = (isOctaveUp ? 1.0 : 0.0)
                + pitchValue * ONE_OVER_TWELVE;

        // By how much of the range should the tape head have moved if playing full length?
        double fullDelta = isReverse ? -2 - pitchOffsetOctaves  : pitchOffsetOctaves;
        double absDelta = Math.abs(fullDelta);
        double playLengthPercent = lengthValue;

        // Is it too much? If so, shorten the length to ensure it will always fit.
        if (absDelta > 1.0) {
            playLengthPercent = lengthValue / absDelta;
            absDelta = 1.0;
        }

        double actualDelta = absDelta * lengthValue * 5.0;

        sampleCount = (int) (playLengthPercent * rangeMs * 48.0);
        fadeCount = (int) (sampleCount * 0.5 * fade.getAsDouble());
        fadeOutStart = fadeCount;
        fadeDelta = 1.0 / fadeCount;
        fadeValue = 0.0;

        double modEnd = 0.0;
        double modStart = 0.0;
        if (fullDelta > 0.0) {
            modStart = actualDelta;
        } else {
            modEnd = actualDelta;
        }

        modDelta = (modEnd - modStart) / (double) sampleCount;
        modValue = modStart;
    }

    public void setRange(double switchValue) {
        this.rangeMs = RANGES[(int) switchValue];
    }

    public String getRangeDescription(double switchValue) {
        return RANGE_NAMES[(int) switchValue];
    }

    public boolean isPlaying() {
        return sampleCount > 0;
    }

    public void setPitchModIsConnected(boolean pitchModIsConnected) {
        this.pitchModIsConnected = pitchModIsConnected;
    }

    public String getEffectiveLengthDescription() {
        double lengthValue = length.getAsDouble();
        double pitchValue = pitch.getAsDouble();

        // Value between -1/12 and 13/12
        double pitchOffsetOctaves = (isOctaveUp ? 1.0 : 0.0)
                + pitchValue * ONE_OVER_TWELVE;

        // By how much of the range should the tape head have moved if playing full length?
        double fullDelta = isReverse ? -2 - pitchOffsetOctaves  : pitchOffsetOctaves;

        double absDelta = Math.abs(fullDelta);
        double playLengthPercent = absDelta > 1.0
            ? lengthValue / absDelta
            : lengthValue;

        double playLengthMs = playLengthPercent * rangeMs;
        return String.format("%.3fs", playLengthMs * 0.001);
    }

}
