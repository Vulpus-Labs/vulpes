package com.vulpuslabs.vulpes.modules.segments;

import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public enum CurveType implements DoubleTransformer {

    LINEAR(0, "Linear") {
        @Override
        public double apply(double input) {
            return input;
        }
    },

    QUARTER_SINE(1, "Quarter Sine") {
        @Override
        public double apply(double input) {
            return Approximate.sinUnit(input * 0.25);
        }
    },

    QUARTER_COSINE(2, "Inverse Quarter Cosine") {
        @Override
        public double apply(double input) {
            return 1.0 - Approximate.cosUnit(input * 0.25);
        }
    },

    SINUSOID(3, "Sinusoid") {
        @Override
        public double apply(double input) {
            double squared = input * input;
            double cubed = squared * input;
            return 3 * squared - 2 * cubed;
        }
    };

    private final int switchValue;
    private final String name;

    CurveType(int switchValue, String name) {
        this.switchValue = switchValue;
        this.name = name;
    }

    public int getSwitchValue() {
        return switchValue;
    }

    public String getName() {
        return name;
    }

}
