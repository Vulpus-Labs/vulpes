package com.vulpuslabs.vulpes.filter.api;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public interface Filter extends DoubleTransformer {

    void configure(double centerFreq, double q, double gainDb);

}
