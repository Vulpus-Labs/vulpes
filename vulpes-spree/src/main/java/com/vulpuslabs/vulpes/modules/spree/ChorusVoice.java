package com.vulpuslabs.vulpes.modules.spree;

import com.vulpuslabs.vulpes.values.api.IndexedDoubleConsumer;
import com.vulpuslabs.vulpes.values.drift.DriftValue;

public class ChorusVoice {

    private final MonoChorus chorus = new MonoChorus();
    private final int channel;

    private final DriftValue timeRandomisation = new DriftValue(480000, 0.8, 1.2);
    private final DriftValue widthRandomisation = new DriftValue(480000, 0.8, 1.2);
    private final DriftValue speedRandomisation = new DriftValue(480000, 0.8, 1.2);
    private final DriftValue feedbackRandomisation = new DriftValue(480000, 0.8, 1.2);

    public ChorusVoice(int channel, double spread) {
        this.channel = channel;
        setSpreadAmount(spread);
    }

    public void setTriangle() {
        chorus.setTriangle();
    }

    public void setSine() {
        chorus.setSine();
    }

    public void setSpreadAmount(double spread) {
        var delta = spread * 0.2;

        timeRandomisation.setRange(1.0 - delta, 1.0 + delta);
        widthRandomisation.setRange(1.0 - delta, 1.0 + delta);
        speedRandomisation.setRange(1.0 - delta, 1.0 + delta);
        feedbackRandomisation.setRange(1.0 - delta, 1.0);
    }

    public void processSample(InputBus inputBus,
                              IndexedDoubleConsumer outputBus,
                              ParameterSet parameterSet) {
        outputBus.accept(channel, chorus.processSample(
                inputBus.getInput(channel),
                parameterSet.getTime(timeRandomisation),
                parameterSet.getDepth(widthRandomisation),
                parameterSet.getFrequencyHz(speedRandomisation),
                parameterSet.getFeedback(feedbackRandomisation),
                parameterSet.getMix()));
    }

}
