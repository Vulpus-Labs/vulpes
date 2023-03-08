package com.vulpuslabs.vulpes.modules.swirl;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.ranges.Range;

import java.awt.*;

public class SwirlController {

    private static final Range LONG_INTERPOLATION = new Range(60000, 600000);
    private static final Range MEDIUM_INTERPOLATION = new Range(500, 60000);
    private static final Range SHORT_INTERPOLATION = new Range(1, 500);
    private static final DoubleTransformer LONG_TRANSFORM = Range.UNIT_UNIPOLAR.to(LONG_INTERPOLATION);
    private static final DoubleTransformer MEDIUM_TRANSFORM = Range.UNIT_UNIPOLAR.to(MEDIUM_INTERPOLATION);
    private static final DoubleTransformer SHORT_TRANSFORM = Range.UNIT_UNIPOLAR.to(SHORT_INTERPOLATION);

    private final SwirlModel model;
    private final SwirlView view;
    private final SwirlInputs inputs;
    private final SwirlOutputs outputs;
    private final InterpolationController interpolationController;

    private boolean isProcessingAudio;
    private int intermediateCount;
    private int interpolationRange = 2;
    private DoubleTransformer interpolationLengthTransformer = MEDIUM_TRANSFORM;
    private int intermediateInterval = 20;

    public SwirlController(SwirlModel model,
                           SwirlView view,
                           SwirlInputs inputs,
                           SwirlOutputs outputs,
                           InterpolationController interpolationController) {
        this.model = model;
        this.view = view;

        this.inputs = inputs;
        this.outputs = outputs;

        this.interpolationController = interpolationController;
    }

    public void redraw(Graphics2D graphics) {
        view.redraw(graphics);
    }

    public void initialise() {
        inputs.randomise(model);
    }

    public void setDesync(boolean desync) {
        interpolationController.setDesync(desync);
    }

    public void setAutoRetrig(boolean autoRetrig) {
        interpolationController.setAutoRetrig(autoRetrig);
    }

    public void setRadiusBipolar(boolean radiusBipolar) {
        inputs.setRadiusBipolar(radiusBipolar);
    }

    public void connectTriggerDriftInput() {
        inputs.setDriftTriggerIsConnected(true);
    }

    public void disconnectTriggerDriftInput() {
        inputs.setDriftTriggerIsConnected(true);
    }

    public void startProcessingAudio() {
        isProcessingAudio = true;
        intermediateInterval = 5;
        view.setDrawHeads(false);
    }

    public void stopProcessingAudio() {
        isProcessingAudio = false;
        intermediateInterval = 20;
        interpolationController.setNewTargets(model);
        view.setDrawHeads(true);
    }

    public void setBlockingDc(boolean blockingDc) {
        outputs.setBlockingDc(blockingDc);
    }

    public void setInterpolationRange(int range, double interpolationLengthUnit) {
        interpolationRange = range;
        interpolationLengthTransformer = switch (range) {
            case 0 -> SHORT_TRANSFORM;
            case 1 -> MEDIUM_TRANSFORM;
            default -> LONG_TRANSFORM;
        };

        setInterpolationLength(interpolationLengthUnit);
    }

    public String describeInterpolationRange() {
        return switch (interpolationRange) {
            case 0 -> "1-500ms";
            case 1 -> "0.5-60s";
            default -> "60-600s";
        };
    }

    public void setInterpolationLength(double interpolationLengthUnit) {
        interpolationController.setInterpolationLengthMs(
                model,
                interpolationLengthTransformer.apply(interpolationLengthUnit));
    }

    public String describeInterpolationLength(double interpolationLengthUnit) {
        var ms = interpolationLengthTransformer.apply(interpolationLengthUnit);

        return switch (interpolationRange) {
            case 0, 1 -> (int) ms + "ms";
            default -> (int) (ms / 1000) + "s";
        };
    }

    public void setActiveSize(int activeSize) {
        view.setActiveSize(activeSize);
        inputs.setActiveSize(activeSize);
        outputs.setActiveSize(activeSize);
    }

    public void setDrive(double drive) {
        inputs.setDrive(drive);
    }

    public void setDriveMod(double driveMod) {
        inputs.setDriveMod(driveMod);
    }

    public void connectDriveMod() {
        inputs.connectDriveMod();
    }

    public void disconnectDriveMod() {
        inputs.disconnectDriveMod();
    }

    public void tick() {
        if (isProcessingAudio) {
            inputs.readInputs(model);
        } else {
            interpolationController.applyInterpolation(model);
        }

        if (intermediateCount == 0) {
            view.plotIntermediate();
            intermediateCount = intermediateInterval;
        } else {
            intermediateCount--;
        }

        if (inputs.isDriftTriggered()) {
            setNewTargets();
        }

        outputs.writeOutputs(model);
    }

    public void setNewTargets() {
        interpolationController.setNewTargets(model);
    }

}
