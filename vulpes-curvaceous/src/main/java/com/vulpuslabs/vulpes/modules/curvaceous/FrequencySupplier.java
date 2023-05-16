package com.vulpuslabs.vulpes.modules.curvaceous;

import com.vulpuslabs.vulpes.values.api.IndexedDoubleSupplier;
import com.vulpuslabs.vulpes.values.events.TwoPositionSwitchState;

import java.util.function.DoubleSupplier;

public class FrequencySupplier {

    private static final double ONE_OVER_TWELVE = 1.0 / 12.0;
    private static final double[] BASE_FREQUENCIES = {
            8.175799,
            16.35160,
            32.70320,
            65.40639,
            130.8128,
            261.2656};

    private final IndexedDoubleSupplier freqInput;
    private final IndexedDoubleSupplier syncInput;
    private final PolyFMControl polyFmControl;
    private final FMControl monoFmControl;
    private final DoubleSupplier pitch;
    private TwoPositionSwitchState polyFmIsExp = TwoPositionSwitchState.OFF;
    private double baseFrequency = BASE_FREQUENCIES[3];
    private int numberOfVoices = 1;
    private final double[] frequencies = new double[16];
    private final boolean[] oldSyncs = new boolean[16];
    private final boolean[] newSyncs = new boolean[16];

    public FrequencySupplier(IndexedDoubleSupplier freqInput,
                             IndexedDoubleSupplier syncInput,
                             PolyFMControl polyFmControl,
                             FMControl monoFmControl,
                             DoubleSupplier pitch) {
        this.freqInput = freqInput;
        this.syncInput = syncInput;
        this.polyFmControl = polyFmControl;
        this.monoFmControl = monoFmControl;
        this.pitch = pitch;
    }

    public void setBaseFrequency(int index) {
        baseFrequency = BASE_FREQUENCIES[index];
    }

    public void setNumberOfVoices(int numberOfVoices) {
        this.numberOfVoices = numberOfVoices;
    }

    public void tick() {
        double monoFmValue = monoFmControl.getAsDouble();
        polyFmControl.tick();
        double freqOffset = pitch.getAsDouble() * ONE_OVER_TWELVE;

        for (int i=0; i<numberOfVoices; i++) {
            double freqCv = freqInput.getAsDouble(i) + freqOffset;

            if (monoFmControl.isConnectedExponential()) freqCv += monoFmValue * 0.1;
            if (polyFmControl.isConnectedExponential()) freqCv += polyFmControl.getAsDouble(i) * 0.1;

            double freqHz = baseFrequency * Math.pow(2.0, freqCv);

            if (monoFmControl.isConnectedLinear()) freqHz += monoFmValue;
            if (polyFmControl.isConnectedLinear()) freqHz += polyFmControl.getAsDouble(i);

            frequencies[i] = freqHz;
        }
    }

    public double getFrequency(int channel) {
        return frequencies[channel];
    }
}
