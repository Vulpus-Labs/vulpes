package com.vulpuslabs.vulpes.modules.swirl;

import com.vulpuslabs.vulpes.values.api.IndexedDoubleBiConsumer;
import com.vulpuslabs.vulpes.values.polar.PolarInterpolator;
import com.vulpuslabs.vulpes.values.polar.PolarValue;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class SwirlModel implements IndexedDoubleBiConsumer {

    private final int size;
    private final PolarValue[] values;
    private final PolarInterpolator[] interpolators;

    public SwirlModel(int size) {
        this.size = size;
        this.values = new PolarValue[size];
        this.interpolators = new PolarInterpolator[size];

        for (int i=0; i<size; i++) {
            values[i] = new PolarValue(0, 0);
            interpolators[i] = new PolarInterpolator(values[i]);
        }
    }

    public void applyInterpolation(Consumer<PolarInterpolator> setTarget) {
        for (PolarInterpolator interpolator : interpolators) {
            if (interpolator.tick()) {
                setTarget.accept(interpolator);
            }
        }
    }

    public void setNewTargets(Consumer<PolarInterpolator> setTarget) {
        for (PolarInterpolator interpolator : interpolators) {
            setTarget.accept(interpolator);
        }
    }

    public int getSize() {
        return size;
    }

    public void getCartesian(int activeSize, DoubleConsumer[] xConsumers, DoubleConsumer[] yConsumers) {
        for (int i=0; i<activeSize; i++) {
            var value = values[i];
            xConsumers[i].accept(value.getCartesianX());
            yConsumers[i].accept(value.getCartesianY());
        }
    }

    public void adjustSpeed(double ratio) {
        double reciprocal = 1.0 / ratio;
        for (PolarInterpolator interpolator : interpolators) {
            interpolator.adjustSpeed(ratio, reciprocal);
        }
    }

    public PolarValue[] getPolarValues() {
        return values;
    }

    @Override
    public void accept(int index, double first, double second) {
        values[index].set(first, second);
    }
}
