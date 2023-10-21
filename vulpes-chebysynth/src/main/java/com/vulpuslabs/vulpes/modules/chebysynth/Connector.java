package com.vulpuslabs.vulpes.modules.chebysynth;

import com.vulpuslabs.vulpes.values.events.EventBus;
import com.vulpuslabs.vulpes.values.inputs.TriggerInput;
import voltage.core.VoltageAudioJack;
import voltage.core.VoltageComponent;
import voltage.core.VoltageKnob;

public class Connector {

    private final EventBus eventBus;

    public Connector(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public TriggerInput triggerInput(VoltageAudioJack inputJack) {
        TriggerInput input = new TriggerInput(inputJack::GetValue);
        eventBus.registerBooleanObserver(inputJack, input::setIsConnected);
        return input;
    }

    public EnvelopeGeneratorControls envelopeControls(VoltageComponent attackSlider,
                                             VoltageComponent decaySlider,
                                             VoltageComponent sustainSlider,
                                             VoltageComponent releaseSlider) {
        EnvelopeGeneratorControls controls = new EnvelopeGeneratorControls();

        eventBus.registerDoubleObserver(attackSlider, controls::setAttackLength);
        eventBus.registerDoubleObserver(decaySlider, controls::setDecayLength);
        eventBus.registerDoubleObserver(sustainSlider, controls::setSustainLevel);
        eventBus.registerDoubleObserver(releaseSlider, controls::setReleaseLength);

        return controls;
    }

    public HarmonicWeights harmonicWeights(VoltageComponent[] sliders, VoltageKnob[] knobs) {
        HarmonicWeights harmonicWeights = new HarmonicWeights();
        for (int i=0; i<10; i++) {
            final int j = i;
            eventBus.registerDoubleObserver(sliders[i], (v) -> harmonicWeights.setWeight(j, v));
            eventBus.registerDoubleObserver(knobs[i], (v) -> harmonicWeights.setEnvelopeWeighting(j, v));
        }
        return harmonicWeights;
    }

    public VoiceControls voiceControls(VoltageKnob voiceCount,
                                VoltageKnob pitchSpread,
                                VoltageKnob stereoSpread,
                                VoltageKnob vibratoSpeed,
                                VoltageKnob vibratoDepth,
                                VoltageKnob drift,
                               VoltageKnob volumeKnob,
                               VoltageKnob feedbackKnob,
                               VoltageComponent modWheelSelect,
                               VoltageAudioJack rightJack) {
        VoiceControls voiceControls = new VoiceControls();
        eventBus.registerDoubleObserver(voiceCount, voiceControls::setVoiceCount);
        eventBus.registerDoubleObserver(pitchSpread, voiceControls::setPitchSpread);
        eventBus.registerDoubleObserver(stereoSpread, voiceControls::setStereoSpread);
        eventBus.registerDoubleObserver(vibratoSpeed, voiceControls::setVibratoSpeed);
        eventBus.registerDoubleObserver(vibratoDepth, voiceControls::setVibratoDepth);
        eventBus.registerDoubleObserver(drift, voiceControls::setDriftAmount);
        eventBus.registerDoubleObserver(volumeKnob, voiceControls::setVolume);
        eventBus.registerDoubleObserver(feedbackKnob, voiceControls::setFeedbackAmount);
        eventBus.registerDoubleObserver(modWheelSelect, (d) -> voiceControls.setModWheelSelect((int) d));
        eventBus.registerBooleanObserver(rightJack, voiceControls::setStereoMode);
        return voiceControls;
    }

}
