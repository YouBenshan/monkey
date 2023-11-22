package io.github.youbenshan.monkey.benchmark;

import io.github.youbenshan.monkey.DoubleCompressor;
import io.github.youbenshan.monkey.util.GorillaUtil;
import org.openjdk.jmh.annotations.*;
import io.github.youbenshan.monkey.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author You Benshan
 */
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
public class ThrouphputBenchmark {
    @Param({"100000"})
    public int amountOfPoints;
    public double[] uncompressedValues;
    Map<String, double[]> map;

    @Setup
    public void setup() throws IOException, URISyntaxException {
        uncompressedValues = new double[amountOfPoints];
        for (int i = 0; i < amountOfPoints; i++) {
            uncompressedValues[i] = i;
        }
        map = FileUtils.datas();
    }

    @Benchmark
    @OperationsPerInvocation(100000)
    public void monkeyCompress() {
        byte[] bytes = DoubleCompressor.compress(uncompressedValues);
        DoubleCompressor.decompress(bytes);
//        System.out.println("Monkey: "+bytes.length);
    }

    @Benchmark
    @OperationsPerInvocation(100000)
    public void gorillaCompress() {
        byte[] bytes = GorillaUtil.compress(uncompressedValues);
        GorillaUtil.decompress(bytes, uncompressedValues.length);
//        System.out.println("Gorilla: "+ bytes.length);
    }

    @Benchmark
    public void monkeyFileTest() {
        for (double[] ds : map.values()) {
            byte[] bytes = DoubleCompressor.compress(ds);
            DoubleCompressor.decompress(bytes);
        }
    }


    @Benchmark
    public void gorillaFileTest() {
        for (double[] ds : map.values()) {
            byte[] bytes = GorillaUtil.compress(ds);
            GorillaUtil.decompress(bytes, ds.length);
        }
    }


}
