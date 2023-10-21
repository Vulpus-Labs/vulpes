package com.vulpuslabs.vulpes.modules.rapscallion;

import com.vulpuslabs.vulpes.values.events.TwoPositionSwitchState;
import com.vulpuslabs.vulpes.values.stereo.Pan;

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
    private boolean triggerSet;
    private final DoubleConsumer modOut;
    private final DoubleSupplier loopInL;
    private final DoubleSupplier loopInR;
    private final DoubleConsumer loopOutL;
    private final DoubleConsumer loopOutR;
    private final DoubleConsumer fadeOutTrigger;

    private int rangeMs;
    private final DoubleSupplier length;

    private boolean isAutoRetrigger;
    private boolean isOctaveUp;

    private boolean isReverse;
    private final DoubleSupplier pitch;
    private final DoubleSupplier fade;
    private final DoubleSupplier balance;

    private int sampleCount;
    private double modValue;
    private double modDelta;

    private double fadeValue;

    private double fadeDelta;

    private int fadeCount;

    private int fadeOutStart;

    private final Pan pan = new Pan();

    public void setAutoRetrigger(TwoPositionSwitchState autoRetrigger) {
        isAutoRetrigger = autoRetrigger == TwoPositionSwitchState.ON;
    }
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
                      DoubleSupplier balance,
                      DoubleConsumer modOut,
                      DoubleSupplier loopInL,
                      DoubleSupplier loopInR,
                      DoubleConsumer loopOutL,
                      DoubleConsumer loopOutR,
                      DoubleConsumer fadeOutTrigger) {
        this.triggerIn = triggerIn;
        this.length = length;
        this.pitch = pitch;
        this.fade = fade;
        this.balance = balance;
        this.modOut = modOut;
        this.loopInL = loopInL;
        this.loopInR = loopInR;
        this.loopOutL = loopOutL;
        this.loopOutR = loopOutR;
        this.fadeOutTrigger = fadeOutTrigger;
    }

    public void processSample() {
        modOut.accept(modValue);
        triggerSet = triggerSet || triggerIn.getAsBoolean();

        if (sampleCount == 0) {
            if (triggerSet || isAutoRetrigger) {
                triggerNew();
                triggerSet = false;
            } else {
                loopOutL.accept(0.0);
                loopOutR.accept(0.0);
                return;
            }
        }

        sampleCount -= 1;
        modValue += modDelta;

        double loopInSampleL = loopInL.getAsDouble() * pan.getLeft();
        double loopInSampleR = loopInR.getAsDouble() * pan.getRight();

        if (sampleCount == fadeOutStart) {
            fadeCount = sampleCount;
            fadeValue = 1.0;
            fadeDelta = -fadeDelta;
            fadeOutTrigger.accept(5.0);
        } else {
            fadeOutTrigger.accept(0.0);
        }

        if (fadeCount == -1) {
            loopOutL.accept(loopInSampleL);
            loopOutR.accept(loopInSampleR);
            return;
        }

        double fadeValueSquared = fadeValue * fadeValue;
        double fadeValueCubed = fadeValueSquared * fadeValue;
        double fadeAmount = 3 * fadeValueSquared - 2 * fadeValueCubed;

        loopOutL.accept(loopInSampleL * fadeAmount);
        loopOutR.accept(loopInSampleR * fadeAmount);
        fadeValue += fadeDelta;
        fadeCount -= 1;
    }

    private void triggerNew() {
        double lengthValue = length.getAsDouble();
        double pitchValue = pitch.getAsDouble();
        pan.set(balance.getAsDouble());

        // Value between -7/12 and 19/12
        double pitchOffsetOctaves = (isOctaveUp ? 1.0 : 0.0)
                + pitchValue * ONE_OVER_TWELVE;
        double speedRatio = Math.pow(2.0, pitchOffsetOctaves);

        // By how much of the range should the tape head have moved if playing full length?
        double fullDelta = isReverse ? -1 - speedRatio  : speedRatio - 1;
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

    public String getEffectiveLengthDescription() {
        double lengthValue = length.getAsDouble();
        double pitchValue = pitch.getAsDouble();

        // Value between -7/12 and 19/12
        double pitchOffsetOctaves = (isOctaveUp ? 1.0 : 0.0)
                + pitchValue * ONE_OVER_TWELVE;
        // Make exp
        double speedRatio = Math.pow(2.0, pitchOffsetOctaves);

        // By how much of the range should the tape head have moved if playing full length?
        double fullDelta = isReverse ? -1 - speedRatio  : speedRatio - 1;

        double absDelta = Math.abs(fullDelta);
        double playLengthPercent = absDelta > 1.0
            ? lengthValue / absDelta
            : lengthValue;

        double playLengthMs = playLengthPercent * rangeMs;
        return String.format("%.3fs", playLengthMs * 0.001);
    }

}
