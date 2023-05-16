package com.vulpuslabs.vulpes.modules.curvaceous;

import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.api.DoubleTransformer;

public enum CurveType implements DoubleTransformer {

    NONE(0, "None") {
        @Override
        public double apply(double input) {
            return 0;
        }
    },

    STEP(1, "Step") {
        @Override
        public double apply(double input) {
            return 0;
        }
    },

    LINEAR(2, "Linear") {
        @Override
        public double apply(double input) {
            return input;
        }
    },

    QUARTER_SINE(3, "Quarter Sine") {
        @Override
        public double apply(double input) {
            return Approximate.sinUnit(input * 0.25);
        }
    },

    QUARTER_COSINE(4, "Inverse Quarter Cosine") {
        @Override
        public double apply(double input) {
            return 1.0 - Approximate.cosUnit(input * 0.25);
        }
    },

    SINUSOID(5, "Sinusoid") {
        @Override
        public double apply(double input) {
            double squared = input * input;
            double cubed = squared * input;
            return 3 * squared - 2 * cubed;
        }
    };

    public static CurveType valueOf(double switchValue) {
        for (CurveType curveType : CurveType.values()) {
            if (curveType.getSwitchValue() == (int) switchValue) {
                return curveType;
            }
        }
        return CurveType.NONE;
    }

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
