package com.vulpuslabs.vulpes.modules.torment;

import com.vulpuslabs.vulpes.values.events.UIEventConnector;
import voltage.core.VoltageAudioJack;

import java.util.function.DoubleSupplier;

public class Connector {

    private final UIEventConnector eventConnector;

    public Connector(UIEventConnector eventConnector) {
        this.eventConnector = eventConnector;
    }

    public InputBus connectInputs(
            VoltageAudioJack carrierInJack,
            VoltageAudioJack modulationInJack,
            DoubleSupplier carrierAmp,
            DoubleSupplier modulationAmp,
            DoubleSupplier carrierFeedbackAmp,
            DoubleSupplier modulationFeedbackAmp,
            DoubleSupplier outputAmp) {
        
        InputBus inputBus = new InputBus(
                eventConnector.connectMonoInput(carrierInJack),
                eventConnector.connectMonoInput(modulationInJack),
                carrierAmp,
                modulationAmp,
                carrierFeedbackAmp,
                modulationFeedbackAmp,
                outputAmp);

        return inputBus;
    }

    public OutputBus connectOutputs(VoltageAudioJack outputJack) {
        return new OutputBus(eventConnector.connectMonoOutput(outputJack));
    }

    public void connectController(Controller controller, Object carrierNormSwitch, Object modulationNormSwitch, Object carrierRectificationSwitch, Object modulationRectificationSwitch, Object oversampleButton, Object dcBiasSwitch, Object outputNormSwitch, Object tormentorSelector) {
        eventConnector.connectThreeStateSwitch(carrierNormSwitch, controller::setCarrierNorm);
        eventConnector.connectThreeStateSwitch(modulationNormSwitch, controller::setModulationNorm);
        eventConnector.connectThreeStateSwitch(outputNormSwitch, controller::setOutputNorm);
        eventConnector.connectTwoStateSwitch(carrierRectificationSwitch, controller::setCarrierRectification);
        eventConnector.connectTwoStateSwitch(modulationRectificationSwitch, controller::setModulationRectification);
        eventConnector.getEventBus().registerDoubleObserver(oversampleButton, controller::setOversampling);
        eventConnector.connectTwoStateSwitch(dcBiasSwitch, controller::setDcBiasCorrection);
        eventConnector.getEventBus().registerDoubleObserver(tormentorSelector, controller::setTormentor);
    }
}
