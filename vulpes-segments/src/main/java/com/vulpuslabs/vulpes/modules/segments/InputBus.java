package com.vulpuslabs.vulpes.modules.segments;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.inputs.DisconnectableInput;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class InputBus {

    private final BooleanSupplier inputTrigger;
    private final DoubleSupplier startCv;
    private final DoubleSupplier endCv;
    private final DoubleSupplier lengthCv;
    private final DisconnectableInput gateInput;
    private final DisconnectableInput cvInput;
    private double gateLengthPercent;
    private LengthRange lengthRange;

    public InputBus(BooleanSupplier inputTrigger,
                    DoubleSupplier startCv,
                    DoubleSupplier endCv,
                    DoubleSupplier lengthCv,
                    DisconnectableInput gateInput,
                    DisconnectableInput cvInput) {
        this.inputTrigger = inputTrigger;
        this.startCv = startCv;
        this.endCv = endCv;
        this.lengthCv = lengthCv;
        this.gateInput = gateInput;
        this.cvInput = cvInput;
    }

    public boolean hasTriggered() {
        return inputTrigger.getAsBoolean();
    }

    public double getStart() {
        return startCv.getAsDouble();
    }

    public double getEnd() {
        return endCv.getAsDouble();
    }

    public double getLength() {
        return lengthCv.getAsDouble();
    }

    public boolean gateIsConnected() {
        return gateInput.isConnected();
    }

    public boolean cvIsConnected() {
        return cvInput.isConnected();
    }

    public void setGateLengthPercent(double gateLengthPercent) {
        this.gateLengthPercent = gateLengthPercent;
    }

    public double getGateLengthPercent() {
        return gateLengthPercent;
    }

    public void setLengthRange(double switchValue) {
        lengthRange = LengthRange.fromDouble(switchValue);
    }

    public int getLengthSamples() {
        return (int) lengthRange.getLengthSamples(lengthCv.getAsDouble());
    }

    public double getGate() {
        return gateInput.getAsDouble();
    }

    public double getCv() {
        return cvInput.getAsDouble();
    }

    public String getRangeDescription() {
        return lengthRange.getDescription();
    }

    public String getLengthKnobValueDescription(double knobValue) {
        return lengthRange.describeCv(knobValue);
    }
}
