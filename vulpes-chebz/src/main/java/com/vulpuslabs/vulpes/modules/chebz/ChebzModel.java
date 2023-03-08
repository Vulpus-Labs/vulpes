package com.vulpuslabs.vulpes.modules.chebz;

import com.vulpuslabs.vulpes.values.api.Action;

import java.util.function.DoubleBinaryOperator;

import static com.vulpuslabs.vulpes.modules.chebz.HarmonicOscillator.*;

public class ChebzModel {

    private final InputBus inputBus;
    private final OutputBus outputBus;
    private HarmonicOscillator oscillator;
    private final double[] unmodulated = new double[10];
    private final double[] modulated = new double[10];
    private final double[] both = new double[3];
    private final double[] unmodulatedForDisplay = new double[10];
    private final double[] modulatedForDisplay = new double[10];
    private final double[] bothForDisplay = new double[3];
    private final double[] coefficients = new double[10];
    private double oddEvenBalance = 0.5;
    private Action sendAction;

    public ChebzModel(InputBus inputBus, OutputBus outputBus) {
        this.inputBus = inputBus;
        this.outputBus = outputBus;
        oscillator = new RawHarmonicOscillator(48000);
        sendAction = this::sendPostModulation;
    }

    public void setWavefoldingActive(boolean wavefoldingActive) {
        oscillator.setIsFolding(wavefoldingActive);
    }

    public void setOversamplingFactor(int factor) {
        var oldPosition = oscillator.getPosition();
        var wasFolding = oscillator.isFolding();
        if (factor == 1) {
            oscillator = new RawHarmonicOscillator(48000);
        } else {
            oscillator = new DownsampledHarmonicOscillator(48000, factor);
        }
        oscillator.setPosition(oldPosition);
        oscillator.setIsFolding(wasFolding);
    }

    public double getSampleAt(double pointer) {
        return oscillator.getSampleAt(pointer, oddEvenBalance, coefficients, unmodulatedForDisplay, modulatedForDisplay, bothForDisplay);
    }

    public void setModulationBypass(boolean modulationBypass) {
        sendAction = modulationBypass
                ? this::sendPreModulation
                : this::sendPostModulation;
    }

    private void sendPostModulation() {
        outputBus.sendIndividualOuts(modulated);
    }

    private void sendPreModulation() {
        outputBus.sendIndividualOuts(unmodulated);
    }

    public void processSample() {
        oddEvenBalance = inputBus.getOddEvenBalance();
        inputBus.getCoefficients(coefficients);

        oscillator.processSample(
                inputBus.getFrequencyHz(),
                oddEvenBalance,
                coefficients,
                unmodulated,
                modulated,
                both);

        sendAction.perform();

        outputBus.sendEvens(both[EVENS]);
        outputBus.sendOdds(both[ODDS]);
        outputBus.sendBoth(both[BOTH]);
    }
}
