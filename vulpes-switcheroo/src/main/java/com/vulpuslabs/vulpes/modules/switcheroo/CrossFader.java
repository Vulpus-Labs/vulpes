package com.vulpuslabs.vulpes.modules.switcheroo;

public class CrossFader {

    private final Channel[] channels;
    private final CrossFade[] crossFades;

    private Channel lastActiveBeforeCrossfade;

    private final int max;
    private int numberActive;
    private int startPtr;
    private int endPtr;

    public CrossFader(int max, int firstInput, Channel[] channels) {
        this.max = max;
        this.lastActiveBeforeCrossfade = channels[firstInput];
        this.channels = channels;

        this.crossFades = new CrossFade[max + 1];

        numberActive = 0;
        startPtr = 0;
        endPtr = 0;
    }

    public void startCrossFade(int targetChannel, int lengthSamples) {
        if (numberActive == max) {
            return;
        }

        crossFades[endPtr] = new CrossFade(lengthSamples, channels[targetChannel]);

        endPtr += 1;
        if (endPtr > max) {
            endPtr = 0;
        }

        numberActive++;
    }

    public double processSample() {
        double fadedSample = lastActiveBeforeCrossfade.getValue();

        var p = startPtr;
        while (p != endPtr) {
            CrossFade crossFade = crossFades[p];

            p += 1;
            if (p > max) {
                p = 0;
            }

            fadedSample = crossFade.applyTo(fadedSample);

            if (crossFade.isEnded()) {
                lastActiveBeforeCrossfade = crossFade.getTarget();
                startPtr = p;
                numberActive--;
            }
        }

        return fadedSample;
    }

}
