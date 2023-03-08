package com.vulpuslabs.vulpes.values.lfo;

import com.vulpuslabs.vulpes.values.FakeTrig;
import com.vulpuslabs.vulpes.values.oscillators.OscillatorFromGenerator;


public class FakeSinLfo extends OscillatorFromGenerator {

    public FakeSinLfo(double pos) {
        super(48000, FakeTrig::fakeSinUnit);
        setPosition(pos);
    }

    public FakeSinLfo() {
        this(0.0);
    }

}
