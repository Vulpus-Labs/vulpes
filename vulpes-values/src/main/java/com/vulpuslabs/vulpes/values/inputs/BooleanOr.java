package com.vulpuslabs.vulpes.values.inputs;

import java.util.function.BooleanSupplier;

public class BooleanOr implements BooleanSupplier {

    private final BooleanSupplier lhs;
    private final BooleanSupplier rhs;

    public BooleanOr(BooleanSupplier lhs, BooleanSupplier rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public boolean getAsBoolean() {
        var rhsVal = rhs.getAsBoolean();
        return lhs.getAsBoolean() || rhsVal;
    }
}
