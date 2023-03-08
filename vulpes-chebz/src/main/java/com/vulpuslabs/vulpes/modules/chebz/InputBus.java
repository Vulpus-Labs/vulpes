package com.vulpuslabs.vulpes.modules.chebz;

import com.vulpuslabs.vulpes.values.api.ControllableSmoothing;
import com.vulpuslabs.vulpes.values.inputs.ControllableSmoothedInput;
import com.vulpuslabs.vulpes.values.inputs.CvModulatableKnob;
import com.vulpuslabs.vulpes.values.inputs.DisconnectableInput;

import java.util.function.DoubleSupplier;

public class InputBus implements ControllableSmoothing {

    private final DoubleSupplier smoothedFrequency;
    private final DoubleSupplier voctInput;
    private final DoubleSupplier fmInput;
    private final ControllableSmoothedInput[] coefficientKnobs = new ControllableSmoothedInput[10];
    private final DoubleSupplier oddEvenBalance;
    private double fmAmount = 0.0;
    private boolean voctIsConnected;
    private boolean fmIsConnected;

    public InputBus(CvModulatableKnob[] coefficientKnobs,
                    DisconnectableInput voctInput,
                    DisconnectableInput fmInput,
                    DoubleSupplier frequencyKnob,
                    DoubleSupplier oddEvenBalance) {
        this.smoothedFrequency = frequencyKnob;

        for (int i=0; i<10; i++) {
            this.coefficientKnobs[i] = new ControllableSmoothedInput(coefficientKnobs[i]);
        }

        this.voctInput = voctInput;
        voctInput.onConnectionStatusChanged(this::setVoctIsConnected);
        this.fmInput = fmInput;
        fmInput.onConnectionStatusChanged(this::setFmIsConnected);
        this.oddEvenBalance = oddEvenBalance;
    }

    public void setFmAmount(double fmAmount) {
        this.fmAmount = fmAmount;
    }

    private void setVoctIsConnected(boolean isConnected) {
        voctIsConnected = isConnected;
    }

    private void setFmIsConnected(boolean isConnected) {
        fmIsConnected = isConnected;
    }

    public double getFrequencyHz() {
        var baseFreq = smoothedFrequency.getAsDouble();

         if (voctIsConnected) {
             if (fmIsConnected) {
                 var fm = fmInput.getAsDouble() * fmAmount * 0.1;
                 baseFreq *= Math.pow(2.0, voctInput.getAsDouble() + fm);
             } else {
                 baseFreq *= Math.pow(2.0, voctInput.getAsDouble());
             }
         } else if (fmIsConnected) {
             var fm = fmInput.getAsDouble() * fmAmount * 0.1;
             baseFreq *= Math.pow(2.0, fm);
         }

         return baseFreq;
    }

    public void getCoefficients(double[] coefficients) {
        for (int i=0; i<10; i++) {
            coefficients[i] = coefficientKnobs[i].getAsDouble();
        }
    }

    public double getOddEvenBalance() {
        return oddEvenBalance.getAsDouble();
    }

    @Override
    public void stopSmoothing() {
        for (int i=0; i<10; i++) {
            coefficientKnobs[i].stopSmoothing();
        }
    }

    @Override
    public void startSmoothing(double decayRate) {
        for (int i=0; i<10; i++) {
            coefficientKnobs[i].startSmoothing(decayRate);
        }
    }
}
