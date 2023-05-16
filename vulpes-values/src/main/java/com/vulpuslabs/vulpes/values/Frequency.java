package com.vulpuslabs.vulpes.values;

public final class Frequency {

    private Frequency() {

    }

    public static final double C2 = 65.4;

    public static double fromCv(double cv) {
        return C2 * Math.pow(2.0, cv);
    }

    public static int toSampleLength(double frequency, int sampleRate) {
        return (int) Math.ceil(sampleRate / frequency);
    }

}
