package com.vulpuslabs.vulpes.modules.curvaceous;

import com.vulpuslabs.vulpes.values.api.IndexedDoubleSupplier;

public class SyncControl {

    private final IndexedDoubleSupplier syncSource;
    private final boolean[] isTriggering = new boolean[16];
    private final boolean[] wasGreaterThanZero = new boolean[16];
    private int numberOfVoices;
    private boolean isConnected;

    public SyncControl(IndexedDoubleSupplier syncSource) {
        this.syncSource = syncSource;
    }

    public void setNumberOfVoices(int numberOfVoices) {
        this.numberOfVoices = numberOfVoices;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public void tick() {
        if (!isConnected) return;
        for (int i=0; i<numberOfVoices; i++) {
            boolean wasPreviouslyGreaterThenZero = wasGreaterThanZero[i];
            boolean isGreaterThanZero = syncSource.getAsDouble(i) > 0.0;
            wasGreaterThanZero[i] = isGreaterThanZero;
            isTriggering[i] = isGreaterThanZero &! wasPreviouslyGreaterThenZero;
        }
    }

    public boolean isTriggering(int channel) {
        return isConnected && isTriggering[channel];
    }
}
