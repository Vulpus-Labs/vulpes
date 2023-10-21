package com.vulpuslabs.vulpes.values.inputs;

import com.vulpuslabs.vulpes.values.events.UIEventConnector;
import voltage.core.VoltageAudioJack;
import voltage.core.VoltageComponent;

import java.util.function.DoubleSupplier;

public class InputOrKnob implements DoubleSupplier {

    public static InputOrKnob connect(UIEventConnector connector,
                                      VoltageAudioJack input,
                                      VoltageComponent knob) {
        InputOrKnob result = new InputOrKnob(
                connector.connectMonoInput(input)
        );
        connector.connectUnsmoothedKnob(knob, result::setKnobValue);
        connector.getEventBus().registerBooleanObserver(input, result::setInputIsConnected);

        return result;
    }

    private final DisconnectableInput input;
    private double knobValue;
    private DoubleSupplier valueSupplier;

    public InputOrKnob(DisconnectableInput input) {
        this.input = input;
        valueSupplier = this::getKnobValue;
    }

    public void setKnobValue(double knobValue) {
        this.knobValue = knobValue;
    }

    private double getKnobValue() {
        return knobValue;
    }

    public void setInputIsConnected(boolean isConnected)   {
        valueSupplier = isConnected ? input : this::getKnobValue;
    }

    @Override
    public double getAsDouble() {
        return valueSupplier.getAsDouble();
    }
}
