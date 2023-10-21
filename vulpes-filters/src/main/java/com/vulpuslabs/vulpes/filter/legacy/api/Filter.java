package com.vulpuslabs.vulpes.filter.legacy.api;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public interface Filter extends DoubleTransformer {

    void configure(double centerFreq, double q, double gainDb);

}
