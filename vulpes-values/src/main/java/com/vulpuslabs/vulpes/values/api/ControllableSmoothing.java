package com.vulpuslabs.vulpes.values.api;

public interface ControllableSmoothing {

    void stopSmoothing();
    void startSmoothing(double decayRate);

}
