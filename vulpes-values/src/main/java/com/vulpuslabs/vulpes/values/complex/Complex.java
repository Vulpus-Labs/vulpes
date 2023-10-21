package com.vulpuslabs.vulpes.values.complex;

import com.vulpuslabs.vulpes.values.Approximate;

public class Complex {

    private double real;
    private double imaginary;

    public Complex() {

    }

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public void set(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public void add(Complex other) {
        add(other, this);
    }

    public void add(Complex other, Complex target) {
        target.set(real + other.real, imaginary + other.imaginary);
    }

    public void subtract(Complex other) {
        subtract(other, this);
    }

    public void subtract(Complex other, Complex target) {
        target.set(real - other.real, imaginary - other.imaginary);
    }

    public void multiply(Complex other) {
        multiply(other, this);
    }

    public void multiply(Complex other, Complex target) {
        target.set(
                (real * other.real) - (imaginary * other.imaginary),
                (real * other.imaginary) + (imaginary * other.real));
    }

    public void conjugate() {
        imaginary = -imaginary;
    }

    public void scale(double scale) {
        real *= scale;
        imaginary *= scale;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public void toPolar(Polar target) {
        target.set(
                getMagnitude(),
                getPhase()
        );
    }

    public double getMagnitude() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    public double getPhase() {
        return Approximate.atan2(imaginary, real);
    }

    public void copyTo(Complex other) {
        other.real = real;
        other.imaginary = imaginary;
    }

    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public Complex plus(Complex other) {
        Complex c = new Complex();
        add(other, c);
        return c;
    }

    public Complex minus(Complex other) {
        Complex c = new Complex();
        subtract(other, c);
        return c;
    }

    public Complex times(Complex other) {
        Complex c = new Complex();
        multiply(other, c);
        return c;
    }

    public void conjugateTo(Complex other) {
        other.real = real;
        other.imaginary = -imaginary;
    }
}
