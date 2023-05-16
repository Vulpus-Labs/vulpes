package com.vulpuslabs.vulpes.modules.segments;


public class Curve {

    private CurveType curveFunction = CurveType.LINEAR;

    private int sampleCount;
    private int gateSampleCount;
    private double start;
    private double end;
    private double position;
    private double delta;

    public void start(double start, double end, int lengthSamples, int gateLengthSamples) {
        sampleCount = lengthSamples;
        gateSampleCount = gateLengthSamples;
        position = 0.0;
        this.start = start;
        this.end = end;
        delta = 1.0 / lengthSamples;
    }

    public boolean isRunning() {
        return sampleCount > 0;
    }

    public double getCv(double passThrough) {
        if (sampleCount == 0) return passThrough;

        double curved = curveFunction.apply(position);
        double value = end * curved + start * (1.0 - curved);
        position += delta;
        sampleCount -= 1;

        return value;
    }

    public void update(double start, double end) {
        this.start = start;
        this.end = end;
    }

    public double getGate(double passThrough) {
        if (sampleCount == 0) return passThrough;

        if (gateSampleCount == 0) return 0.0;
        gateSampleCount -= 1;
        return 5.0;
    }

    public void setCurveType(double switchValue) {
        for (CurveType curve : CurveType.values()) {
            if (curve.getSwitchValue() == (int) switchValue) {
                curveFunction = curve;
                return;
            }
        }
    }

    public String getSelectedCurveName() {
        return curveFunction.getName();
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }
}
