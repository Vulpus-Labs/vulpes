package com.vulpuslabs.vulpes.modules.segments;

public class Controller {

    private final InputBus inputBus;
    private final OutputBus outputBus;
    private final Curve curve;
    private boolean continuousMode;

    public Controller(InputBus inputBus, OutputBus outputBus, Curve curve) {
        this.inputBus = inputBus;
        this.outputBus = outputBus;
        this.curve = curve;
    }

    public void processSample() {
        double start = inputBus.getStart();
        double end = inputBus.getEnd();
        double length = inputBus.getLength();

        if (inputBus.hasTriggered()) {
            int lengthSamples = inputBus.getLengthSamples();
            int gateLengthSamples = (int) (lengthSamples * inputBus.getGateLengthPercent());

            curve.start(
                    start,
                    end,
                    lengthSamples,
                    gateLengthSamples);
        }

		boolean wasRunning = curve.isRunning();
        double startOut = start;
        double endOut = end;
        if (wasRunning) {
            if (continuousMode) {
                curve.update(start, end);
            } else {
                startOut = curve.getStart();
                endOut = curve.getEnd();
            }
        }
        double cv = curve.getCv(inputBus.getCv());
        boolean hasEnded = wasRunning && !curve.isRunning();
        
        double gate = curve.getGate(inputBus.getGate());

        outputBus.tick(hasEnded, startOut, endOut, length, gate, cv);
    }

    public void setContinuousMode(double switchValue) {
        continuousMode = switchValue == 1.0;
    }
}
