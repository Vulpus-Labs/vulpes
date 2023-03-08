package com.vulpuslabs.vulpes.modules.switcheroo;

public class OutputBus {

    private double outputSample;
    private boolean crossfadeTo1Triggered;
    private boolean crossfadeTo2Triggered;
    private boolean crossfadeTo3Triggered;
    private boolean crossfadeTo4Triggered;
    private boolean stateChanged;

    public void reset() {
        crossfadeTo1Triggered = false;
        crossfadeTo2Triggered = false;
        crossfadeTo3Triggered = false;
        crossfadeTo4Triggered = false;
        stateChanged = false;
    }

    public double getOutputSample() {
        return outputSample;
    }

    public void setOutputSample(double outputSample) {
        this.outputSample = outputSample;
    }

    public void onCrossfadeTriggered(int targetChannel) {
        stateChanged = true;
        switch(targetChannel) {
            case 0 -> crossfadeTo1Triggered = true;
            case 1 -> crossfadeTo2Triggered = true;
            case 2 -> crossfadeTo3Triggered = true;
            default -> crossfadeTo4Triggered = true;
        }
    }

    public void onClock() {
        stateChanged = true;
    }

    public void onReset() {
        stateChanged = true;
    }

    public boolean isCrossfadeTo1Triggered() {
        return crossfadeTo1Triggered;
    }

    public boolean isCrossfadeTo2Triggered() {
        return crossfadeTo2Triggered;
    }

    public boolean isCrossfadeTo3Triggered() {
        return crossfadeTo3Triggered;
    }

    public boolean isCrossfadeTo4Triggered() {
        return crossfadeTo4Triggered;
    }

    public boolean isStateChanged() {
        return stateChanged;
    }
}
