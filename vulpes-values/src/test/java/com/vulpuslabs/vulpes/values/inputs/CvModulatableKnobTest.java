package com.vulpuslabs.vulpes.values.inputs;

import com.vulpuslabs.vulpes.values.ranges.Range;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CvModulatableKnobTest {

    /*
    private final CvModulatableKnob unit = new CvModulatableKnob(
            5, 5000,
            new DisconnectableInput(this::getCvValue),
            100
    );

    private double cvValue;
    private double modulationAmount;
    private double knobValue;

    private double getCvValue() {
        return cvValue;
    }

    public void whenCvValueIs(double cvValue) {
        this.cvValue = cvValue;
    }

    @Test
    public void cvDisconnected() {
        unit.setCvIsConnected(false);
        unit.setModulationAmount(0.3);

        assertEquals(100.0, unit.getUnsmoothed());

        unit.setKnobValue(200.0);
        assertEquals(200.0, unit.getUnsmoothed());
    }

    @Test
    public void cvConnected() {
        unit.setCvIsConnected(true);
        unit.setModulationAmount(-1.0);

        whenCvValueIs(-5.0);
        assertEquals(5.0, unit.getUnsmoothed());

        whenCvValueIs(5.0);
        assertEquals(100.0, unit.getUnsmoothed());

        unit.setModulationAmount(1.0);
        whenCvValueIs(-5.0);
        assertEquals(100.0, unit.getUnsmoothed());

        whenCvValueIs(5.0);
        assertEquals(5000.0, unit.getUnsmoothed());

        unit.setModulationAmount(0.5);
        whenCvValueIs(-5.0);
        assertEquals(100.0, unit.getUnsmoothed());

        whenCvValueIs(5.0);
        assertEquals(2550.0, unit.getUnsmoothed());
    }

     */
}
