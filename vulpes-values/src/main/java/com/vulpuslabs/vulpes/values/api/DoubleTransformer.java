package com.vulpuslabs.vulpes.values.api;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

@FunctionalInterface
public interface DoubleTransformer {

    double apply(double value);

    default DoubleSupplier transforming(DoubleSupplier supplier) {
        return new DoubleSupplierTransfomer(supplier, this);
    }

    default DoubleConsumer transforming(DoubleConsumer consumer) {
        return new DoubleConsumerTransformer(consumer, this);
    }

    final class DoubleSupplierTransfomer implements DoubleSupplier {

        private final DoubleSupplier supplier;
        private final DoubleTransformer transformer;

        public DoubleSupplierTransfomer(DoubleSupplier supplier, DoubleTransformer transformer) {
            this.supplier = supplier;
            this.transformer = transformer;
        }

        @Override
        public double getAsDouble() {
            return transformer.apply(supplier.getAsDouble());
        }
    }

    final class DoubleConsumerTransformer implements DoubleConsumer {

        private final DoubleConsumer consumer;
        private final DoubleTransformer transformer;

        public DoubleConsumerTransformer(DoubleConsumer consumer, DoubleTransformer transformer) {
            this.consumer = consumer;
            this.transformer = transformer;
        }

        @Override
        public void accept(double value) {
            consumer.accept(transformer.apply(value));
        }
    }

}
