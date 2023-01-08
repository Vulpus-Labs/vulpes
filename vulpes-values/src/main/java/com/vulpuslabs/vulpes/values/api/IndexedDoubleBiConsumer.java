package com.vulpuslabs.vulpes.values.api;

@FunctionalInterface
public interface IndexedDoubleBiConsumer {

    void accept(int index, double first, double second);

}
