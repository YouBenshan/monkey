package you.bs.monkey;

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
public class FloatCompressorTest {
    private void test(float[] ds) {
        byte[] bytes = FloatCompressor.compress(ds);
        System.out.println(ds.length * Float.BYTES + "-->" + bytes.length);
        float[] doubles = FloatCompressor.decompress(bytes);
        Assert.assertArrayEquals(ds, doubles, 1e-15F);
    }

    @Test
    public void simple() {
        float[] ds = {1.0f, -2.0f, -2.5f, 65537f, 2147483650.0f, -16384f, 2.8f, -38.0f};
        test(ds);
    }

    @Test
    public void randmon() {
        int mount = 100000;
        Random random = new Random();
        double[] ds = IntStream.range(0, mount).mapToDouble(i -> random.nextDouble()).toArray();
        test(toFloat(ds));
    }

    @Test
    public void empyt() {
        test(new float[]{});
    }

    @Test
    public void leadingAndTrailing() {
        int a = 0;
        for (int i = 0; i <= Integer.SIZE; i++) {
            int b = 1 << i;
            testInt(a, b);
        }
    }

    private void testInt(int a, int b) {
        float[] ds = new float[]{Float.intBitsToFloat(a), Float.intBitsToFloat(b)};
        test(ds);
        ds = new float[]{Float.intBitsToFloat(b), Float.intBitsToFloat(a)};
        test(ds);
    }

    @Test
    public void leading() {
        int a = 0;
        for (int i = 0; i <= Integer.SIZE; i++) {
            int b = (1 << i) - 1;
            testInt(a, b);

        }
    }

    @Test
    public void tailing() {
        int a = 0;
        int c = 1 << (Integer.SIZE - 1);
        for (int i = 0; i <= Integer.SIZE; i++) {
            int b = c ^ ((1 << i) - 1);
            testInt(a, b);
        }
    }

    @Test
    public void special() {
        Random random = new Random();
        float[] sample = new float[]{0f, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.MAX_VALUE, Float.MIN_VALUE, Float.NaN, -0};
        float[] fs = new float[100000];
        for (int i = 0; i < fs.length; i++) {
            fs[i] = sample[random.nextInt(sample.length)];
        }
        test(fs);
    }

    @Test
    public void files() throws IOException, URISyntaxException {
        Map<String, double[]> map = FileUtils.datas();
        for (Map.Entry<String, double[]> e : map.entrySet()) {
            test(toFloat(e.getValue()));
        }
    }

    private float[] toFloat(double[] ds) {
        float[] fs = new float[ds.length];
        for (int i = 0; i < ds.length; i++) {
            fs[i] = (float) ds[i];
        }
        return fs;
    }
}
