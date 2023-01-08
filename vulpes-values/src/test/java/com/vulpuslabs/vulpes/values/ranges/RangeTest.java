package com.vulpuslabs.vulpes.values.ranges;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RangeTest {

    @Test
    public void rebounding() {
        var transform = Range.CV_BIPOLAR.clampTo(Range.UNIT_UNIPOLAR);
        transform = Range.CV_BIPOLAR.clampTo(new Range(0.3, 0.7));

        assertNear(0.3, transform.apply(-6.0));
        assertNear(0.3, transform.apply(-5.0));
        assertNear(0.5, transform.apply(0.0));
        assertNear(0.7, transform.apply(5.0));
        assertNear(0.7, transform.apply(6.0));
    }

    private void assertNear(double expected, double actual) {
        assertTrue(Math.abs(expected - actual) < 1e-15);
    }
}
