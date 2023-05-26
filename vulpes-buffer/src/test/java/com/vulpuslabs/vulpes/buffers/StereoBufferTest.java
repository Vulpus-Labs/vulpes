package com.vulpuslabs.vulpes.buffers;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.stereo.StereoBuffer;
import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class StereoBufferTest {

    private double expected(double x) {
        double sum = 0.0;
        Random random = new Random();
        random.setSeed(1234);
        for (int i=0; i < 100; i++) {
            sum += Math.cos(x * random.nextDouble());
        }
        return sum * 0.01;
    }

    @Test
    public void testStereoBuffer() {
        StereoBuffer unit = new StereoBuffer(BufferSize.BUFFER_1k);
        StereoSample sample = new StereoSample();
        for (int i=0; i < 1000; i++) {
            sample.set(expected(i * 0.01), 0.0);
            unit.write(sample);
        }

        double totFracDiff = 0.0;
        double totHermitDiff = 0.0;
        int count = 0;
        for (int  i=0; i < 9991 ; i++) {
            unit.readFractional(i * 0.1, sample);
            double fracLeft = sample.getLeft();
            unit.readFractionalHermite(i * 0.1, sample);
            double hermitLeft = sample.getLeft();
            double expected = expected(10.0 - i * 0.001);
            totFracDiff += Math.abs(fracLeft - expected);
            totHermitDiff += Math.abs(hermitLeft - expected);
            count++;
        }

        double avgFracDiff = totFracDiff / count;
        double avgHermitDiff = totHermitDiff / count;
        System.out.println("Avg frac diff: " + avgFracDiff);
        System.out.println("Avg hermit diff: " + avgHermitDiff);

        double totFracDev = 0.0;
        double totHermitDev = 0.0;
        for (int  i=0; i < 9991 ; i++) {
            unit.readFractional(i * 0.1, sample);
            double fracLeft = sample.getLeft();
            unit.readFractionalHermite(i * 0.1, sample);
            double hermitLeft = sample.getLeft();
            double expected = expected(10.0 - i * 0.001);
            double fracDev = Math.abs(fracLeft - expected) - avgFracDiff;
            double hermitDev = Math.abs(hermitLeft - expected) - avgHermitDiff;
            totFracDev += fracDev * fracDev;
            totHermitDev += hermitDev * hermitDev;
            count++;
        }

        double fracStdDev = Math.sqrt(totFracDev / count);
        double hermitStdDev = Math.sqrt(totHermitDev / count);

        System.out.println("Frac stddev: " + fracStdDev);
        System.out.println("Hermit stddev: " + hermitStdDev);
    }
}
