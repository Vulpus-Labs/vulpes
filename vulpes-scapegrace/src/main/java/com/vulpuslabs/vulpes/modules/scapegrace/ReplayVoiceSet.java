package com.vulpuslabs.vulpes.modules.scapegrace;

import com.vulpuslabs.vulpes.buffers.SampleData;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

import static com.vulpuslabs.vulpes.buffers.api.Stereo.LEFT;
import static com.vulpuslabs.vulpes.buffers.api.Stereo.RIGHT;

public class ReplayVoiceSet implements Consumer<SampleData> {

    private final LinkedList<ReplayVoice> voices = new LinkedList<>();

    public void add(ReplayVoice voice) {
        voices.add(voice);
    }

    public int getCount() {
        return voices.size();
    }

    @Override
    public void accept(SampleData sampleData) {
        var left = 0.0;
        var right = 0.0;

        Iterator<ReplayVoice> iterator = voices.iterator();
        while (iterator.hasNext()) {
            ReplayVoice voice = iterator.next();
            voice.accept(sampleData);
            left += sampleData.getSample(LEFT);
            right += sampleData.getSample(RIGHT);
            if (voice.isFinished()) iterator.remove();
        }

        sampleData.setSample(LEFT, left);
        sampleData.setSample(RIGHT, right);
    }
}
