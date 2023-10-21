package com.vulpuslabs.vulpes.modules.chebysynth;

import javax.sound.midi.ShortMessage;

public class MidiMapper {

    private static final double ONE_OVER_64 = 1.0 / 64.0;
    private static final double ONE_OVER_128 = 1.0 / 128.0;

    private final PolyVoiceEngine voiceEngine;

    private final VoiceMapper voiceMapper;
    private final VoiceControls voiceControls;

    public MidiMapper(PolyVoiceEngine voiceEngine, VoiceControls voiceControls) {
        this.voiceEngine = voiceEngine;
        this.voiceControls = voiceControls;
        this.voiceMapper = new VoiceMapper(voiceEngine);
    }

    public void receive(ShortMessage event) {
        switch (event.getCommand()) {
            case ShortMessage.NOTE_ON -> voiceMapper.noteOn(event.getData1(), event.getData2());
            case ShortMessage.NOTE_OFF -> voiceMapper.noteOff(event.getData1());
            case ShortMessage.PITCH_BEND ->
                    voiceControls.setPitchBend(Math.pow(2, (double) (event.getData2() - 64) * ONE_OVER_64));
            case ShortMessage.CONTROL_CHANGE -> onControlChange(event);
            case ShortMessage.STOP, ShortMessage.START, ShortMessage.CONTINUE -> voiceMapper.resetAll();
        }
    }

    private void onControlChange(ShortMessage event) {
        if (event.getData1() == 1) {
            voiceControls.setModWheelAmount((double) event.getData2() * ONE_OVER_128);
        } else if (event.getData1() == 64) {
            voiceMapper.setSustaining(event.getData2() >= 64);
        }
    }

}
