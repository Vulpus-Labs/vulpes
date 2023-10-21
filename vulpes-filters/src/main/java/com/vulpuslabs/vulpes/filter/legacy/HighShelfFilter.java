package com.vulpuslabs.vulpes.filter.legacy;

import com.vulpuslabs.vulpes.filter.legacy.BaseFilter;

public final class HighShelfFilter extends BaseFilter {

    public HighShelfFilter(int sampleRate) {
        super(sampleRate, true);
    }

    @Override
    protected void configureFilter(double gainAbs, double omega, double sn, double cs, double alpha, double beta) {
        configureBiquadFilter(
                (gainAbs + 1) - (gainAbs - 1) * cs + beta * sn,
                2 * ((gainAbs - 1) - (gainAbs + 1) * cs),
                (gainAbs + 1) - (gainAbs - 1) * cs - beta * sn,
                gainAbs * ((gainAbs + 1) + (gainAbs - 1) * cs + beta * sn),
                -2 * gainAbs * ((gainAbs - 1) + (gainAbs + 1) * cs),
                gainAbs * ((gainAbs + 1) + (gainAbs - 1) * cs - beta * sn));
    }
}
