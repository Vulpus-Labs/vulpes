package com.vulpuslabs.vulpes.modules.freqs;

import java.awt.*;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class Controller {

    private static final int BUFFER_SIZE = 1024;
    private static final int BUFFER_MASK = BUFFER_SIZE - 1;
    private static final int WINDOW_SIZE = BUFFER_SIZE >> 1;
    private static final int OFFSET_MASK = (WINDOW_SIZE >> 1) - 1;

    private final DoubleSupplier leftInput;
    private final DoubleSupplier rightInput;
    private final DoubleConsumer output;
    private final FFTProcessor fftProcessor;
    private final double[] leftBuffer = new double[BUFFER_SIZE];
    private final double[] rightBuffer = new double[BUFFER_SIZE];
    private final double[] outBuffer = new double[BUFFER_SIZE];
    private int readPtr = 0;

    public Controller(DoubleSupplier leftInput, DoubleSupplier rightInput, DoubleConsumer output) {
        this.leftInput = leftInput;
        this.rightInput = rightInput;
        this.output = output;
        this.fftProcessor = new FFTProcessor(WINDOW_SIZE, leftBuffer, rightBuffer, outBuffer);
    }

    public void startFFTThread() {
        Thread fftThread = new Thread(fftProcessor);
        fftThread.start();
    }

    public void stopFFTThread() {
        fftProcessor.stop();
    }
    public void processSample() {
        leftBuffer[readPtr] = leftInput.getAsDouble();
        rightBuffer[readPtr] = rightInput.getAsDouble();
        output.accept(outBuffer[readPtr]);
        readPtr = (readPtr + 1) & BUFFER_MASK;

        if ((readPtr & OFFSET_MASK) == 0) {
            int windowStart = (readPtr + WINDOW_SIZE) & BUFFER_MASK;
            fftProcessor.process(windowStart);
        }
    }

    public void drawWindow(Graphics2D g, int width, int height) {
        double[] window = fftProcessor.getLastWindow();

        g.clearRect(0, 0, width, height);

        double delta = window.length / (double) width;
        double pos = 0.0;
        for (int i=0; i < width; i++) {
            double sample = window[(int) pos];
            pos += delta;
            g.drawLine(i, height / 2, i, (int) ((height / 2) + (sample * 0.4 * height)));
        }
    }
}
