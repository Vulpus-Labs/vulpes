package com.vulpuslabs.vulpes.modules.swirl;

import com.vulpuslabs.vulpes.values.smoothed.DCBlocker;

import java.util.function.DoubleConsumer;
import java.util.function.IntFunction;

public class SwirlOutputs {

    private final double[] xValues;
    private final double[] yValues;
    private final DoubleConsumer[] rawXOutputs;
    private final DoubleConsumer[] rawYOutputs;
    private final DoubleConsumer[] postProcessedXOutputs;
    private final DoubleConsumer[] postProcessedYOutputs;
    private final int maxSize;
    private int activeSize;
    private boolean blockingDc;

    public SwirlOutputs(int maxSize,
                        int activeSize,
                        IntFunction<DoubleConsumer> getXOutput,
                        IntFunction<DoubleConsumer> getYOutput) {
        this.maxSize = maxSize;
        this.activeSize = activeSize;

        this.xValues = new double[maxSize];
        this.yValues = new double[maxSize];

        this.rawXOutputs = new DoubleConsumer[maxSize];
        this.rawYOutputs = new DoubleConsumer[maxSize];
        for (int i=0; i<maxSize; i++) {
            rawXOutputs[i] = getXOutput.apply(i);
            rawYOutputs[i] = getYOutput.apply(i);
        }

        this.postProcessedXOutputs = new DoubleConsumer[maxSize];
        this.postProcessedYOutputs = new DoubleConsumer[maxSize];
        setPostProcessedOutputs();
    }

    private void setPostProcessedOutputs() {
        for (int i=0; i<maxSize; i++) {
            postProcessedXOutputs[i] = blockingDc
                    ? new DCBlocker().transforming(rawXOutputs[i])
                    : rawXOutputs[i];
            postProcessedYOutputs[i] = blockingDc
                    ? new DCBlocker().transforming(rawYOutputs[i])
                    : rawYOutputs[i];
        }
    }

    public void setBlockingDc(boolean blockingDc) {
        if (this.blockingDc != blockingDc) {
            this.blockingDc = blockingDc;
            setPostProcessedOutputs();
        }
    }

    public void setActiveSize(int activeSize) {
        this.activeSize = activeSize;
    }

    public void writeOutputs(SwirlModel model) {
        model.getCartesian(activeSize, postProcessedXOutputs, postProcessedYOutputs);
    }
}
