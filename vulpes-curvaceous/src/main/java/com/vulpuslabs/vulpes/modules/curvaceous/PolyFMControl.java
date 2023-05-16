package com.vulpuslabs.vulpes.modules.curvaceous;

import com.vulpuslabs.vulpes.values.api.IndexedDoubleSupplier;
import com.vulpuslabs.vulpes.values.events.TwoPositionSwitchState;
import com.vulpuslabs.vulpes.values.events.UIEventConnector;

import java.util.function.DoubleSupplier;

public class PolyFMControl implements IndexedDoubleSupplier {

    private final IndexedDoubleSupplier fmInput;
    private final DoubleSupplier attenuvertor;
    private double attenuvertorValue;
    private boolean inputIsConnected;
    private boolean isExponential;

    public PolyFMControl(IndexedDoubleSupplier fmInput, DoubleSupplier attenuvertor) {
        this.fmInput = fmInput;
        this.attenuvertor = attenuvertor;
    }

    public void setIsExponential(TwoPositionSwitchState state) {
        isExponential = state == TwoPositionSwitchState.ON;
    }

    public void setIsConnected(boolean isConnected) {
        this.inputIsConnected = isConnected;
    }

    public void tick() {
        if (inputIsConnected) this.attenuvertorValue = attenuvertor.getAsDouble();
    }

    @Override
    public double getAsDouble(int index) {
        return fmInput.getAsDouble(index) * attenuvertorValue;
    }

    public boolean isConnectedLinear() {
        return inputIsConnected && !isExponential;
    }

    public boolean isConnectedExponential() {
        return inputIsConnected && isExponential;
    }
}
