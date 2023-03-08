package com.vulpuslabs.vulpes.values.api;

@FunctionalInterface
public interface BooleanConsumer {

    BooleanConsumer DO_NOTHING = (value) -> {};

    void accept(boolean value);

}
