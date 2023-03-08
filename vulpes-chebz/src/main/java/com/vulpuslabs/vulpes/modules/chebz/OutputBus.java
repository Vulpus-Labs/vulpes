package com.vulpuslabs.vulpes.modules.chebz;

import com.vulpuslabs.vulpes.values.api.DoubleTransformer;
import com.vulpuslabs.vulpes.values.outputs.DisconnectableOutput;
import com.vulpuslabs.vulpes.values.ranges.Range;

import java.util.function.DoubleConsumer;

public class OutputBus {

    private static final DoubleTransformer SCALE_UP =
            Range.UNIT_BIPOLAR.to(Range.CV_BIPOLAR);

    private final DoubleConsumer[] individualOuts;
    private final DoubleConsumer evensOut;
    private final DoubleConsumer oddsOut;
    private final DoubleConsumer bothOut;

    public OutputBus(DisconnectableOutput[] individualOuts,
                     DisconnectableOutput evensOut,
                     DisconnectableOutput oddsOut,
                     DisconnectableOutput bothOut) {
        for (int i=0; i<10; i++) {
            individualOuts[i] = individualOuts[i].transform(SCALE_UP);
        }
        this.individualOuts = individualOuts;
        this.evensOut = evensOut.transform(SCALE_UP);
        this.oddsOut = oddsOut.transform(SCALE_UP);
        this.bothOut = bothOut.transform(SCALE_UP);
    }

    public void sendIndividualOuts(double[] samples) {
        for (int i=0; i<10; i++) {
            individualOuts[i].accept(samples[i]);
        }
    }

    public void sendEvens(double evens) {
        evensOut.accept(evens);
    }

    public void sendOdds(double odds) {
        oddsOut.accept(odds);
    }

    public void sendBoth(double both) {
        bothOut.accept(both);
    }
}
