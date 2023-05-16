package com.vulpuslabs.vulpes.values.events;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public enum ThreePositionSwitchState {
    LOW,
    MID,
    HIGH;

    static DoubleConsumer toDoubleObserver(Consumer<ThreePositionSwitchState> observer) {
        return new ThreePositionSwitchSwitchStateObserver(observer);
    }

    private static final class ThreePositionSwitchSwitchStateObserver implements DoubleConsumer {

        private final Consumer<ThreePositionSwitchState> switchStateObserver;

        ThreePositionSwitchSwitchStateObserver(Consumer<ThreePositionSwitchState> switchStateObserver) {
            this.switchStateObserver = switchStateObserver;
        }

        @Override
        public void accept(double value) {
            switchStateObserver.accept(value == 0.0 ? LOW : (
                    value == 1.0 ? MID : HIGH));
        }
    }
}
