package com.vulpuslabs.vulpes.modules.freqs;

import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.complex.Complex;
import com.vulpuslabs.vulpes.values.complex.FFT;
import com.vulpuslabs.vulpes.values.complex.Polar;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FFTProcessor implements Runnable {

    private final BlockingQueue<Integer> workQueue = new LinkedBlockingQueue<>();

    private final int bufferSize;
    private final int windowSize;
    private final int mask;
    private final FFT fft = new FFT();
    private final Complex[] leftWindow;
    private final Complex[] rightWindow;

    private final double[] leftBuffer;
    private final double[] rightBuffer;
    private final double[] writeBuffer;

    private final Complex[][] delayBuffer;
    private int delayPtr = 0;

    private final Polar p1 = new Polar();
    private final Polar p2 = new Polar();

    public FFTProcessor(int windowSize, double[] leftBuffer, double[] rightBuffer, double[] writeBuffer) {
        this.leftBuffer = leftBuffer;
        this.rightBuffer = rightBuffer;
        this.writeBuffer = writeBuffer;

        this.windowSize = windowSize;
        this.bufferSize = leftBuffer.length;
        this.mask = bufferSize - 1;
        leftWindow = new Complex[windowSize];
        rightWindow = new Complex[windowSize];

        for (int i = 0; i < windowSize; i++) {
            leftWindow[i] = new Complex();
            rightWindow[i] = new Complex();
        }

        delayBuffer = new Complex[256][windowSize >> 1];
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < (windowSize >> 1); j++) {
                delayBuffer[i][j] = new Complex();
            }
        }

        lastWindow = new double[windowSize];
    }

    public void stop() {
        workQueue.add(-1);
    }

    public void queueProcess(int ptr) {
        workQueue.add(ptr);
    }

    @Override
    public void run() {
        while (true) {
            try {
                int ptr = workQueue.take();
                if (ptr == -1) break;
                process(ptr);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int waveOffset;

    public void process(int ptr) {
        populateWindow(leftBuffer, ptr, leftWindow);
        fft.transform(leftWindow);

        int lowptr = 5;
        int hiptr = windowSize - 5;

        Random random = new Random();
        random.setSeed(1024L);

        Complex[] delayAtPtr = delayBuffer[delayPtr];

        waveOffset = (waveOffset + 1) & 127;

        while (lowptr < (windowSize >> 1) - 5) {
            int offset = (int) (delayPtr + 64 + 64 * Approximate.cosUnit(
                    ((waveOffset + lowptr) & 127) / 128.0)) & 255;

            Complex[] delayFrame = delayBuffer[offset];
            Complex delayC = delayFrame[lowptr];
            Complex lowC = leftWindow[lowptr];
            Complex highC = leftWindow[hiptr];

            lowC.add(delayC);
            lowC.scale(0.9);
            lowC.conjugateTo(highC);
            lowC.copyTo(delayAtPtr[lowptr]);

            lowptr++;
            hiptr--;
        }

        delayPtr = (delayPtr + 1) & 255;
        fft.reverseTransform(leftWindow);

        populateBuffer(writeBuffer, ptr, leftWindow);
    }

    private void populateWindow(double[] samples, int ptr, Complex[] window) {
        double hannPtr = 0.0;
        double hannDelta = 1.0 / (double) windowSize;

        for (int i=0; i<windowSize; i++) {
            double hann = 0.1 * (1.0 - Approximate.cosUnit(hannPtr));
            window[i].set(samples[ptr++] * hann, 0.0);
            ptr = ptr & mask;
            hannPtr += hannDelta;
        }
    }

    private void populateBuffer(double[] samples, int ptr, Complex[] window) {
        double[] buf = new double[lastWindow.length];
        int halfWindow = windowSize >> 1;

        double hannPtr = 0.0;
        double hannDelta = 1.0 / (double) windowSize;

        for (int i=0; i<halfWindow; i++) {
            double v = 2.5 * (1.0 - Approximate.cosUnit(hannPtr)) * window[i].getReal();
            buf[i] = v;
            samples[ptr++] += v;

            hannPtr += hannDelta;
            ptr = ptr & mask;
        }
        for (int i=halfWindow; i<windowSize; i++) {
            double v = 2.5 * (1.0 - Approximate.cosUnit(hannPtr)) * window[i].getReal();
            buf[i] = v;
            samples[ptr++] = v;
            ptr = ptr & mask;
            hannPtr += hannDelta;
        }

        lastWindow = buf;
    }

    private double[] lastWindow;

    public double[] getLastWindow() {
        return lastWindow;
    }
}
