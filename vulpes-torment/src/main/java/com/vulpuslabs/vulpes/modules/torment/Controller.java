package com.vulpuslabs.vulpes.modules.torment;

import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.events.ThreePositionSwitchState;
import com.vulpuslabs.vulpes.values.events.TwoPositionSwitchState;
import com.vulpuslabs.vulpes.values.ranges.Range;
import com.vulpuslabs.vulpes.values.smoothed.DCBlocker;

public class Controller {

    interface Tormentor {
        double torment(double carrier, double modulation);

    }

    private static final Range CLAMP_RANGE = Range.UNIT_BIPOLAR;

    private final InputBus inputBus;
    private final OutputBus outputBus;
    private final VoltageMeter carrierMeter = new VoltageMeter();
    private final VoltageMeter modulationMeter = new VoltageMeter();
    private final VoltageMeter outputMeter = new VoltageMeter();
    private DoubleTransformer carrierNorm;
    private DoubleTransformer modulationNorm;
    private DoubleTransformer outputNorm;

    private final Resampler resampler;

    private boolean carrierRectification;
    private boolean modulationRectification;

    private double carrierFeedbackUnitSample;
    private double modulationFeedbackUnitSample;
    private boolean dcBiasCorrection;

    private boolean oversampling;
    private final DCBlocker dcBlocker = new DCBlocker();

    private Tormentor tormentor;

    public Controller(InputBus inputBus, OutputBus outputBus) {
        this.inputBus = inputBus;
        this.outputBus = outputBus;
        carrierNorm = this::clamp;
        modulationNorm = this::clamp;
        outputNorm = this::clamp;
        resampler = new Resampler(48000, 8, 22e3, this::sum);
    }

    public void processSample() {
        // Obtain input signals, attenuvert and add feedback
        var carrierUnitSample = inputBus.getCarrierSample() * inputBus.getCarrierAmp() * 0.2
                + carrierFeedbackUnitSample;
        var modulationUnitSample = inputBus.getModulationSample() * inputBus.getModulationAmp() * 0.2
                + modulationFeedbackUnitSample;

        // Norm samples
        carrierUnitSample = carrierNorm.apply(carrierUnitSample);
        modulationUnitSample = modulationNorm.apply(modulationUnitSample);

        // Rectify as a final step
        if (carrierRectification) carrierUnitSample = Math.abs(carrierUnitSample);
        if (modulationRectification) modulationUnitSample = Math.abs(modulationUnitSample);

        // Write to input meters
        carrierMeter.accept(carrierUnitSample);
        modulationMeter.accept(modulationUnitSample);

        // Torment
        var tormentedSample = oversampling
            ? resampler.apply(carrierUnitSample, modulationUnitSample)
            : tormentor.torment(carrierUnitSample, modulationUnitSample);

        // Attenuvert and norm output
        var outputUnitSample = outputNorm.apply(tormentedSample * inputBus.getOutputScaling());

        // Get DC-blocked version for feedback
        var dcBlocked = dcBlocker.apply(outputUnitSample);

        // Optionally feed DC-blocked signal to output
        outputUnitSample = dcBiasCorrection ? dcBlocked : outputUnitSample;

        carrierFeedbackUnitSample = dcBlocked * inputBus.getCarrierFeedbackScaling();
        modulationFeedbackUnitSample = dcBlocked * inputBus.getModulationFeedbackScaling();

        // Write to output and output meter
        outputMeter.accept(outputUnitSample);
        outputBus.setOutputSample(outputUnitSample * 5.0);
    }

    public double getCarrierMeter() {
        return carrierMeter.getValue();
    }

    public double getModulationMeter() {
        return modulationMeter.getValue();
    }

    public double getOutputMeter() {
        return outputMeter.getValue();
    }

    private double clamp(double unitSample) {
        return CLAMP_RANGE.clamp(unitSample);
    }

    private double fold(double unitSample) {
        return Approximate.sinUnit(unitSample * 0.25);
    }

    private double sat(double unitSample) {
        return Approximate.tanh(unitSample);
    }

    public void setCarrierNorm(ThreePositionSwitchState switchState) {
        carrierNorm = switch (switchState) {
            case LOW -> this::sat;
            case MID -> this::fold;
            case HIGH -> this::clamp;
        };
    }

    public void setModulationNorm(ThreePositionSwitchState switchState) {
        modulationNorm = switch (switchState) {
            case LOW -> this::sat;
            case MID -> this::fold;
            case HIGH -> this::clamp;
        };
    }

    public void setOutputNorm(ThreePositionSwitchState switchState) {
        outputNorm = switch (switchState) {
            case LOW -> this::sat;
            case MID -> this::fold;
            case HIGH -> this::clamp;
        };
    }

    private double sum(double carrier, double modulation) {
        return (carrier + modulation) * 0.5;
    }

    private double amplitudeModulation(double carrier, double modulation) {
        return carrier * (modulation * 0.5 + 0.5);
    }

    private double ringModulation(double carrier, double modulation) {
        return carrier * modulation;
    }

    private double analogRingModulation(double carrier, double modulation) {
        double a = 0.5 * carrier + modulation;
        double b = modulation - 0.5 * carrier;

        return waveShape(a) + waveShape(-a) - (waveShape(b) + waveShape(-b));
    }
    
    private double waveShape(double sample) {
        double scaled = sample * 5.0;
        return scaled < 0.0 ? 0.0 : 0.04 * Math.log(1.0 + Math.exp(10.0 * (scaled - 1.0)));
    }

    private double previous;
    private double deltaModulation(double carrier, double modulation) {
        var diff = carrier - previous;
        var modDiff = diff * (modulation * 0.5 + 0.5);
        previous += modDiff;
        return previous;
    }

    private double deltaRingModulation(double carrier, double modulation) {
        var diff = carrier - previous;
        var modDiff = diff * modulation;
        var result = previous + modDiff;
        previous = modulationRectification ? result : (carrier + result) * 0.5;
        return result;
    }

    public void setTormentor(double value) {
        tormentor = switch ((int) value) {
            case 1 -> this::amplitudeModulation;
            case 2 -> this::ringModulation;
            case 3 -> this::analogRingModulation;
            case 4 -> this::deltaModulation;
            case 5 -> this::deltaRingModulation;
            default -> this::sum;
        };

        resampler.setTormentor(tormentor);
    }

    public void setCarrierRectification(TwoPositionSwitchState state) {
        carrierRectification = state == TwoPositionSwitchState.ON;
    }

    public void setModulationRectification(TwoPositionSwitchState state) {
        modulationRectification = state == TwoPositionSwitchState.ON;
    }

    public void setOversampling(double oversampling) {
        this.oversampling = oversampling > 0.0;
    }

    public void setDcBiasCorrection(TwoPositionSwitchState state) {
        dcBiasCorrection = state == TwoPositionSwitchState.ON;
    }
}
