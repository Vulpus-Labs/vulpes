package com.vulpuslabs.vulpes.modules.switcheroo;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.ranges.Range;

import java.util.function.DoubleSupplier;

public class GridModel {

    public static final DoubleTransformer RANDOM_CHANNEL = Range.UNIT_UNIPOLAR
            .to(new Range(0, 3));
    public static final DoubleTransformer RANDOM_STEP_COUNT = Range.UNIT_UNIPOLAR.to(
            new Range(2, 16));
    private final byte[] selectedChannels = new byte[16];
    private int currentStep = 0;
    private int stepCount = 16;

    public void advanceStep() {
        currentStep = (currentStep + 1) % stepCount;
    }

    public void resetStep() {
        currentStep = 0;
    }

    public int getCurrentSelectedChannel() {
        return selectedChannels[currentStep];
    }

    public void setSelectedChannel(int col, int selectedChannel) {
        selectedChannels[col] = (byte) selectedChannel;
    }

    public byte[] getSaveState() {
        byte[] saveState = new byte[17];
        System.arraycopy(selectedChannels, 0, saveState, 0, 16);
        saveState[16] = (byte) stepCount;
        return saveState;
    }

    public void setSaveState(byte[] saveState) {
        System.arraycopy(saveState, 0, selectedChannels, 0, 16);
        stepCount = (int) saveState[16];
    }

    public int getStepCount() {
        return stepCount;
    }

    public void randomise(DoubleSupplier randomSource) {
        for (int i=0; i<16; i++) {
            selectedChannels[i] = (byte) Math.round(RANDOM_CHANNEL.apply(randomSource.getAsDouble()));
        }
        stepCount = (int) Math.round(RANDOM_STEP_COUNT.apply(randomSource.getAsDouble()));
    }

    public void reset() {
        for (int i=0; i<16; i++) {
            selectedChannels[i] = 0;
        }
        stepCount = 16;
    }

    public int incrementStepCount() {
        stepCount = Math.min(stepCount + 1, 16);
        return stepCount;
    }

    public int decrementStepCount() {
        stepCount = Math.max(stepCount - 1, 2);
        return stepCount;
    }

    public int getSelectedChannel(int step) {
        return selectedChannels[step];
    }

    public int getCurrentStep() {
        return currentStep;
    }
}
