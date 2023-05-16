package com.vulpuslabs.vulpes.buffers.benchmarks;

import com.vulpuslabs.vulpes.values.events.EventBus;
import com.vulpuslabs.vulpes.values.events.UIEventConnector;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Random;
import java.util.function.DoubleSupplier;

public class ReflectionBenchmark {

    public static final class NoiseSource {
        private final Random random = new Random();

        public double GetValue() {
            return random.nextDouble();
        }
    }

    @State(Scope.Benchmark)
    public static class DirectCaller {

        private final NoiseSource noiseSource = new NoiseSource();

        public double getValue() {
            return noiseSource.GetValue();
        }

    }

    @State(Scope.Benchmark)
    public static class MethodHandleCaller {

        private final NoiseSource noiseSource = new NoiseSource();
        private final DoubleSupplier supplier = noiseSource::GetValue;

        public double getValue() {
            return supplier.getAsDouble();
        }

    }

    @State(Scope.Benchmark)
    public static class ReflectiveCaller {

        private final EventBus eventBus = new EventBus();
        private final UIEventConnector connector = new UIEventConnector(eventBus);
        private final NoiseSource noiseSource = new NoiseSource();
        private final DoubleSupplier supplier = connector.getDoubleSupplier(noiseSource, "GetValue");

        public double getValue() {
            return supplier.getAsDouble();
        }

    }

    //@Benchmark
    @Fork(value = 1, warmups = 1)
    public double addRandomsDirect(DirectCaller caller) {
        double result = 0.0;
        for (int i=0; i<1000; i++) {
            result += caller.getValue();
        }
        return result;
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    public double addRandomsMethodHandle(MethodHandleCaller caller) {
        double result = 0.0;
        for (int i=0; i<1000; i++) {
            result += caller.getValue();
        }
        return result;
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    public double addRandomsReflective(ReflectiveCaller caller) {
        double result = 0.0;
        for (int i=0; i<1000; i++) {
            result += caller.getValue();
        }
        return result;
    }

}
