package com.vulpuslabs.vulpes.modules.distributor;

public class Delay {

    private final int position;
    private int count;

    public Delay(int position, int count) {
        this.position = position;
        this.count = count;
    }

    public int getPosition() {
        return position;
    }

    public boolean tick() {
        count -= 1;
        return count == 0;
    }
}
