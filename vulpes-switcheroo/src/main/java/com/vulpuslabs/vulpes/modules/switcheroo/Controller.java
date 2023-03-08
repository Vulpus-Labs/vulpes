package com.vulpuslabs.vulpes.modules.switcheroo;

import com.vulpuslabs.vulpes.values.inputs.CvMultiplyableKnob;
import com.vulpuslabs.vulpes.values.inputs.TriggerInput;

import java.awt.*;
import java.util.function.DoubleSupplier;

public class Controller {

    private final Channel[] channels;
    private final CvMultiplyableKnob crossfadeAmount;

    private final GridModel gridModel;
    private final GridView gridView;
    private final CrossFader crossFader;

    private final TriggerInput clockTrigger;
    private final TriggerInput resetTrigger;

    public Controller(Channel[] channels,
                      DoubleSupplier crossfadeCv,
                      double initialCrossfadeValue,
                      DoubleSupplier clockTriggerInput,
                      DoubleSupplier resetTriggerInput,
                      int viewWidth,
                      int viewHeight) {
        this.channels = channels;

        this.gridModel = new GridModel();
        this.crossfadeAmount = new CvMultiplyableKnob(
                crossfadeCv,
                2.0,
                initialCrossfadeValue,
                0.1);

        this.clockTrigger = new TriggerInput(clockTriggerInput);
        this.resetTrigger = new TriggerInput(resetTriggerInput);

        this.gridView = new GridView(gridModel, viewWidth, viewHeight);
        this.crossFader = new CrossFader(16, 0, channels);
    }

    public void processSample(OutputBus outputBus) {
        outputBus.reset();

        var currentSelected = gridModel.getCurrentSelectedChannel();
        var newSelected = currentSelected;

        if (clockTrigger.hasTriggered()) {
            gridModel.advanceStep();
            newSelected = gridModel.getCurrentSelectedChannel();
            outputBus.onClock();
        }

        if (resetTrigger.hasTriggered()) {
            gridModel.resetStep();
            newSelected = gridModel.getCurrentSelectedChannel();
            outputBus.onReset();
        }

        if (newSelected != currentSelected) {
            crossFader.startCrossFade(newSelected, (int) crossfadeAmount.getUnsmoothed() * 48);
            outputBus.onCrossfadeTriggered(newSelected);
        }

        outputBus.setOutputSample(crossFader.processSample());
    }

    public void onClick(int mouseX, int mouseY) {
        int step = gridView.getStep(mouseX);
        int channel = gridView.getChannel(mouseY);

        gridModel.setSelectedChannel(step, channel);
    }

    public int incrementStepCount() {
        return gridModel.incrementStepCount();
    }

    public int decrementStepCount() {
        return gridModel.decrementStepCount();
    }

    public int getStepCount() {
        return gridModel.getStepCount();
    }

    public void setCrossfadeCvIsConnected(boolean cvIsConnected) {
        crossfadeAmount.setCvIsConnected(cvIsConnected);
    }

    public void setCrossfadeKnobValue(double knobValue) {
        crossfadeAmount.setKnobValue(knobValue);
    }

    public void setInputIsConnected(int channel, boolean inputIsConnected) {
        channels[channel].setInputIsConnected(inputIsConnected);
    }

    public void setClockIsConnected(boolean isConnected) {
        clockTrigger.setIsConnected(isConnected);
    }

    public void setResetIsConnected(boolean isConnected) {
        resetTrigger.setIsConnected(isConnected);
    }

    public void setVolumeKnobValue(int channel, double knobValue) {
        channels[channel].setVolumeKnobValue(knobValue);
    }

    public void setVolumeCVIsConnected(int channel, boolean cvIsConnected) {
        channels[channel].setVolumeCVIsConnected(cvIsConnected);
    }

    public void draw(Graphics2D graphics) {
        gridView.draw(graphics);
    }

    public void reset() {
        gridModel.reset();
    }

    public void randomise(DoubleSupplier randomSource) {
        gridModel.randomise(randomSource);
    }

    public byte[] getSaveState() {
        return gridModel.getSaveState();
    }

    public void setSaveState(byte[] saveState) {
        gridModel.setSaveState(saveState);
    }
}
