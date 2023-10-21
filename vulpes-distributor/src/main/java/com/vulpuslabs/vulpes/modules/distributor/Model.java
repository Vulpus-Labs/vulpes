package com.vulpuslabs.vulpes.modules.distributor;

import com.vulpuslabs.vulpes.values.random.RandomDouble;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;

public class Model {

    private final int[] gates = new int[15];
    private final double[] balances = new double[10];
    private final LinkedList<Delay> delays = new LinkedList<>();
    private final Stack<Integer> toPropagate = new Stack<Integer>();

    private int delayCount;
    private final RandomDouble random = new RandomDouble(0L);

    private int gateLength;
    private int delayLength;
    private double gateJitter;
    private double delayJitter;

    public void setBalance(int index, double amount) {
        balances[index] = amount;
    }

    public void setGateLength(double unit) {
        gateLength = (int) (48000 * unit);
    }

    public void setDelayLength(double unit) {
        delayLength = (int) (48000 * unit);
    }

    public void setGateJitter(double gateJitter) {
        this.gateJitter = gateJitter;
    }

    public void setDelayJitter(double unit) {
        this.delayJitter = unit;
    }

    private void touch(int position) {
        double gateJitterAmount = gateJitter == 0.0 ? 1.0 :  1.0 - random.getAsDouble() * gateJitter;
        gates[position] = (int) (gateLength * gateJitterAmount);

        double delayJitterAmount = delayJitter == 0.0 ? 1.0 :  1.0 - random.getAsDouble() * delayJitter;
        int delayAmount = (int) (this.delayLength * delayJitterAmount);

        if (delayAmount == 0) {
            propagate(position);
        } else {
            if (delayCount < 256) {
                delays.add(new Delay(position, delayAmount));
                delayCount += 1;
            }
        }
    }

    private void propagate(int position) {
        int hi = switch (position) {
            case 0 -> 1;
            case 1 -> 3;
            case 2 -> 4;
            case 3 -> 6;
            case 4 -> 7;
            case 5 -> 8;
            case 6 -> 10;
            case 7 -> 11;
            case 8 -> 12;
            case 9 -> 13;
            default -> -1;
        };

        if (hi == -1) return;

        int lo = switch (position) {
            case 0 -> 2;
            case 1 -> 4;
            case 2 -> 5;
            case 3 -> 7;
            case 4 -> 8;
            case 5 -> 9;
            case 6 -> 11;
            case 7 -> 12;
            case 8 -> 13;
            case 9 -> 14;
            default -> -1;
        };

        double hiOrLo = random.getAsDouble();
        int next = hiOrLo > balances[position] ? lo : hi;

        touch(next);
    }

    public void tick(boolean addTrigger, boolean[] isGating) {
        if (addTrigger) {
            touch(0);
        }

        for (int i=0; i<15; i++) {
            int gateCount = gates[i];
            if (gateCount > 0) {
                isGating[i] = true;
                gates[i] = gateCount - 1;
            } else {
                isGating[i] = false;
            }
        }

        ListIterator<Delay> delayIterator = delays.listIterator();
        while (delayIterator.hasNext()) {
            Delay delay = delayIterator.next();
            if (delay.tick()) {
                delayIterator.remove();
                delayCount -= 1;
                toPropagate.add(delay.getPosition());
            }
        }

        while (!toPropagate.isEmpty()) {
            propagate(toPropagate.pop());
        }
    }
}
