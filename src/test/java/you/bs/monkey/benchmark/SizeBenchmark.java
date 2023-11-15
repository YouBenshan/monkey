package you.bs.monkey.benchmark;

import org.junit.Assert;
import org.junit.Test;
import you.bs.monkey.DoubleCompressor;
import you.bs.monkey.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class SizeBenchmark {

    @Test
    public void testSize() throws IOException, URISyntaxException {
        Map<String, double[]> map = FileUtils.datas();
        System.out.println("File Name:\tMonkey size\tGrilla Size\tMonkey/Gorilla");
        for (Map.Entry<String, double[]> e : map.entrySet()) {
            byte[] monkey = DoubleCompressor.compress(e.getValue());
            byte[] gorilla = GorillaUtil.compress(e.getValue());

            System.out.println(e.getKey() + ":\t" + monkey.length + "\t" + gorilla.length + "\t" + monkey.length * 1d / gorilla.length);
        }
    }

    @Test
    public void gorilla() throws IOException, URISyntaxException {
        Map<String, double[]> map = FileUtils.datas();
        for (Map.Entry<String, double[]> e : map.entrySet()) {
            byte[] gorilla = GorillaUtil.compress(e.getValue());
            double[] ans = GorillaUtil.decompress(gorilla, e.getValue().length);
            Assert.assertArrayEquals(e.getValue(), ans, 1e-15);

        }
    }

    @Test
    public void files() throws IOException, URISyntaxException {
        Map<String, double[]> map = FileUtils.datas();
        boolean f = false;
        while (f) {
            for (double[] ds : map.values()) {
                byte[] bytes = DoubleCompressor.compress(ds);
                double[] doubles = DoubleCompressor.decompress(bytes);

                byte[] gorilla = GorillaUtil.compress(ds);
                double[] ans = GorillaUtil.decompress(gorilla, ds.length);
            }
        }
    }

    @Test
    public void testTime() throws IOException, URISyntaxException, InterruptedException {
        Map<String, double[]> map = FileUtils.datas();
        Thread.sleep(1000L);
        int t = 200;
        long start, d;
        start = System.currentTimeMillis();
        for (int i = 0; i < t; i++) {
            for (Map.Entry<String, double[]> e : map.entrySet()) {
                byte[] bytes = GorillaUtil.compress(e.getValue());
                double[] doubles = GorillaUtil.decompress(bytes, e.getValue().length);
            }
        }
        d = System.currentTimeMillis() - start;
        System.out.println("Gorilla:    " + d);

        Thread.sleep(1000L);
        start = System.currentTimeMillis();
        for (int i = 0; i < t; i++) {
            for (Map.Entry<String, double[]> e : map.entrySet()) {
                byte[] bytes = DoubleCompressor.compress(e.getValue());
                double[] ans = DoubleCompressor.decompress(bytes);
            }
        }
        d = System.currentTimeMillis() - start;
        System.out.println("Monkey:    " + d);
    }
}
