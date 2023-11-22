package io.github.youbenshan.monkey;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author You Benshan
 */
public class DoubleCompressorTest {
    private void test(double[] ds) {
        byte[] bytes = DoubleCompressor.compress(ds);
//        System.out.println(ds.length * 8 + "-->" + bytes.length);
        double[] doubles = DoubleCompressor.decompress(bytes);
        Assert.assertArrayEquals(ds, doubles, 1e-15);
    }

    @Test
    public void simple() {
        double[] ds = {1.0, -2.0, -2.5, 65537, 2147483650.0, -16384, 2.8, -38.0};
        test(ds);
    }

    @Test
    public void randmon() {
        int mount = 100000;
        Random random = new Random();
        double[] ds = IntStream.range(0, mount).mapToDouble(i -> random.nextDouble()).toArray();
        test(ds);
    }

    @Test
    public void empyt() {
        test(new double[]{});
    }

    @Test
    public void one() {
        test(new double[]{1});
    }


    @Test
    public void leadingAndTrailing() {
        long a = 0L;
        for (int i = 0; i <= Long.SIZE; i++) {
            long b = 1L << i;
            tstLong(a, b);
        }
    }

    @Test
    public void leading() {
        long a = 0L;
        for (int i = 0; i <= Long.SIZE; i++) {
            long b = (1L << i) - 1;
            tstLong(a, b);
        }
    }

    @Test
    public void tailing() {
        long a = 0L;
        long c = 1L << (Long.SIZE - 1);
        for (int i = 0; i <= Long.SIZE; i++) {
            long b = c ^ ((1L << i) - 1);
            tstLong(a, b);
        }
    }

    private void tstLong(long a, long b) {
        double[] ds = new double[]{Double.longBitsToDouble(a), Double.longBitsToDouble(b)};
        test(ds);
        ds = new double[]{Double.longBitsToDouble(b), Double.longBitsToDouble(a)};
        test(ds);
    }

    @Test
    public void special() {
        Random random = new Random();
        double[] sample = new double[]{0d, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.MAX_VALUE, Double.MIN_VALUE, Double.NaN, -0};
        double[] ds = IntStream.range(0, 1000000).mapToDouble(i -> sample[random.nextInt(sample.length)]).toArray();
        test(ds);
    }

    @Test
    public void files() throws IOException, URISyntaxException {
        Map<String, double[]> map = FileUtils.datas();
        for (Map.Entry<String, double[]> e : map.entrySet()) {
            test(e.getValue());
        }

    }
}
