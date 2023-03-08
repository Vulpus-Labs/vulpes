package com.vulpuslabs.vulpes.modules.spree;

import com.vulpuslabs.vulpes.values.api.IndexedDoubleConsumer;

public class Controller {

    private final InputBus inputBus;
    private final IndexedDoubleConsumer outputBus;
    private final ParameterSet parameters = new ParameterSet();
    private ChorusVoice[] voices;
    private int activeVoices;
    private double spreadAmount;
    private boolean isTriangle = false;

    public Controller(InputBus inputBus,
                      IndexedDoubleConsumer outputBus,
                      int activeVoices,
                      double spreadAmount,
                      boolean isTriangle) {
        this.inputBus = inputBus;
        this.outputBus = outputBus;
        voices = new ChorusVoice[activeVoices];
        this.activeVoices = activeVoices;
        this.spreadAmount = spreadAmount;
        this.isTriangle = isTriangle;

        for (int i=0; i < activeVoices; i++) {
            voices[i] = new ChorusVoice(i, spreadAmount);
            if (isTriangle) {
                voices[i].setTriangle();
            }
        }
    }

    public void setActiveVoices(int activeVoices) {
        if (activeVoices == this.activeVoices) {
            return;
        }
        ChorusVoice[] newVoices = new ChorusVoice[activeVoices];
        System.arraycopy(voices, 0, newVoices, 0, activeVoices);
        if (activeVoices > this.activeVoices) {
            for (int i = this.activeVoices; i < activeVoices; i++) {
                voices[i] = new ChorusVoice(i, spreadAmount);
                if (isTriangle) {
                    voices[i].setTriangle();
                }
            }
        }
        this.activeVoices = activeVoices;
        this.voices = newVoices;
    }

    public void setTriangle() {
        isTriangle = true;
        for (int i = 0; i < activeVoices; i++) {
            voices[i].setTriangle();
        }
    }

    public void setSine() {
        isTriangle = false;
        for (int i = 0; i < activeVoices; i++) {
            voices[i].setSine();
        }
    }

    public void setSpreadAmount(double spreadAmount) {
        this.spreadAmount = spreadAmount;

        for (ChorusVoice voice : voices) {
            voice.setSpreadAmount(spreadAmount);
        }
    }

    public void processSamples() {
        inputBus.setParameters(parameters);

        for (ChorusVoice voice : voices) {
            voice.processSample(inputBus, outputBus, parameters);
        }
    }

}
