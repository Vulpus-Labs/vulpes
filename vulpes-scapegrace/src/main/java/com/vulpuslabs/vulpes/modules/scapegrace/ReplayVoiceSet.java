package com.vulpuslabs.vulpes.modules.scapegrace;

import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

import static com.vulpuslabs.vulpes.buffers.api.Stereo.LEFT;
import static com.vulpuslabs.vulpes.buffers.api.Stereo.RIGHT;

public class ReplayVoiceSet implements Consumer<StereoSample> {

    private final LinkedList<ReplayVoice> voices = new LinkedList<>();

    public void add(ReplayVoice voice) {
        voices.add(voice);
    }

    public int getCount() {
        return voices.size();
    }

    @Override
    public void accept(StereoSample sampleData) {


        Iterator<ReplayVoice> iterator = voices.iterator();
        while (iterator.hasNext()) {
            ReplayVoice voice = iterator.next();
            voice.accept(sampleData);
            if (voice.isFinished()) iterator.remove();
        }
    }
}
