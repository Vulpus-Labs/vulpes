package com.vulpuslabs.vulpes.modules.catkins;

import com.vulpuslabs.vulpes.buffers.SampleData;
import com.vulpuslabs.vulpes.buffers.api.BufferRandomAccess;
import com.vulpuslabs.vulpes.buffers.api.SampleCount;
import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.inputs.DisconnectableInput;
import com.vulpuslabs.vulpes.values.outputs.DisconnectableOutput;
import com.vulpuslabs.vulpes.values.ranges.Range;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

import static com.vulpuslabs.vulpes.values.api.stereo.Stereo.LEFT;
import static com.vulpuslabs.vulpes.values.api.stereo.Stereo.RIGHT;

public class StereoReadHead {

    private final DoubleSupplier offset;
    private final DoubleSupplier offsetMod;
    private final DisconnectableOutput sendLeft;
    private final DisconnectableOutput sendRight;
    private final DisconnectableInput receiveLeft;
    private final DisconnectableInput receiveRight;
    private DoubleSupplier modulatedOffset;
    private Consumer<BufferRandomAccess> sendAction;
    private boolean isModulating;
    private DoubleTransformer posRange;
    private DoubleTransformer modRange;
    private boolean isLeftConnected;
    private boolean isRightConnected;
    private final SampleData stereoData = new SampleData(SampleCount.STEREO);
    private final SampleData[] readBuffer = new SampleData[4];

    public StereoReadHead(
            DoubleSupplier offset,
            DisconnectableInput offsetMod,
            DisconnectableOutput sendLeft,
            DisconnectableOutput sendRight,
            DisconnectableInput receiveLeft,
            DisconnectableInput receiveRight) {
        this.offset = offset;
        this.offsetMod = offsetMod;
        setRange(10);
        this.sendLeft = sendLeft;
        this.sendRight = sendRight;
        this.receiveLeft = receiveLeft;
        this.receiveRight = receiveRight;
        this.sendAction = this::sendDisconnected;

        sendLeft.onConnectionStatusChanged(this::sendLeftConnectionStatusChanged);
        sendRight.onConnectionStatusChanged(this::sendRightConnectionStatusChanged);
        offsetMod.onConnectionStatusChanged(this::offsetModConnectionStatusChanged);

        for (int i=0; i<4; i++) {
            readBuffer[i] = new SampleData(SampleCount.STEREO);
        }
        setRange(10);
    }

    public void processSample(BufferRandomAccess buffer, SampleData target) {
        sendAction.accept(buffer);
        target.setSample(LEFT, target.getSample(LEFT) + receiveLeft.getAsDouble());
        target.setSample(RIGHT, target.getSample(RIGHT) + receiveRight.getAsDouble());
    }

    private void sendConnected(BufferRandomAccess buffer) {
        buffer.getSampleAtOffset(modulatedOffset.getAsDouble(), readBuffer, stereoData);
        sendLeft.accept(stereoData.getSample(LEFT));
        sendRight.accept(stereoData.getSample(RIGHT));
    }

    private void sendDisconnected(BufferRandomAccess buffer) {
        // do nothing;
    }

    private void offsetModConnectionStatusChanged(boolean isConnected) {
        if (isModulating == isConnected) return;

        isModulating = isConnected;
        configureModulatedOffset();
    }


    public void setRange(double rangeMs) {
        posRange = Range.UNIT_UNIPOLAR
                .to(new Range(0, 48.0 * rangeMs));

        modRange = ((DoubleTransformer) Math::abs)
                .andThen(
                    Range.CV_UNIPOLAR.clampTo(
                            new Range(0, 48.0 * rangeMs)));

        configureModulatedOffset();
    }

    private void configureModulatedOffset() {
        modulatedOffset = isModulating
                ? this::getModulatedOffset
                : posRange.transforming(offset);
    }

    private void sendLeftConnectionStatusChanged(boolean isConnected) {
        isLeftConnected = isConnected;
        updateConnections();
    }

    private void sendRightConnectionStatusChanged(boolean isConnected) {
        isRightConnected = isConnected;
        updateConnections();
    }

    private void updateConnections() {
        sendAction = (isLeftConnected || isRightConnected) ? this::sendConnected : this::sendDisconnected;
    }

    private double getModulatedOffset() {
        return posRange.apply(offset.getAsDouble()) +
                modRange.apply(offsetMod.getAsDouble());
    }
}
