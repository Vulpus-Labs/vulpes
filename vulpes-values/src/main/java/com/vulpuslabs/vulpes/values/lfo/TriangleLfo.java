package com.vulpuslabs.vulpes.values.lfo;

import com.vulpuslabs.vulpes.values.oscillators.OscillatorFromGenerator;


public class TriangleLfo extends OscillatorFromGenerator {

    public TriangleLfo(double pos) {
        super(48000, (value) ->
                4.0 * (Math.abs(Math.round(value) - value) - 0.25)
        );
        this.setPosition(pos);
    }

    public TriangleLfo() {
        this(0.0);
    }


}
