package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.buffers.stereo.StereoBuffer;
import com.vulpuslabs.vulpes.buffers.stereo.StereoSample;
import com.vulpuslabs.vulpes.values.api.DoubleBiConsumer;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class GranuleTable {

    private final int lastIndex;

    private final Consumer<Granule> configure;
    private final Granule[] granules;

    private final StereoSample bufferSample = new StereoSample();

    private int ramtop;

    public GranuleTable(int maxSize, Consumer<Granule> configure) {
        lastIndex = maxSize - 1;
        this.configure = configure;

        // Preallocate array
        granules = new Granule[maxSize];

        for (int i=0; i<maxSize; i++) {
            granules[i] = new Granule();
        }
    }

    public int getActiveCount() {
        return ramtop;
    }

    public void readSample(StereoBuffer buffer,
                           StereoSample sample,
                           boolean isFrozen,
                           boolean addNew) {
        sample.set(0, 0);
        double freezeDelta = isFrozen ? -1.0 : 0.0;

        for (int i=0; i < ramtop; i++) {
            Granule granule = granules[i];
            if (granule.isFinished()) {
                if (addNew) {
                    configure.accept(granule);
                    granule.nextSample(buffer, bufferSample, freezeDelta);
                    sample.add(bufferSample);
                    addNew = false;
                } else {
                    if (i == ramtop - 1) {
                        // Allow the collection to shrink as granules complete.
                        ramtop--;
                    }
                }
            } else {
                granule.nextSample(buffer, bufferSample, freezeDelta);
                sample.add(bufferSample);
            }
        }

        if (addNew && ramtop <= lastIndex) {
            Granule granule = granules[ramtop++];
            configure.accept(granule);
            granule.nextSample(buffer, bufferSample, freezeDelta);
            sample.add(bufferSample);
        }
    }

    public int getMaxCount() {
        return granules.length;
    }

    public void getPositions(DoubleBiConsumer c) {
        for (int i=0; i<ramtop; i++) {
            Granule granule = granules[i];
            if (!granule.isFinished()) {
                granule.getPosition(c);
            }
        }
    }
}
