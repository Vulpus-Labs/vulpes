package com.vulpuslabs.vulpes.values.polar;

public class PolarInterpolator {

    private final PolarValue value;
    private double radiusDelta;
    private double angleDelta;
    private int driftCount;

    public PolarInterpolator(PolarValue polarValue) {
        this.value = polarValue;
    }

    public void setTarget(double targetAngle, double targetRadius, int timeSamples) {
        this.driftCount = timeSamples;
        var reciprocal = 1.0 / timeSamples;

        var currentAngle = value.getAngle();
        var currentRadius = value.getRadius();

        var angleDistance = Math.abs(targetAngle - currentAngle);
        var angleDirection = Math.signum(targetAngle - currentAngle);

        // Always go there the quickest way you can
        this.angleDelta = angleDistance < 0.5
                ? angleDistance * angleDirection * reciprocal
                : (1.0 - angleDistance) * -angleDirection * reciprocal;

        this.radiusDelta = (targetRadius - currentRadius) * reciprocal;
    }

    public boolean tick() {
        if (driftCount == 0) {
            return true;
        }
        value.move(angleDelta, radiusDelta);
        driftCount -= 1;
        return false;
    }

    public void adjustSpeed(double ratio, double reciprocal) {
        driftCount *= ratio;
        angleDelta *= reciprocal;
        radiusDelta *= reciprocal;
    }
}
