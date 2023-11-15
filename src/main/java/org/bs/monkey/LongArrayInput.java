package org.bs.monkey;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

public class LongArrayInput {
    private final long[] longArray;
    private int position = 0;
    private long lB;
    private int bitsLeft = Long.SIZE;

    LongArrayInput(byte[] array) {
        LongBuffer buffer = ByteBuffer.wrap(array).asLongBuffer();
        this.longArray = new long[buffer.remaining()];
        buffer.get(this.longArray);
        lB = longArray[position++];
    }

    int length() {
        return (int) (longArray[longArray.length - 1] & Util.MASK_ARRAY[32]);
    }

    long getLong(int bits) {
        if (bits <= bitsLeft) {
            bitsLeft -= bits;
            return (lB >>> bitsLeft) & Util.MASK_ARRAY[bits];
        } else {
            long value = lB & Util.MASK_ARRAY[bitsLeft];
            bits -= bitsLeft;
            lB = longArray[position++];
            bitsLeft = Long.SIZE - bits;
            return (value << bits) | (lB >>> bitsLeft);
        }
    }

    boolean readBit() {
        if (bitsLeft > 0) {
            bitsLeft--;
        } else {
            lB = longArray[position++];
            bitsLeft = Long.SIZE - 1;
        }
        return (lB & Util.BIT_SET_MASK[bitsLeft]) != 0;
    }

    long meaningful(int meaningfulSize) {
        return switch (meaningfulSize) {
            case -1 -> 1;
            case 0 -> 3;
            default -> (1L << meaningfulSize | getLong(meaningfulSize)) << 1 | 1;
        };
    }
}
