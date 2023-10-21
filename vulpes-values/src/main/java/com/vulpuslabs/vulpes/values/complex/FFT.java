package com.vulpuslabs.vulpes.values.complex;

public class FFT {

    private final Complex w = new Complex();
    private final Complex tao = new Complex();

    public void transform(Complex[] x) {
        int n = x.length;
        permutate(x);

        int sliceCount = n >> 1;
        int halfSize = 1;
        int sliceSize = 2;

        while (sliceSize <= n) {
            double thetaDelta = 2.0 * Math.PI / sliceSize;
            double theta = 0.0;

            for (int j = 0; j < halfSize; j++) {
                w.set(Math.cos(theta), -Math.sin(theta));
                theta += thetaDelta;

                int sliceStart = 0;
                for (int k=0; k < sliceCount; k++) {
                    int lIndex = sliceStart + j;
                    int rIndex = lIndex + halfSize;
                    Complex l = x[lIndex];
                    Complex r = x[rIndex];
                    w.multiply(r, tao); // tao = w * r
                    l.subtract(tao, r); // r = l - tao
                    l.add(tao);         // l = l + tao

                    sliceStart += sliceSize;
                }
            }

            halfSize = sliceSize;
            sliceSize = sliceSize << 1;
            sliceCount = sliceCount >> 1;
        }
    }

    public void reverseTransform(Complex[] x) {
        double scale = 1.0 / x.length;

        for (Complex c : x) {
            c.conjugate();
        }

        transform(x);

        for (Complex c : x) {
            c.conjugate();
            c.scale(scale);
        }
    }

   public void circularConvolve(Complex[] x, Complex[] y, Complex[] target) {
        transform(x);
        transform(y);

        for (int i=0; i < x.length; i++) {
            x[i].multiply(y[i], target[i]);
        }

        reverseTransform(target);
   }

    private void permutate(Complex[] x) {
        int size = x.length;
        int shift = 1 + Integer.numberOfLeadingZeros(size);

        for (int k = 0; k < size; k++) {
            int j = Integer.reverse(k) >>> shift;
            if (j > k) {
                Complex tmp = x[j];
                x[j] = x[k];
                x[k] = tmp;
            }
        }
    }

}
