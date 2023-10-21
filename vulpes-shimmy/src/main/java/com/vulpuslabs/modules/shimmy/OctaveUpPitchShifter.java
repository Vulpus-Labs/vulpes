package com.vulpuslabs.modules.shimmy;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.stereo.StereoBuffer;
import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;
import com.vulpuslabs.vulpes.values.Approximate;

public class OctaveUpPitchShifter {

    private class Tap {
        private final int lengthSamples;
        private final double hanningDelta;
        private int offset;
        private double hanningPtr;

        public Tap(int lengthSamples) {
            this.lengthSamples = lengthSamples;
            this.hanningDelta = 1.0 / lengthSamples;
            this.offset = lengthSamples;
            this.hanningPtr = 0;
        }

        private static final double CONSTANT_SUM = 1.435;
        private static final double BH_FIRST_TERM = 0.35875 / CONSTANT_SUM;
        private static final double BH_SECOND_TERM = 0.48829 / CONSTANT_SUM;
        private static final double BH_THIRD_TERM = 0.14128 / CONSTANT_SUM;
        private static final double BH_FOURTH_TERM = 0.01168 / CONSTANT_SUM;


        public void read(StereoBuffer buffer, StereoSample target) {
            buffer.read(offset, target);
            double doubleHanningPtr = hanningPtr + hanningPtr;

            var blackman = (BH_FIRST_TERM
                    - BH_SECOND_TERM * Approximate.cosUnit(hanningPtr)
                    + BH_THIRD_TERM * Approximate.cosUnit(doubleHanningPtr)
                    - BH_FOURTH_TERM * Approximate.cosUnit(doubleHanningPtr + hanningPtr));

            target.multiply(blackman);

            offset -= 1;
            hanningPtr += hanningDelta;

            if (offset == 0) {
                offset = lengthSamples;
                hanningPtr = 0;
            }
        }

        public void advance(int samples) {
            offset -= samples;
            hanningPtr += samples * hanningDelta;
        }
    }

    private final Tap[] taps;
    private final StereoBuffer buffer;
    private final StereoSample tapRead = new StereoSample();

    public OctaveUpPitchShifter() {
        this.taps = new Tap[4];
        for (int i=0; i<4; i++) {
            taps[i] = new Tap(8192);
            taps[i].advance(i * 2048);
        }

        this.buffer = new StereoBuffer(BufferSize.BUFFER_8k);
    }

    public void apply(StereoSample in, StereoSample out) {
        buffer.write(in);
        taps[0].read(buffer, out);
        for (int i=1; i<4; i++) {
            taps[i].read(buffer, tapRead);
            out.add(tapRead);
        }
    }
}
