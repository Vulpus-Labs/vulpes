package com.vulpuslabs.vulpes.values.events;

import com.vulpuslabs.vulpes.values.api.BooleanConsumer;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class EventBus {

    private long nextRegistrationKey;
    private final Map<Long, Object> componentsByRegistrationKey = new HashMap<>();
    private final Map<Object, Map<Long, DoubleConsumer>> doubleObservers = new IdentityHashMap<>();
    private final Map<Object, Map<Long, BooleanConsumer>> booleanObservers = new IdentityHashMap<>();

    private <V> long registerObserver(Object component, V observer, Map<Object, Map<Long, V>> registrations) {
        var registrationKey = nextRegistrationKey++;
        registrations.computeIfAbsent(component, (c) -> new HashMap<>())
                .put(registrationKey, observer);
        componentsByRegistrationKey.put(registrationKey, component);
        return registrationKey;
    }

    public long registerDoubleObserver(Object component, DoubleConsumer doubleObserver) {
        return registerObserver(component, doubleObserver, doubleObservers);
    }

    public void unregisterDoubleObserver(long registrationKey) {
        unregisterObserver(registrationKey, doubleObservers);
    }

    public long registerBooleanObserver(Object component, BooleanConsumer booleanObserver) {
        return registerObserver(component, booleanObserver, booleanObservers);
    }

    public void unregisterBooleanObserver(long registrationKey) {
        unregisterObserver(registrationKey, booleanObservers);
    }

    public boolean onDouble(Object component, double value) {
        var observers = doubleObservers.get(component);
        if (observers == null) return false;

        for (DoubleConsumer observer : observers.values()) {
            observer.accept(value);
        }
        return true;
    }

    public boolean onBoolean(Object component, boolean value) {
        var observers = booleanObservers.get(component);
        if (observers == null) return false;

        for (BooleanConsumer observer : observers.values()) {
            observer.accept(value);
        }
        return true;
    }

    private <V> void unregisterObserver(long registrationKey, Map<Object, Map<Long, V>> registrations) {
        var component = componentsByRegistrationKey.get(registrationKey);
        if (component == null) return;
        componentsByRegistrationKey.remove(registrationKey);

        var observers = registrations.get(component);
        if (observers == null) return;

        observers.remove(registrationKey);
        if (observers.isEmpty()) {
            registrations.remove(component);
        }
    }
}
