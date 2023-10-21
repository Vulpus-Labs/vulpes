package com.vulpuslabs.vulpes.modules.torment;

import java.util.function.DoubleSupplier;

public class InputBus {

    private final DoubleSupplier carrierInput;
    private final DoubleSupplier modulationInput;
    private final DoubleSupplier carrierAmp;
    private final DoubleSupplier modulationAmp;
    private final DoubleSupplier carrierFeedback;
    private final DoubleSupplier modulationFeedback;
    private final DoubleSupplier outputAmp;

    public InputBus(DoubleSupplier carrierInput,
                    DoubleSupplier modulationInput,
                    DoubleSupplier carrierAmp,
                    DoubleSupplier modulationAmp,
                    DoubleSupplier carrierFeedback,
                    DoubleSupplier modulationFeedback,
                    DoubleSupplier outputAmp) {
        this.carrierInput = carrierInput;
        this.modulationInput = modulationInput;
        this.carrierAmp = carrierAmp;
        this.modulationAmp = modulationAmp;
        this.carrierFeedback = carrierFeedback;
        this.modulationFeedback = modulationFeedback;
        this.outputAmp = outputAmp;
    }

    public double getCarrierSample() {
        return carrierInput.getAsDouble();
    }

    public double getCarrierAmp() {
        return carrierAmp.getAsDouble();
    }

    public double getModulationSample() {
        return modulationInput.getAsDouble();
    }

    public double getModulationAmp() {
        return modulationAmp.getAsDouble();
    }

    public double getOutputScaling() {
        return outputAmp.getAsDouble();
    }
    
    public double getCarrierFeedbackScaling() {
        return carrierFeedback.getAsDouble();
    }

    public double getModulationFeedbackScaling() {
        return modulationFeedback.getAsDouble();
    }

}
