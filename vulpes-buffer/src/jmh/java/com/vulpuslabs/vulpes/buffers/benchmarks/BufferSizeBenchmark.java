package com.vulpuslabs.vulpes.buffers.benchmarks;

import com.vulpuslabs.vulpes.buffers.api.BufferSize;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BufferSizeBenchmark {

    @Benchmark
    public double fastWrap(WrapState state) {
        return BufferSize.BUFFER_1k.wrap(state.value);
    }

    @Benchmark
    public double nativeWrap(WrapState state) {
        return BufferSize.BUFFER_1k.slowMod(state.value);
    }

    @State(Scope.Thread)
    public static class WrapState {

        double value;

        @Setup(Level.Iteration)
        public void prepare() {
            Random random = new Random();
            value = random.nextDouble() * 2048;
        }
    }
}
