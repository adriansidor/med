import org.junit.Test;
import pw.edu.pl.clustering.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;

/**
 * Created by Artur on 2016-01-25.
 */
public class ComparisonTests {

    private static final String filename = "resources/iris.data";

    private static final String separator = ",";

    @Test
    public void comparisonTests() throws IOException {
        File file = new File(filename);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        List<Vector<Double>> vectors = new ArrayList<Vector<Double>>();
        List<Integer> oc = new ArrayList<Integer>();
        while( (line = br.readLine()) != null ) {
            String[] values = line.split(separator);
            Vector<Double> vector = new Vector<Double>(values.length-1);
            for(int i = 0; i<values.length-1; i++) {
                vector.add(i, Double.valueOf(values[i]));
            }
            vectors.add(vector);

            String clazz = values[values.length-1];
            if(clazz.equals("Iris-setosa")) {
                oc.add(0);
            } else if(clazz.equals("Iris-versicolor")) {
                oc.add(1);
            } else {
                oc.add(2);
            }
        }
        br.close();
        fr.close();
        Matrix matrix = new Matrix(vectors);

        List<ClusteringAlgorithm> algorithms = new ArrayList<>();
        algorithms.add(new KMeansClustering());
        algorithms.add(new BisectingKMeans());
        algorithms.add(new PAMClustering());
        algorithms.add(new BisectingPAM());

        RandIndex randIndexer = new RandIndex();
        for (ClusteringAlgorithm algorithm : algorithms) {
            List<Double> randIndexes = new ArrayList<>();git
            IntStream.range(0, 100).forEach(i -> {
                int[] clusters = algorithm.cluster(matrix, 3);
                randIndexes.add(
                        randIndexer.calculate(oc.toArray(new Integer[oc.size()]),
                                              clusters));
            });
            Statistics statistics = new Statistics(randIndexes);
            System.out.println("Algorithm: " + algorithm.getName());
            System.out.println("Mean of Rand index: " + statistics.getMean());
            System.out.println("Standard deviation of Rand index: " + statistics.getStandardDeviation() + "\n");
        }
    }

    private class Statistics {
        private double mean;
        private double standardDeviation;

        public Statistics(List<Double> values) {
            int valueCount = 0;
            double sum = 0;
            for (Double value : values) {
                valueCount++;
                sum += value;
            }
            mean = sum / valueCount;

            double sumOfSquaredDeviations = 0;
            for (Double value : values) {
                sumOfSquaredDeviations += Math.pow(value - mean, 2);
            }
            double variance = sumOfSquaredDeviations/valueCount;
            standardDeviation = Math.sqrt(variance);
        }

        public double getMean() {
            return mean;
        }

        public double getStandardDeviation() {
            return standardDeviation;
        }
    }
}
