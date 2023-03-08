package com.vulpuslabs.vulpes.values.ranges;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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

    @Test
    public void clamping() {
        var range = new Range(-5.0, 5.0);

        assertNear(-5.0, range.clamp(-5.1));
        assertNear(-4.0, range.clamp(-4.0));
        assertNear(4.0, range.clamp(4.0));
        assertNear(5.0, range.clamp(5.1));

        range = new Range(0.0, 7.0);
        assertNear(0.0, range.clamp(-0.1));
        assertNear(0.0, range.clamp(0.0));
        assertNear(4.0, range.clamp(4.0));
        assertNear(7.0, range.clamp(7.1));

        range = new Range(-7.0, 0.0);
        assertNear(-7.0, range.clamp(-7.1));
        assertNear(-7.0, range.clamp(-7.0));
        assertNear(-3.0, range.clamp(-3.0));
        assertNear(0.0, range.clamp(0.1));

        range = new Range(-5.0, 6.0);

        assertNear(-5.0, range.clamp(-5.1));
        assertNear(-4.0, range.clamp(-4.0));
        assertNear(4.0, range.clamp(4.0));
        assertNear(6.0, range.clamp(6.1));
    }

    private void assertNear(double expected, double actual) {
        if(Math.abs(expected - actual) > 1e-15) {
            fail("Expected " + expected + " but was " + actual);
        }
    }
}
