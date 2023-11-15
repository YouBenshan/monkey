package org.bs.monkey;

public class Util {

    static final long[] MASK_ARRAY = maskArray();
    static final long[] BIT_SET_MASK = bitSetMask();
    private static final int[] MAX_BIT_COUNT = maxBitCount();
    static final int MAX_LONG_BIT_SIZE = MAX_BIT_COUNT[Long.SIZE - 1];
    static final int MAX_INTEGER_BIT_SIZE = MAX_BIT_COUNT[Integer.SIZE - 1];
    static final int[] LONG_BIT_SIZE = bitSize(Long.SIZE);
    static final int[] INTEGER_BIT_SIZE = bitSize(Integer.SIZE);

    private static long[] maskArray() {
        long[] ans = new long[Long.SIZE];
        long mask = 1;
        long value = 0;
        for (int i = 1; i < ans.length; i++) {
            value = value | mask;
            ans[i] = value;
            mask = mask << 1;
        }
        return ans;
    }

    private static long[] bitSetMask() {
        long[] ans = new long[Long.SIZE];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (1L << i);
        }
        return ans;
    }

    private static int[] bitSize(int size) {
        int[] ans = new int[size];
        for (int i = 0; i < size; i++) {
            ans[i] = maxTrailingBitsCount(size, i);
        }
        return ans;
    }

    private static int[] maxBitCount() {
        int[] ans = new int[Long.SIZE];
        int v = 0;
        for (int i = 0; i < ans.length; i++) {
            if ((1 << v) < i + 1) {
                v++;
            }
            ans[i] = v;
        }
        return ans;
    }

    private static int maxTrailingBitsCount(int size, int leadingZeros) {
        int maxTrailBitCount = size - leadingZeros - 1;
        return MAX_BIT_COUNT[maxTrailBitCount];
    }

}
