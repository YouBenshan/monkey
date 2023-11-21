package you.bs.monkey.util;

import org.junit.Assert;
import org.junit.Test;
import you.bs.monkey.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class GorillaTest {

    @Test
    public void gorilla() throws IOException, URISyntaxException {
        Map<String, double[]> map = FileUtils.datas();
        for (Map.Entry<String, double[]> e : map.entrySet()) {
            byte[] gorilla = GorillaUtil.compress(e.getValue());
            double[] ans = GorillaUtil.decompress(gorilla, e.getValue().length);
            Assert.assertArrayEquals(e.getValue(), ans, 1e-15);

        }
    }

}
