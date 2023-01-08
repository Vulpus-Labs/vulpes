package com.vulpuslabs.vulpes.modules.swirl;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.polar.PolarInterpolator;
import com.vulpuslabs.vulpes.values.ranges.Range;

import java.util.function.DoubleSupplier;

public class InterpolationController {

    private static final DoubleTransformer DESYNC_TRANSFORMER =
            Range.UNIT_UNIPOLAR.to(new Range(1.0, 1.1));

    private final DoubleSupplier randomSource;
    private int interpolationLengthSamples = 48000;
    private boolean desync;
    private boolean autoRetrig;

    public InterpolationController(DoubleSupplier randomSource) {
        this.randomSource = randomSource;
    }

    public void setDesync(boolean desync) {
        this.desync = desync;
    }

    public void setAutoRetrig(boolean autoRetrig) {
        this.autoRetrig = autoRetrig;
    }

    public void setInterpolationLengthMs(SwirlModel model, double interpolationLengthMs) {
        int newInterpolationLengthSamples = Math.max(1, (int) (interpolationLengthMs * 48.0));
        if (newInterpolationLengthSamples != interpolationLengthSamples) {
            double ratio = (double) newInterpolationLengthSamples / interpolationLengthSamples;
            interpolationLengthSamples = newInterpolationLengthSamples;
            model.adjustSpeed(ratio);
        }
    }

    public void setNewTargets(SwirlModel model) {
        model.setNewTargets(this::setTarget);
    }

    public void applyInterpolation(SwirlModel model) {
        model.applyInterpolation(autoRetrig ? this::setTarget : this::noop);
    }

    private void setTarget(PolarInterpolator interpolator) {
        double desyncAmount = desync
                ? DESYNC_TRANSFORMER.apply(randomSource.getAsDouble())
                : 1.0;
        interpolator.setTarget(
                randomSource.getAsDouble(),
                randomSource.getAsDouble() * 5.0,
                (int) (interpolationLengthSamples * desyncAmount));
    }

    private void noop(PolarInterpolator interpolator) {

    }
}
