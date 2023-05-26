package com.vulpuslabs.vulpes.buffers.stereo;

import com.vulpuslabs.vulpes.values.api.DoubleBiConsumer;

import java.util.function.DoubleConsumer;

public class StereoSample {

    private double left;
    private double right;

    public void reset() {
        left = 0.0;
        right = 0.0;
    }

    public void set(double left, double right) {
        this.left = left;
        this.right = right;
    }

    public void add(StereoSample other) {
        add(other.left, other.right);
    }

    public void add(double left, double right) {
        this.left += left;
        this.right += right;
    }

    public void multiply(double rhs) {
        left *= rhs;
        right *= rhs;
    }

    public void mix(StereoSample other, double amount) {
        double complement = 1.0 - amount;
        left = (complement * left) + (amount * other.left);
        right = (complement * right) + (amount * other.right);
    }

    public void writeTo(DoubleConsumer leftConsumer, DoubleConsumer rightConsumer) {
        leftConsumer.accept(left);
        rightConsumer.accept(right);
    }

    public void writeTo(DoubleBiConsumer consumer) {
        consumer.accept(left, right);
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }
}
