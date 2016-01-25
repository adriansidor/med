package pw.edu.pl.clustering;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artur on 2016-01-25.
 */
public class RandIndex {

    public double calculate(Integer[] baseClustering, int[] ratedClustering) {
        if (baseClustering.length == 0
            || baseClustering.length != ratedClustering.length) {
            throw new IllegalArgumentException("Invalid clustering sizes!");
        }

        List<Pair> pairs = generatePairs(baseClustering.length);
        int agreements = 0;
        int iterations = 0;
        for (Pair pair : pairs) {
            int first = pair.first;
            int second = pair.second;
            if (baseClustering[first] == baseClustering[second]
                    && ratedClustering[first] == ratedClustering[second]
                || baseClustering[first] != baseClustering[second]
                    && ratedClustering[first] != ratedClustering[second]) {
                agreements++;
            }
            iterations++;
        }
        return ((double)agreements)/iterations;
    }

    private List<Pair> generatePairs(int length) {
        List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            for (int j = i; j < length; j++) {
                pairs.add(new Pair(i,j));
            }
        }
        return pairs;
    }

    private class Pair {
        private int first;
        private int second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public int getFirst() {
            return first;
        }

        public int getSecond() {
            return second;
        }
    }
}
