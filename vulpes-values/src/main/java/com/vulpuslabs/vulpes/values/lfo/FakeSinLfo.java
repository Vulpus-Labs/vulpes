package com.vulpuslabs.vulpes.values.lfo;

import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.oscillators.OscillatorFromGenerator;


public class FakeSinLfo extends OscillatorFromGenerator {

    public FakeSinLfo(double pos) {
        super(48000, Approximate::sinUnit);
        setPosition(pos);
    }

    public FakeSinLfo() {
        this(0.0);
    }

}
