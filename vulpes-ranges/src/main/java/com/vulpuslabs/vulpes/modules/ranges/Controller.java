package com.vulpuslabs.vulpes.modules.ranges;

public class Controller {

    private final RangeAdjuster[] adjusters;

    public Controller(RangeAdjuster...adjusters) {
        this.adjusters = adjusters;
    }

    public void processSample() {
        for (RangeAdjuster adjuster : adjusters) {
            adjuster.processSample();
        }
    }
}
