package you.bs.monkey;

/**
 * @author You Benshan
 */
public class DoubleCompressor {
    private final LongArrayOutput out;
    private int size = 0;
    private long storedVal = 0L;

    public DoubleCompressor() {
        this.out = new LongArrayOutput();
    }

    public static byte[] compress(double[] values) {
        DoubleCompressor compressor = new DoubleCompressor();
        for (double value : values) {
            compressor.compressOne(value);
        }
        return compressor.finish();
    }

    public static double[] decompress(byte[] values) {
        if (values.length == 0) {
            return new double[0];
        }
        LongArrayInput in = new LongArrayInput(values);
        int length = in.length();
        double[] result = new double[length];
        long storedVal = 0L;
        for (int i = 0; i < length; i++) {
            if (in.readBit()) {
                int trailingZeros = (int) in.getLong(Util.MAX_LONG_BIT_SIZE);
                long diff = 1L;
                int max = Util.LONG_BIT_SIZE[trailingZeros];
                if (max > 0) {
                    int leadingZeros = (int) in.getLong(max);
                    int meaningfulSize = Long.SIZE - leadingZeros - trailingZeros - 2;
                    diff = in.meaningful(meaningfulSize);
                }
                storedVal ^= diff << trailingZeros;
            }
            result[i] = Double.longBitsToDouble(storedVal);
        }
        return result;
    }

    public void compressOne(double value) {
        size++;
        long valueBits = Double.doubleToRawLongBits(value);
        if (storedVal == valueBits) {
            out.skipBit();
        } else {
            long diff = valueBits ^ storedVal;
            int trailingZeros = Long.numberOfTrailingZeros(diff);
            out.writeBit();
            out.writeBits(trailingZeros, Util.MAX_LONG_BIT_SIZE);
            if (Util.LONG_BIT_SIZE[trailingZeros] > 0) {
                int leadingZeros = Long.numberOfLeadingZeros(diff);
                out.writeBits(leadingZeros, Util.LONG_BIT_SIZE[trailingZeros]);

                int notMeaningfulSize = leadingZeros + trailingZeros + 2;
                int meaningfulSize = Long.SIZE - notMeaningfulSize;
                if (meaningfulSize > 0) {
                    out.writeBits((diff << (1 + leadingZeros)) >>> (notMeaningfulSize), meaningfulSize);
                }
            }
            storedVal = valueBits;
        }
    }

    public byte[] finish() {
        if (size == 0) {
            return new byte[0];
        }
        return out.finish(size);
    }
}