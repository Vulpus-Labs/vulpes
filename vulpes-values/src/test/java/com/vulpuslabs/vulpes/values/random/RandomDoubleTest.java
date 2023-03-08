package com.vulpuslabs.vulpes.values.random;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class RandomDoubleTest {

    @Test
    public void getSomeDoubles() {
        RandomDouble randomDouble = new RandomDouble(new Random().nextLong());
        for (int i=0; i<1000; i++) {
            //System.out.println(randomDouble.getAsDouble());
        }
    }
}
