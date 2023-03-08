package com.vulpuslabs.vulpes.modules.swirl;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.inputs.TriggerInput;
import com.vulpuslabs.vulpes.values.ranges.Range;
import com.vulpuslabs.vulpes.values.smoothed.SmoothedValue;

import java.util.function.DoubleSupplier;
import java.util.function.IntFunction;

public class SwirlInputs {

    private final DoubleSupplier randomSource;
    private final DoubleSupplier[] angleInputs;
    private final DoubleSupplier[] radiusInputs;
    private final DoubleSupplier thetaDriveInput;
    private final TriggerInput driftTrigger;
    private final int maxSize;
    private int activeSize;
    private double drive;
    private double driveMod;
    private boolean driveModConnected;
    private final SmoothedValue.Supplier driveSupplier = SmoothedValue.smooth(this::getDrive, 0.1);
    private DoubleTransformer radiusRangeTransformer = Range.CV_BIPOLAR.clampTo(Range.CV_UNIPOLAR);

    public SwirlInputs(DoubleSupplier randomSource, DoubleSupplier thetaDriveInput, DoubleSupplier driftTriggerInput, int maxSize, int activeSize) {
        this.randomSource = randomSource;
        this.thetaDriveInput = Range.CV_BIPOLAR.clampTo(Range.UNIT_BIPOLAR)
                .transforming(thetaDriveInput);
        this.driftTrigger = new TriggerInput(driftTriggerInput);
        this.maxSize = maxSize;
        this.activeSize = activeSize;
        this.angleInputs = new DoubleSupplier[maxSize];
        this.radiusInputs = new DoubleSupplier[maxSize];
    }

    public void connectAngles(IntFunction<DoubleSupplier> angleSupplier) {
        for (int i=0; i<maxSize; i++) {
            angleInputs[i] = angleSupplier.apply(i);
        }
    }

    public void connectRadii(IntFunction<DoubleSupplier> radiusSupplier) {
        for (int i=0; i<maxSize; i++) {
            radiusInputs[i] = radiusSupplier.apply(i);
        }
    }

    public void setDriftTriggerIsConnected(boolean isConnected) {
        driftTrigger.setIsConnected(isConnected);
    }

    public void setRadiusBipolar(boolean radiusBipolar) {
        if (radiusBipolar) {
            radiusRangeTransformer = Range.CV_BIPOLAR.clamper();
        } else {
            radiusRangeTransformer = Range.CV_BIPOLAR.clampTo(Range.CV_UNIPOLAR);
        }
    }

    public void setActiveSize(int activeSize) {
        this.activeSize = activeSize;
    }

    public void randomise(SwirlModel model) {
        for (int i=0; i<16; i++) {
            model.accept(i, randomSource.getAsDouble(),
                    randomSource.getAsDouble() * 5.0);
        }
    }

    public void setDrive(double drive) {
        this.drive = drive * 0.1;

        if (driveModConnected) {
            adjustRange();
        }
    }

    public void connectDriveMod() {
        driveModConnected = true;
        adjustRange();
    }

    private void adjustRange() {
        var driveModRange = driveMod < 0.0
                ? new Range(drive + (driveMod * drive), drive)
                : new Range(drive, drive + ((0.5 - drive) * driveMod));

        this.driveSupplier.setUnsmoothed(
                Range.CV_BIPOLAR.clampTo(driveModRange)
                .transforming(thetaDriveInput));
    }

    private double getDrive() {
        return drive;
    }

    public void disconnectDriveMod() {
        driveModConnected = false;
        driveSupplier.setUnsmoothed(this::getDrive);
    }

    public void setDriveMod(double driveMod) {
        this.driveMod = driveMod;

        if (driveModConnected) {
            adjustRange();
        }
    }

    public void readInputs(SwirlModel model) {
        var driveVal = driveSupplier.getAsDouble();

        for (int i=0; i<activeSize; i++) {
            model.accept(i,
                    driveVal * angleInputs[i].getAsDouble(),
                    radiusRangeTransformer.apply(radiusInputs[i].getAsDouble()));
        }
    }

    public boolean isDriftTriggered() {
        return driftTrigger.hasTriggered();
    }
}
