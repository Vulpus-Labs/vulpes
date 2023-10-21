package com.vulpuslabs.vulpes.filter.legacy;

import com.vulpuslabs.vulpes.filter.legacy.BaseFilter;

public final class LowShelfFilter extends BaseFilter {

    public LowShelfFilter(int sampleRate) {
        super(sampleRate, true);
    }

    @Override
    protected void configureFilter(double gainAbs, double omega, double sn, double cs, double alpha, double beta) {
        double betaTimesSn = beta * sn;
        double gainMinusOne = gainAbs - 1;
        double gainPlusOne = gainAbs + 1;
        double gainMinusOneTimesCs = gainMinusOne * cs;
        double gainPlusOneTimesCs = gainPlusOne * cs;

        configureBiquadFilter(
                gainPlusOne + gainMinusOneTimesCs + betaTimesSn,
                -2.0 * (gainMinusOne + gainPlusOneTimesCs),
                gainPlusOne + gainMinusOneTimesCs - betaTimesSn,
                gainAbs * (gainPlusOne - gainMinusOneTimesCs + betaTimesSn),
                2 * gainAbs * (gainMinusOne - gainPlusOneTimesCs),
                gainAbs * (gainPlusOne - gainMinusOneTimesCs - betaTimesSn));
    }
}
