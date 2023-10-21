package com.vulpuslabs.vulpes.modules.chebysynth;

import com.vulpuslabs.vulpes.values.Approximate;
import com.vulpuslabs.vulpes.values.random.RandomDouble;
import com.vulpuslabs.vulpes.values.smoothed.DCBlocker;
import com.vulpuslabs.vulpes.values.smoothed.SmoothedValue;

public class ChebyshevOscillator {

    private final double sampleRateReciprocal;
    private double pos = 0.0;

    private double feedback = 0.0;

    private final SmoothedValue driftValue = new SmoothedValue(0.0001, 0.5);
    private final RandomDouble fastRandom = new RandomDouble(0);

    private final DCBlocker dcBlocker = new DCBlocker();

    public ChebyshevOscillator(double sampleRate) {
        sampleRateReciprocal = 1.0 / sampleRate;
    }

    public double getSample(double feedbackAmount, double frequency, double driftAmount, double[] coefficients) {
        driftValue.accept(fastRandom.getAsDouble());
        double drift = driftValue.getAsDouble();
        var x = Approximate.cosUnit(pos);

        if (feedbackAmount > 0.0) {
            // Wavefold to keep chebyshev within bounds
            x += feedback * feedbackAmount;
            double z = 0.25 * (x + 1);
            double frac = z - Math.floor(z);
            x = 1 - 4 * Math.abs(0.5 - frac);
        }
        var twoX = x * 2.0;

        double sum = 0.0;

        double x2 = x;
        sum += x2 * coefficients[0];
        double x1 = (twoX * x2) - 1.0;
        sum += x1  * coefficients[1];

        for (int i=2; i<10; i++) {
            double newX1 = (twoX * x1) - x2;
            sum += newX1 * coefficients[i];
            x2 = x1;
            x1 = newX1;
        }

        if (feedbackAmount > 0.0) {
            feedback += 0.2 * (sum - feedback);
        }
        pos += frequency * (1.0 + (drift - 0.5) * driftAmount * 2.0) * sampleRateReciprocal;
        pos -= Math.floor(pos);

        return sum;
    }

    public double getPosition() {
        return pos;
    }

    public void setPosition(double position) {
        pos = position;
    }

}
