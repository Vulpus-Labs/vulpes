package com.vulpuslabs.vulpes.modules.crosstalk;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public class SimpleTiltFilter implements DoubleTransformer {

    public static SimpleTiltFilter create(double gain, FilterConfiguration filterConfiguration) {
        double ampReciprocal = Math.log(2) / 6.0;

        double gfactor = 5;
        double g1 = (gain > 0) ? -gfactor * gain : -gain;
        double g2 = (gain > 0) ? gain : gfactor * gain;

        double lgain = Math.exp(g1 * ampReciprocal) - 1;
        double hgain = Math.exp(g2 * ampReciprocal) - 1;

        return new SimpleTiltFilter(lgain, hgain, filterConfiguration);
    }

    private final double lgain;
    private final double hgain;
    private final FilterConfiguration filterConfiguration;
    private double lpOut;

    private SimpleTiltFilter(double lgain, double hgain, FilterConfiguration filterConfiguration) {
        this.filterConfiguration = filterConfiguration;
        this.lgain = lgain;
        this.hgain = hgain;
    }

    @Override
    public double apply(double in) {
        lpOut = (filterConfiguration.getA0() * in) + (filterConfiguration.getB1() * lpOut);
        return in + (lgain * lpOut) + (hgain * (in - lpOut));
    }
}
