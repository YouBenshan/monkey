package you.bs.monkey;

/**
 * @author You Benshan
 */
public class FloatCompressor {
    private final LongArrayOutput out;
    private int size = 0;
    private int storedVal = 0;

    public FloatCompressor() {
        this.out = new LongArrayOutput();
    }

    public static byte[] compress(float[] values) {
        FloatCompressor compressor = new FloatCompressor();
        for (float value : values) {
            compressor.compressOne(value);
        }
        return compressor.finish();
    }

    public static float[] decompress(byte[] values) {
        if (values.length == 0) {
            return new float[0];
        }
        LongArrayInput in = new LongArrayInput(values);
        int length = in.length();
        float[] result = new float[length];
        int storedVal = 0;
        for (int i = 0; i < length; i++) {
            if (in.readBit()) {
                int trailingZeros = (int) in.getLong(Util.MAX_INTEGER_BIT_SIZE);
                int diff = 1;
                int max = Util.INTEGER_BIT_SIZE[trailingZeros];
                if (max > 0) {
                    int leadingZeros = (int) in.getLong(max);
                    int meaningfulSize = Integer.SIZE - leadingZeros - trailingZeros - 2;
                    diff = (int) in.meaningful(meaningfulSize);
                }
                storedVal ^= diff << trailingZeros;
            }
            result[i] = Float.intBitsToFloat(storedVal);
        }
        return result;
    }

    public void compressOne(float value) {
        size++;
        int valueBits = Float.floatToRawIntBits(value);
        if (storedVal == valueBits) {
            out.skipBit();
        } else {
            int diff = valueBits ^ storedVal;

            int leadingZeros = Integer.numberOfLeadingZeros(diff);
            int trailingZeros = Integer.numberOfTrailingZeros(diff);
            out.writeBits((((1L << Util.MAX_INTEGER_BIT_SIZE) | trailingZeros) << Util.INTEGER_BIT_SIZE[trailingZeros]) | leadingZeros, 1 + Util.MAX_INTEGER_BIT_SIZE + Util.INTEGER_BIT_SIZE[trailingZeros]);

            int notMeaningfulSize = leadingZeros + trailingZeros + 2;
            int meaningfulSize = Integer.SIZE - notMeaningfulSize;
            if (meaningfulSize > 0) {
                out.writeBits((diff << (1 + leadingZeros)) >>> (notMeaningfulSize), meaningfulSize);
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
