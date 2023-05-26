package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;
import com.vulpuslabs.vulpes.values.inputs.DisconnectableInput;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class InputBus {

    private final DisconnectableInput inputL;
    private final DisconnectableInput inputR;

    // private final BooleanSupplier freezeGateSupplier;
    private final BooleanSupplier triggerSupplier;
    private final DoubleSupplier positionSupplier;
    private final DoubleSupplier lengthSupplier;
    private final DoubleSupplier pitchSupplier;
    private final DoubleSupplier fadeSupplier;
    // private final DoubleSupplier feedbackSupplier;
    private final DoubleSupplier mixSupplier;

    public InputBus(DisconnectableInput inputL,
                    DisconnectableInput inputR,
                    //BooleanSupplier freezeGateSupplier,
                    BooleanSupplier triggerSupplier,
                    DoubleSupplier positionSupplier,
                    DoubleSupplier lengthSupplier,
                    DoubleSupplier pitchSupplier,
                    DoubleSupplier fadeSupplier,
                    //DoubleSupplier feedbackSupplier,
                    DoubleSupplier mixSupplier) {
        this.inputL = inputL;
        this.inputR = inputR;
        // this.freezeGateSupplier = freezeGateSupplier;
        this.triggerSupplier = triggerSupplier;
        this.positionSupplier = positionSupplier;
        this.lengthSupplier = lengthSupplier;
        this.pitchSupplier = pitchSupplier;
        this.fadeSupplier = fadeSupplier;
        // this.feedbackSupplier = feedbackSupplier;
        this.mixSupplier = mixSupplier;
    }

    public boolean isTriggering() {
        return triggerSupplier.getAsBoolean();
    }

    public void readInputs(StereoSample inputSample) {
        double leftSample = inputL.getAsDouble();
        inputSample.set(leftSample,
                inputR.isConnected() ? inputR.getAsDouble() : leftSample);
    }

    public double getMix() {
        return mixSupplier.getAsDouble();
    }

    public double getLengthMs() {
        return lengthSupplier.getAsDouble();
    }

    public double getPositionMs() {
        return positionSupplier.getAsDouble();
    }

    public double getPitchCents() {
        return pitchSupplier.getAsDouble();
    }

    public double getFadePercent() {
        return fadeSupplier.getAsDouble();
    }
}
