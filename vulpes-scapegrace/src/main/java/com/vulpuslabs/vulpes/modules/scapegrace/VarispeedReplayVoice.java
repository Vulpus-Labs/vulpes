package com.vulpuslabs.vulpes.modules.scapegrace;

import com.vulpuslabs.vulpes.buffers.api.SampleCount;

import static com.vulpuslabs.vulpes.buffers.api.Stereo.LEFT;
import static com.vulpuslabs.vulpes.buffers.api.Stereo.RIGHT;

public class VarispeedReplayVoice implements ReplayVoice {

    private final int sampleLength;
    private final TwoSampleWindow window;
    private final double delta;
    private double pointer;
    private int replayCount;

    public VarispeedReplayVoice(ReplayVoice voice, int sampleLength) {
        this.window = new TwoSampleWindow(voice);
        this.sampleLength = sampleLength;
        this.pointer = 0.0;
        this.delta = (double) voice.getSampleLength() / sampleLength;
    }

    @Override
    public int getSampleLength() {
        return sampleLength;
    }

    @Override
    public boolean isFinished() {
        return replayCount == 0;
    }

    @Override
    public void accept(SampleData sampleData) {
        window.read(pointer, sampleData);
        replayCount--;
        pointer += delta;
    }

    private static class TwoSampleWindow {

        private final ReplayVoice voice;
        private SampleData first;
        private SampleData second;
        private final SampleData diff;
        private int samplePointer;

        private TwoSampleWindow(ReplayVoice voice) {
            this.voice = voice;
            first = new SampleData(SampleCount.STEREO);
            second = new SampleData(SampleCount.STEREO);
            diff = new SampleData(SampleCount.STEREO);
            voice.accept(first);
            voice.accept(second);
            diff.setSample(LEFT, second.getSample(LEFT) - first.getSample(LEFT));
            diff.setSample(RIGHT, second.getSample(RIGHT) - first.getSample(RIGHT));
            samplePointer = 0;
        }

        private void advance() {
            // Swap pointers
            SampleData exchange = second;
            second = first;
            first = exchange;

            // Read into the second sample
            voice.accept(second);

            // Calculate diffs (avoids recalculation when interpolating)
            diff.setSample(LEFT, second.getSample(LEFT) - first.getSample(LEFT));
            diff.setSample(RIGHT, second.getSample(RIGHT) - first.getSample(RIGHT));

            samplePointer++;
        }

        public void read(double interpolatedPointer, SampleData target) {
            var targetPointer = (int) interpolatedPointer;
            var fraction = interpolatedPointer - targetPointer;
            while (samplePointer < targetPointer) {
                advance();
            }
            target.setSample(LEFT, first.getSample(LEFT) + (fraction * diff.getSample(LEFT)));
            target.setSample(RIGHT, first.getSample(RIGHT) + (fraction * diff.getSample(RIGHT)));
        }
    }
}
