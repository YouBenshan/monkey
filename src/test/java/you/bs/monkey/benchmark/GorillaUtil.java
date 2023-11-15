package you.bs.monkey.benchmark;

import fi.iki.yak.ts.compression.gorilla.LongArrayInput;
import fi.iki.yak.ts.compression.gorilla.LongArrayOutput;
import fi.iki.yak.ts.compression.gorilla.ValueCompressor;
import fi.iki.yak.ts.compression.gorilla.ValueDecompressor;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

public class GorillaUtil {
    public static byte[] compress(double[] ds) {
        LongArrayOutput output = new LongArrayOutput();
        ValueCompressor gorilla = new ValueCompressor(output);
        gorilla.writeFirst(Double.doubleToRawLongBits(ds[0]));
        for (int i = 1; i < ds.length; i++) {
            gorilla.compressValue(Double.doubleToRawLongBits(ds[i]));
        }
        long[] longArray = output.getLongArray();
        ByteBuffer bb = ByteBuffer.allocate(Long.BYTES * longArray.length);
        bb.asLongBuffer().put(longArray, 0, longArray.length);
        return bb.array();
    }

    public static double[] decompress(byte[] bytes, int length) {
        LongBuffer buffer = ByteBuffer.wrap(bytes).asLongBuffer();
        long[] longArray = new long[buffer.remaining()];
        buffer.get(longArray);

        LongArrayInput output = new LongArrayInput(longArray);
        ValueDecompressor gorilla = new ValueDecompressor(output);
        double[] ans = new double[length];
        ans[0] = Double.longBitsToDouble(gorilla.readFirst());
        for (int i = 1; i < length; i++) {
            ans[i] = Double.longBitsToDouble(gorilla.nextValue());
        }
        return ans;
    }
}
