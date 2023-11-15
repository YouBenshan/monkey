package org.bs.monkey;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

public class LongArrayOutput {
    private static final int DEFAULT_ALLOCATION = 256;


    private long[] longArray;
    private int position = 0;
    private long lB = 0L;
    private int bitsLeft = Long.SIZE;

    LongArrayOutput(int initialSize) {
        this.longArray = new long[initialSize];
    }

    LongArrayOutput() {
        this(DEFAULT_ALLOCATION);
    }

    void skipBit() {
        if (bitsLeft == 0) {
            enoughLongArray();
            longArray[position++] = lB;
            bitsLeft = Long.SIZE - 1;
            lB = 0L;
        } else {
            bitsLeft--;
        }
    }

    public void writeBit() {
        if (bitsLeft == 0) {
            enoughLongArray();
            longArray[position++] = lB;
            bitsLeft = Long.SIZE - 1;
            lB = 1L<<bitsLeft;
        } else {
            bitsLeft--;
            lB|=1L<<bitsLeft;
        }
    }

    private void enoughLongArray() {
        if (longArray.length == position) {
            long[] largerArray = new long[longArray.length<<1];
            System.arraycopy(longArray, 0, largerArray, 0, longArray.length);
            longArray = largerArray;
        }
    }

    byte[] finish(int length) {
        appendLength(length);
        ByteBuffer bb = ByteBuffer.allocate(Long.BYTES * position);
        bb.asLongBuffer().put(longArray, 0, position);
        return bb.array();
    }

    private void appendLength(int length) {
        if (bitsLeft >= 32) {
            lB |= length;
            enoughLongArray();
            longArray[position++] = lB;
        } else {
            enoughLongArray();
            longArray[position++] = lB;
            enoughLongArray();
            longArray[position++] = length;
        }
    }

    void writeBits(long value, int bits) {
        if (bits <= bitsLeft) {
            bitsLeft -= bits;
            lB |= value << bitsLeft;
        } else {
            bits -= bitsLeft;
            enoughLongArray();
            longArray[position++] = lB | (value >>> bits);
            bitsLeft = Long.SIZE - bits;
            lB = value << bitsLeft;
        }
    }
}