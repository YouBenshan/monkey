package org.bs.monkey;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FileUtils {
    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static Map<String, double[]> datas() throws IOException, URISyntaxException {
        URL url = FileUtils.class.getResource("/data");
        File[] files = Paths.get(url.toURI()).toFile().listFiles();
        Map<String, double[]> result = new HashMap<>();
        for (File f : files) {
            List<String> strs = Files.readAllLines(f.toPath());
            double[] ds = strs.stream().filter(s -> pattern.matcher(s).matches()).mapToDouble(Double::valueOf).toArray();
            result.put(f.getName(), ds);
        }
        return result;
    }

}
