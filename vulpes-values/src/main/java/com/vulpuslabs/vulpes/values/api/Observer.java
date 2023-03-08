package com.vulpuslabs.vulpes.values.api;

public interface Observer {

    Object getComponent();
    void receiveNewValue(double newValue);
    void receiveNewStatus(boolean status);

}
