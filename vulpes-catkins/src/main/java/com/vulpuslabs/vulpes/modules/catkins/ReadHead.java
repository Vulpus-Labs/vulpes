package com.vulpuslabs.vulpes.modules.catkins;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.inputs.DisconnectableInput;
import com.vulpuslabs.vulpes.values.outputs.DisconnectableOutput;
import com.vulpuslabs.vulpes.values.ranges.Range;

import java.util.function.DoubleSupplier;

public class ReadHead {

    private final DoubleSupplier offset;
    private final DoubleSupplier offsetMod;
    private final DisconnectableOutput send;
    private final DisconnectableInput receive;
    private DoubleSupplier modulatedOffset;

    private boolean isSending;
    private boolean isModulating;
    private DoubleTransformer posRange;
    private DoubleTransformer modRange;

    public ReadHead(
            DoubleSupplier offset,
            DisconnectableInput offsetMod,
            DisconnectableOutput send,
            DisconnectableInput receive) {
        this.offset = offset;
        this.offsetMod = offsetMod;
        setRange(10);
        this.send = send;
        this.receive = receive;

        send.onConnectionStatusChanged(this::sendConnectionStatusChanged);
        offsetMod.onConnectionStatusChanged(this::offsetModConnectionStatusChanged);
    }

    public double processSample(DoubleTransformer bufferReader) {
        if (isSending) {
            send.accept(bufferReader.apply(modulatedOffset.getAsDouble()));
        }
        return receive.getAsDouble();
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

    private void sendConnectionStatusChanged(boolean isConnected) {
        isSending = isConnected;
    }

    private double getModulatedOffset() {
        return posRange.apply(offset.getAsDouble()) +
                modRange.apply(offsetMod.getAsDouble());
    }
}
