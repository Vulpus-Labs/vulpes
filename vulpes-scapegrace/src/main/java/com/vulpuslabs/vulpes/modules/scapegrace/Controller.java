package com.vulpuslabs.vulpes.modules.scapegrace;

import com.vulpuslabs.vulpes.buffers.Buffer;
import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.api.SampleCount;

import java.util.function.DoubleSupplier;

public class Controller {

    private final InputBus inputBus;
    private final Buffer buffer = new Buffer(BufferSize.BUFFER_1m, SampleCount.STEREO);
    private final ReplayVoiceSet replayVoices = new ReplayVoiceSet();

    public Controller(InputBus inputBus) {
        this.inputBus = inputBus;
    }

    public void addVoice(DoubleSupplier randomSource) {
        /*
        var reverse = randomSource.getAsDouble() <= inputBus.getReverseProbability();
        var doubleSpeed = randomSource.getAsDouble() <= inputBus.getDoubleSpeedProbability();
        var delayLength = inputBus.getDelayLength();
        var stereoWidth = inputBus.getStereoWidth();
*/


    }
}
