package com.vulpuslabs.vulpes.values.events;

import org.junit.jupiter.api.Test;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UIEventConnectorTest {

    private double value = 42;

    public double GetValue() {
        return value;
    }

    public void SetValue(double value) {
        this.value = value;
    }

    @Test
    public void bindGetValueAsDoubleSupplier() {
        EventBus eventBus = new EventBus();
        UIEventConnector connector = new UIEventConnector(eventBus);

        DoubleConsumer consumer = connector.getDoubleConsumer(this, "SetValue");
        consumer.accept(23.0);

        DoubleSupplier supplier = connector.getDoubleSupplier(this, "GetValue");

        assertEquals(23.0, supplier.getAsDouble());
    }
}
