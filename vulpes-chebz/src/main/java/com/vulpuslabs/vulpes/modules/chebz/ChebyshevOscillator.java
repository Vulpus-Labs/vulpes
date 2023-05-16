package com.vulpuslabs.vulpes.modules.chebz;

import com.vulpuslabs.vulpes.values.Approximate;

public class ChebyshevOscillator {

    private final double sampleRateReciprocal;
    private double pos = 0.0;

    public ChebyshevOscillator(double sampleRate, double frequencyHz) {
        sampleRateReciprocal = 1.0 / sampleRate;
    }

    public void getSamplesAt(double position, double[] data) {
        var x = Approximate.cosUnit(position);
        var twoX = x * 2.0;
        data[0] = x;
        data[1] = (twoX * data[0]) - 1.0;
        data[2] = (twoX * data[1]) - data[0];
        data[3] = (twoX * data[2]) - data[1];
        data[4] = (twoX * data[3]) - data[2];
        data[5] = (twoX * data[4]) - data[3];
        data[6] = (twoX * data[5]) - data[4];
        data[7] = (twoX * data[6]) - data[5];
        data[8] = (twoX * data[7]) - data[6];
        data[9] = (twoX * data[8]) - data[7];
    }

    public void getSamples(double frequency, double[] data) {
        getSamplesAt(pos, data);
        pos += frequency * sampleRateReciprocal;
        pos -= Math.floor(pos);
    }

    public double getPosition() {
        return pos;
    }

    public void setPosition(double position) {
        pos = position;
    }

}
