package com.vulpuslabs.vulpes.modules.curvaceous;

import com.vulpuslabs.vulpes.values.events.TwoPositionSwitchState;
import com.vulpuslabs.vulpes.values.events.UIEventConnector;

import java.util.function.DoubleSupplier;

public class FMControl implements DoubleSupplier {



    private final DoubleSupplier fmInput;
    private final DoubleSupplier attenuvertor;
    private boolean inputIsConnected;
    private boolean isExponential;

    public FMControl(DoubleSupplier fmInput, DoubleSupplier attenuvertor) {
        this.fmInput = fmInput;
        this.attenuvertor = attenuvertor;
    }

    public void setIsExponential(TwoPositionSwitchState state) {
        isExponential = state == TwoPositionSwitchState.ON;
    }

    public void setIsConnected(boolean isConnected) {
        this.inputIsConnected = isConnected;
    }

    @Override
    public double getAsDouble() {
        return fmInput.getAsDouble() * attenuvertor.getAsDouble();
    }

    public boolean isConnectedLinear() {
        return inputIsConnected && !isExponential;
    }

    public boolean isConnectedExponential() {
        return inputIsConnected && isExponential;
    }
}
