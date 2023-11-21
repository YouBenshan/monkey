package you.bs.monkey.benchmark;

import org.junit.Assert;
import org.junit.Test;
import you.bs.monkey.DoubleCompressor;
import you.bs.monkey.FileUtils;
import you.bs.monkey.util.GorillaUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author You Benshan
 */
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
}
