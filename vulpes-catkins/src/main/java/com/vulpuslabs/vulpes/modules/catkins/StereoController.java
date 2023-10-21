package com.vulpuslabs.vulpes.modules.catkins;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import com.vulpuslabs.vulpes.buffers.stereo.StereoBuffer;
import com.vulpuslabs.vulpes.buffers.stereo.StereoBufferFractionalReader;
import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;
import com.vulpuslabs.vulpes.values.events.TwoPositionSwitchState;
import com.vulpuslabs.vulpes.values.inputs.DisconnectableInput;

import java.text.NumberFormat;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

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

    private final StereoBuffer buffer;

    private StereoBufferFractionalReader bufferReader;

    private final StereoFeedbackCircuit feedbackCircuit;

    private final StereoSample inputData = new StereoSample();
    private final StereoSample feedbackData = new StereoSample();
    private final StereoSample outputData = new StereoSample();

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
        this.buffer = new StereoBuffer(BufferSize.BUFFER_1m);
        this.bufferReader = this.buffer::readFractional;
        this.readHeads = readHeads;
    }

    public void setInterpolationQuality(TwoPositionSwitchState switchState) {
        bufferReader = switchState == TwoPositionSwitchState.OFF
                ? buffer::readFractionalHermite
                : buffer::readFractional;
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
        double leftInput = inputLeft.getAsDouble();
        inputData.set(leftInput,
            inputRight.isConnected() ? inputRight.getAsDouble() : leftInput);

        feedbackCircuit.getAsStereo(feedbackData);
        feedbackData.add(inputData);
        buffer.write(feedbackData);

        outputData.set(0.0, 0.0);

        for (StereoReadHead readHead : readHeads) {
            readHead.processSample(bufferReader, outputData);
        }

        feedbackCircuit.accept(outputData);
        inputData.mix(outputData, mixAmount.getAsDouble());
        inputData.writeTo(outputLeft, outputRight);
    }
}
