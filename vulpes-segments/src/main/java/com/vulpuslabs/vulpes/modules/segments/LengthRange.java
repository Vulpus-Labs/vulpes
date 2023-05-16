package com.vulpuslabs.vulpes.modules.segments;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.ranges.Range;

public enum LengthRange {

    TEN_MS(10, TimeUnit.MILLISECONDS),
    ONE_HUNDRED_MS(100, TimeUnit.MILLISECONDS),
    FIVE_HUNDRED_MS(500, TimeUnit.MILLISECONDS),
    ONE_SECOND(1000, TimeUnit.SECONDS),
    TEN_SECONDS(10000, TimeUnit.SECONDS);

    static LengthRange fromDouble(double switchValue) {
        return values()[(int) switchValue];
    }

    private final int ms;
    private final TimeUnit timeUnit;
    private final DoubleTransformer range;

    LengthRange(int ms, TimeUnit timeUnit) {
        this.ms = ms;
        this.timeUnit = timeUnit;
         this.range = Range.CV_BIPOLAR.clampTo(new Range(2, ms * 48));
    }

    public String getDescription() {
        return timeUnit.display(ms * 48);
    }

    public String describeCv(double cv) {
        return timeUnit.display((int) getLengthSamples(cv));
    }

    public double getLengthSamples(double lengthCv) {
        return range.apply(lengthCv);
    }

}
