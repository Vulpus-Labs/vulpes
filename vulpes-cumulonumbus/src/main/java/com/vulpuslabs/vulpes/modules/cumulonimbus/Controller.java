package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.stereo.StereoBuffer;
import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;

import java.awt.*;

import static com.vulpuslabs.vulpes.modules.cumulonimbus.Controller.FreezeState.*;

public class Controller {

    enum FreezeState {
        SOLID,
        LIQUID,
        FREEZING,
        DEFROSTING
    }

    private static final double FREEZE_DELTA = 1.0 / 96.0;

    private static final Color SONAR_GREEN = new Color(64, 128, 64, 255);
    private static final Color TRANSPARENT_YELLOW = new Color(255, 255, 0, 48);

    private final InputBus inputBus;

    private final OutputBus outputBus;

    private final GranuleTable cloud;
    private final StereoBuffer buffer;

    private final StereoSample inputSample = new StereoSample();
    private final StereoSample feedbackSample = new StereoSample();
    private final StereoSample outputSample = new StereoSample();

    private final StereoFeedbackCircuit feedbackCircuit = new StereoFeedbackCircuit();

    private FreezeState freezeState = FreezeState.LIQUID;
    private double freezeFade = 1.0;
    private double freezeCount = 10;

    public Controller(InputBus inputBus, OutputBus outputBus, int maxSize) {
        this.inputBus = inputBus;
        this.outputBus = outputBus;
        this.cloud = new GranuleTable(maxSize, this::newGranule);
        this.buffer = new StereoBuffer(BufferSize.BUFFER_64k);
    }

    public void processSample() {
        inputBus.readInputs(inputSample);
        inputSample.multiply(inputBus.getLevel());
        inputSample.saturate();

        feedbackCircuit.getAsStereo(feedbackSample);
        feedbackSample.multiply(inputBus.getFeedback());
        inputSample.add(feedbackSample);

        FreezeState newFreezeState = updateFreezeState(inputBus.isFrozen());

        cloud.readSample(buffer, outputSample, freezeState == SOLID, inputBus.isTriggering());
        outputSample.saturate();

        feedbackCircuit.accept(outputSample);
        inputSample.mix(outputSample, inputBus.getMix());

        inputSample.writeTo(outputBus);

        freezeState = newFreezeState;
    }

    private FreezeState updateFreezeState(boolean freezeGate) {
        return switch (freezeState) {
            case SOLID -> {
                if (freezeGate) yield SOLID;
                freezeFade = 0.0;
                freezeCount = 96;
                yield DEFROSTING;
            }
            case LIQUID -> {
                buffer.write(inputSample);
                if (!freezeGate) yield LIQUID;
                freezeFade = 1.0;
                freezeCount = 96;
                yield FREEZING;
            }
            case DEFROSTING -> {
                buffer.write(inputSample.getLeft() * freezeFade,
                        inputSample.getRight() * freezeFade);

                freezeFade += FREEZE_DELTA;
                freezeCount--;
                yield freezeCount == 0 ? LIQUID : DEFROSTING;
            }
            case FREEZING -> {
                buffer.write(inputSample.getLeft() * freezeFade,
                        inputSample.getRight() * freezeFade);

                freezeFade -= FREEZE_DELTA;
                freezeCount--;
                yield freezeCount == 0 ? SOLID : FREEZING;
            }
        };
    }

    private static final double ONE_OVER_TWELVE = 1.0 / 12.0;

    private void newGranule(Granule granule) {
        int lengthSamples = (int) (inputBus.getLengthMs() * 48.0);
        double positionOffset = inputBus.getPositionMs() * 48.0;
        double pitchCents = inputBus.getPitchCents();
        double fadePercent = inputBus.getFadePercent();
        double speedRatio = Math.pow(2.0, pitchCents * ONE_OVER_TWELVE);
        double delta = lengthSamples * (speedRatio - 1.0);
        double shiftStart = delta;
        double shiftEnd = 0.0;
        if (delta < 0.0) {
            shiftStart = 0.0;
            shiftEnd = -delta;
        }

        granule.initialise(positionOffset + shiftStart,
                positionOffset + shiftEnd,
                lengthSamples,
                inputBus.getPan(),
                fadePercent);
    }

    public boolean isTriggering() {
        return inputBus.isTriggering();
    }

    public boolean isFrozen() {
        return inputBus.isFrozen();
    }

    public double granuleDensity() {
        return cloud.getActiveCount() / (double) cloud.getMaxCount();
    }

    public void drawBuffer(Graphics2D graphics, int width, int height) {
        graphics.clearRect(0, 0, width, height);
        StereoSample sample = new StereoSample();
        var delta = -(96000.0 / width);
        var pos = 48000.0;
        int mid = height / 2;
        graphics.setColor(SONAR_GREEN);

        for (int x = 0; x < width; x += 2) {
            buffer.read((int) pos, sample);
            pos += delta;
            graphics.drawLine(
                    x,
                    mid + (int) ((Math.abs(sample.getLeft())) * 0.2 * mid),
                    x,
                    mid - (int) ((Math.abs(sample.getRight())) * 0.2 * mid));
        }

        graphics.setColor(TRANSPARENT_YELLOW);

        var scaling = width / (48000.0);

        cloud.getPositions((grainPos, fade) -> {
            var x = (int) (width - (grainPos * scaling));
            
            if (x > 0) {
                var amt = (int) (fade * mid);
                graphics.drawLine(
                        x,
                        mid - amt,
                        x,
                        mid + amt);
            }
        });

        graphics.dispose();
    }
}
