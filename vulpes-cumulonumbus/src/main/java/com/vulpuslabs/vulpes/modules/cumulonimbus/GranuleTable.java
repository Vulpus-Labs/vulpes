package com.vulpuslabs.vulpes.modules.cumulonimbus;

import com.vulpuslabs.vulpes.buffers.api.BufferRandomAccess;

import java.util.function.Consumer;

public class GranuleTable {

    private final int lastIndex;

    private final Consumer<Granule> configure;
    private final Granule[] granules;

    private int ramtop;
    private int activeCount;

    public GranuleTable(int maxSize, Consumer<Granule> configure) {
        lastIndex = maxSize - 1;
        this.configure = configure;

        // Preallocate array
        granules = new Granule[maxSize];

        for (int i=0; i<maxSize; i++) {
            granules[i] = new Granule();
        }
    }

    public double readSample(BufferRandomAccess bufferRandomAccess,
                             GranuleCurve curve,
                             boolean addNew) {
        double result = 0.0;

        for (int i=0; i < ramtop; i++) {
            Granule granule = granules[i];
            if (granule.isFinished()) {
                if (addNew) {
                    configure.accept(granule);
                    result += granule.nextSample(bufferRandomAccess, curve);
                    addNew = false;
                } else if (i == ramtop - 1) {
                    // Allow the collection to shrink as granules complete.
                    ramtop--;
                }
            } else {
                result += granule.nextSample(bufferRandomAccess, curve);
            }
        }

        if (addNew && ramtop < lastIndex) {
            Granule granule = granules[ramtop++];
            configure.accept(granule);
            result += granule.nextSample(bufferRandomAccess, curve);
        }

        return result;
    }
}
