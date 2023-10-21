package com.vulpuslabs.vulpes.buffers.api;

public enum BufferSize {

    BUFFER_1b(0),
    BUFFER_2b(1),
    BUFFER_4b(2),
    BUFFER_8b(3),
    BUFFER_16b(4),
    BUFFER_32b(5),
    BUFFER_64b(6),
    BUFFER_128b(7),
    BUFFER_256b(8),
    BUFFER_512b(9),
    BUFFER_1k(10),
    BUFFER_2k(11),
    BUFFER_4k(12),
    BUFFER_8k(13),
    BUFFER_16k(14),
    BUFFER_32k(15),
    BUFFER_64k(16),
    BUFFER_128k(17),
    BUFFER_256k(18),
    BUFFER_512k(19),
    BUFFER_1m(20),
    BUFFER_2m(21),
    BUFFER_4m(22);

    private int exponent;
    private int size;
    private double sizeReciprocal;
    private int lastIndex;

    BufferSize(int exponent) {
        this.exponent = exponent;
        this.size = 1 << exponent;
        this.sizeReciprocal = 1.0 / size;
        this.lastIndex = size - 1;
    }

    public int getSize() {
        return size;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public int wrap(int value) {
        return value & lastIndex;
    }

}
