package com.vulpuslabs.vulpes.values.events;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public enum TwoPositionSwitchState {
    OFF,
    ON;

    static DoubleConsumer toDoubleObserver(Consumer<TwoPositionSwitchState> observer) {
        return new TwoPositionSwitchSwitchStateObserver(observer);
    }

    private static final class TwoPositionSwitchSwitchStateObserver implements DoubleConsumer {

        private final Consumer<TwoPositionSwitchState> switchStateObserver;

        TwoPositionSwitchSwitchStateObserver(Consumer<TwoPositionSwitchState> switchStateObserver) {
            this.switchStateObserver = switchStateObserver;
        }

        @Override
        public void accept(double value) {
            switchStateObserver.accept(value == 0.0 ? OFF : ON);
        }
    }
}
