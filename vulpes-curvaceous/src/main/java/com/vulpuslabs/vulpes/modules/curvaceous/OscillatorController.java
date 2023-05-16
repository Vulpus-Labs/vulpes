package com.vulpuslabs.vulpes.modules.curvaceous;

import com.vulpuslabs.vulpes.values.api.IndexedDoubleConsumer;
import com.vulpuslabs.vulpes.values.ranges.Range;

public class OscillatorController {

    private final FrequencySupplier frequencySupplier;
    private final SyncControl syncControl;
    private final IndexedDoubleConsumer output;
    private final CurveControl curveControl;
    private final CurveView curveView;

    private final double[] oscillatorPositions = new double[16];
    private double oscilloscopePos = 0.0;

    private int numberOfVoices = 1;

    public OscillatorController(FrequencySupplier frequencySupplier,
                                SyncControl syncControl,
                                IndexedDoubleConsumer output,
                                CurveControl curveControl, CurveView curveView) {
        this.frequencySupplier = frequencySupplier;
        this.syncControl = syncControl;
        this.output = output;
        this.curveControl = curveControl;
        this.curveView = curveView;
    }

    public void setNumberOfVoices(int numberOfVoices) {
        this.numberOfVoices = numberOfVoices;
        frequencySupplier.setNumberOfVoices(numberOfVoices);
        syncControl.setNumberOfVoices(numberOfVoices);
    }

    public void processSample() {
        frequencySupplier.tick();
        curveControl.tick();
        syncControl.tick();

        for (int i=0; i<numberOfVoices; i++) {
            double freqHz = frequencySupplier.getFrequency(i);
            double oscPos = oscillatorPositions[i];
            double delta = freqHz * SAMPLE_RATE_RECIPROCAL;
            double newPos = syncControl.isTriggering(i) ? 0.0 : oscPos + delta;
            oscillatorPositions[i] = newPos - Math.floor(newPos);
            double x = -5.0 + (10.0 * oscPos);
            double y = curveControl.apply(x);
            output.accept(i, y);
        }

        curveView.plotPoint(oscilloscopePos, Range.CV_BIPOLAR.clamp(curveControl.apply(oscilloscopePos)));
        oscilloscopePos += 0.01;
        if (oscilloscopePos > 5.0) oscilloscopePos -= 10.0;
    }

    private static final double SAMPLE_RATE_RECIPROCAL = 1.0 / 48000;

}
