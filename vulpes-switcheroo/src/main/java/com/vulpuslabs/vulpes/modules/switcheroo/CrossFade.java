package com.vulpuslabs.vulpes.modules.switcheroo;

public final class CrossFade {

    private int count;
    private double position;
    private final double delta;
    private final Channel target;

    public CrossFade(int lengthSamples, Channel target) {
        this.count = lengthSamples;
        position = 0.0;
        delta = 0.5 / lengthSamples;
        this.target = target;
    }

    public double applyTo(double fadedSample) {
        var targetSample = target.getValue();
        double interp = targetSample - fadedSample;
        double amount = 4 * (position - (position * position));

        count -= 1;
        position += delta;

        return fadedSample + (interp * amount);
    }

    public boolean isEnded() {
        return count == 0;
    }

    public Channel getTarget() {
        return target;
    }
}
