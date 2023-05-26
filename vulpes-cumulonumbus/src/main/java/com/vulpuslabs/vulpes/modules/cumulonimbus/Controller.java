package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.stereo.StereoBuffer;
import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;
import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.random.RandomDouble;

public class Controller {

    private final InputBus inputBus;

    private final OutputBus outputBus;

    private final GranuleTable cloud;
    private final StereoBuffer buffer;

    private final StereoSample inputSample = new StereoSample();
    private final StereoSample outputSample = new StereoSample();

    private final RandomDouble randomDouble = new RandomDouble(0);

    public Controller(InputBus inputBus, OutputBus outputBus, int maxSize) {
        this.inputBus = inputBus;
        this.outputBus = outputBus;
        this.cloud = new GranuleTable(maxSize, this::newGranule);
        this.buffer = new StereoBuffer(BufferSize.BUFFER_64k);
    }

    public void processSample() {
        inputBus.readInputs(inputSample);
        boolean isFrozen = false; // TODO: inputBus.isFrozen();

        if (!isFrozen) {
            buffer.write(inputSample);
        }

        cloud.readSample(buffer, outputSample, isFrozen, inputBus.isTriggering());

        inputSample.mix(outputSample, inputBus.getMix());

        inputSample.writeTo(outputBus);
    }

    private static final double ONE_OVER_TWELVE = 1.0 / 12.0;

    private void newGranule(Granule granule) {
        int lengthSamples = (int) (inputBus.getLengthMs() * 48.0);
        double positionOffset = inputBus.getPositionMs() * 48.0;
        double pitchCents = inputBus.getPitchCents();
        double fadePercent = inputBus.getFadePercent();

        double delta = pitchCents * ONE_OVER_TWELVE * lengthSamples;
        double shiftStart = delta;
        double shiftEnd = 0.0;
        if (delta < 0.0) {
            shiftStart = 0.0;
            shiftEnd = -delta * 0.5;
        }

        granule.initialise(positionOffset + shiftStart,
                positionOffset + shiftEnd,
                lengthSamples,
                fadePercent);
    }
}
