package com.vulpuslabs.vulpes.modules.catkins;

import com.vulpuslabs.vulpes.buffers.Buffer;
import com.vulpuslabs.vulpes.buffers.FancyBufferRandomAccess;
import com.vulpuslabs.vulpes.buffers.api.BufferRandomAccess;
import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.LinearInterpolatingBufferRandomAccess;
import com.vulpuslabs.vulpes.buffers.api.SampleCount;

import java.text.NumberFormat;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class Controller {

    private static final double[] RANGES = new double[] {
            10.0,
            100.0,
            500.0,
            1000.0,
            10000.0
    };

    private static final String[] RANGE_NAMES = new String[] {
            "10ms",
            "100ms",
            "500ms",
            "1s",
            "10s"
    };

    private final DoubleSupplier input;
    private final DoubleSupplier mixAmount;
    private final DoubleConsumer output;
    private final ReadHead[] readHeads;

    private final Buffer buffer;
    private BufferRandomAccess bufferRandomAccess;

    private final FeedbackCircuit feedbackCircuit;

    public Controller(DoubleSupplier input,
                      DoubleSupplier feedbackAmount,
                      DoubleSupplier mixAmount,
                      DoubleConsumer output,
                      ReadHead[] readHeads) {
        this.input = input;
        this.feedbackCircuit = new FeedbackCircuit(feedbackAmount);
        this.mixAmount = mixAmount;
        this.output = output;
        this.buffer = new Buffer(BufferSize.BUFFER_1m, SampleCount.MONO);
        bufferRandomAccess = new LinearInterpolatingBufferRandomAccess(buffer);
        this.readHeads = readHeads;
    }

    public void setInterpolationQuality(boolean isHighQuality) {
        bufferRandomAccess = isHighQuality
                ? new FancyBufferRandomAccess(buffer)
                : new LinearInterpolatingBufferRandomAccess(buffer);
    }

    public void setRange(double switchValue) {
        double rangeMs = RANGES[(int) switchValue];
        for (ReadHead readHead : readHeads) {
            readHead.setRange(rangeMs);
        }
    }

    public String getRangeDescription(double switchValue) {
        return RANGE_NAMES[(int) switchValue];
    }

    public String getPosDescription(double posValue, double rangeSwitchValue) {
        double rangeMs = RANGES[(int) rangeSwitchValue];
        double posMs = posValue * rangeMs;
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumIntegerDigits(2);
        return format.format(posMs * 0.001) + "s";
    }

    public void processSample() {
        double inputSample = input.getAsDouble();

        buffer.writeNext(inputSample + feedbackCircuit.getAsDouble());

        double wet = 0.0;
        for (ReadHead readHead : readHeads) {
            wet += readHead.processSample(bufferRandomAccess);
        }

        feedbackCircuit.accept(wet);

        double wetAmount = mixAmount.getAsDouble();
        double dryAmount = 1.0 - wetAmount;

        output.accept(inputSample * dryAmount + wet * wetAmount);
    }
}
