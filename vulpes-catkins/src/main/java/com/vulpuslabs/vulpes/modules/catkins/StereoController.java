package com.vulpuslabs.vulpes.modules.catkins;

import com.vulpuslabs.vulpes.buffers.Buffer;
import com.vulpuslabs.vulpes.buffers.FancyBufferRandomAccess;
import com.vulpuslabs.vulpes.buffers.LinearInterpolatingBufferRandomAccess;
import com.vulpuslabs.vulpes.buffers.SampleData;
import com.vulpuslabs.vulpes.buffers.api.BufferRandomAccess;
import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.api.SampleCount;
import com.vulpuslabs.vulpes.buffers.api.Stereo;
import com.vulpuslabs.vulpes.values.events.TwoPositionSwitchState;
import com.vulpuslabs.vulpes.values.inputs.DisconnectableInput;

import java.text.NumberFormat;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import static com.vulpuslabs.vulpes.values.api.stereo.Stereo.LEFT;
import static com.vulpuslabs.vulpes.values.api.stereo.Stereo.RIGHT;

public class StereoController {

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

    private final DoubleSupplier inputLeft;
    private final DisconnectableInput inputRight;
    private final DoubleSupplier mixAmount;
    private final DoubleConsumer outputLeft;
    private final DoubleConsumer outputRight;
    private final StereoReadHead[] readHeads;

    private final Buffer buffer;
    private BufferRandomAccess bufferRandomAccess;

    private final StereoFeedbackCircuit feedbackCircuit;
    private final double[] stereoData = new double[2];

    private final SampleData inputData = new SampleData(SampleCount.STEREO);
    private final SampleData feedbackData = new SampleData(SampleCount.STEREO);
    private final SampleData outputData = new SampleData(SampleCount.STEREO);

    public StereoController(DoubleSupplier inputLeft,
                            DisconnectableInput inputRight,
                            DoubleSupplier feedbackAmount,
                            DoubleSupplier mixAmount,
                            DoubleConsumer outputLeft,
                            DoubleConsumer outputRight,
                            StereoReadHead[] readHeads) {
        this.inputLeft = inputLeft;
        this.inputRight = inputRight;
        this.feedbackCircuit = new StereoFeedbackCircuit(feedbackAmount);
        this.mixAmount = mixAmount;
        this.outputLeft = outputLeft;
        this.outputRight = outputRight;
        this.buffer = new Buffer(BufferSize.BUFFER_1m, SampleCount.STEREO);
        bufferRandomAccess = new LinearInterpolatingBufferRandomAccess(buffer);
        this.readHeads = readHeads;
    }

    public void setInterpolationQuality(TwoPositionSwitchState switchState) {
        bufferRandomAccess = switchState == TwoPositionSwitchState.OFF
                ? new FancyBufferRandomAccess(buffer)
                : new LinearInterpolatingBufferRandomAccess(buffer);
    }

    public void setRange(double switchValue) {
        double rangeMs = RANGES[(int) switchValue];
        for (StereoReadHead readHead : readHeads) {
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
        inputData.setSample(LEFT, inputLeft.getAsDouble());
        inputData.setSample(RIGHT, inputRight.isConnected() ?
                inputRight.getAsDouble()
                : inputData.getSample(LEFT));

        feedbackCircuit.getAsStereo(feedbackData);
        inputData.add(feedbackData);
        buffer.writeNext(inputData);

        outputData.setSample(LEFT, 0.0);
        outputData.setSample(RIGHT, 0.0);

        for (StereoReadHead readHead : readHeads) {
            readHead.processSample(bufferRandomAccess, outputData);
        }

        feedbackCircuit.accept(outputData);

        double wetAmount = mixAmount.getAsDouble();
        double dryAmount = 1.0 - wetAmount;

        outputLeft.accept(inputData.getSample(LEFT) * dryAmount
                + outputData.getSample(LEFT) * wetAmount);
        outputRight.accept(inputData.getSample(RIGHT) * dryAmount
                + outputData.getSample(RIGHT) * wetAmount);

    }
}
