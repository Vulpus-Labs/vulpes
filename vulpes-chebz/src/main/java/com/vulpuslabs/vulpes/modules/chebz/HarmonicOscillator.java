package com.vulpuslabs.vulpes.modules.chebz;

public interface HarmonicOscillator {

    int ODDS = 0;
    int EVENS = 1;
    int BOTH = 2;

    void processSample(
            double frequency,
            double oddEvenBalance,
            double[] coefficients,
            double[] unmodulated,
            double[] modulated,
            double[] blended);

    double getSampleAt(double position, double oddEvenBalance, double[] coefficients, double[] unmodulated, double[] modulated, double[] blended);

    boolean isFolding();
    void setIsFolding(boolean isFolding);
    double getPosition();
    void setPosition(double newPosition);
}
