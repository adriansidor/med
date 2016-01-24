package pw.edu.pl.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Artur on 2016-01-23.
 */
public class PAMClustering {

    /**
     * Assignes data points to a cluster
     */
    public int[] cluster(Matrix dataPoints, int numClusters) {
        Integer[] medoids = chooseRandomPointsAsMedoids(dataPoints.rows(), numClusters);
        Assignment[] assignments = assign(medoids, dataPoints);

        double cost = Double.MAX_VALUE;
        double newCost = calculateAssignmentCost(assignments);


        while(newCost < cost) {
            System.err.println("Running one iteration of the PAM loop");

            cost = newCost;
            List<Integer> newMedoids = new ArrayList<>();
            for (int medoidIndex = 0; medoidIndex < medoids.length; medoidIndex++) {
                int bestSwap = medoids[medoidIndex];
                double minCost = Double.MAX_VALUE;
                Integer[] tmpMedoids = Arrays.copyOf(medoids, medoids.length);
                for (int rowIndex = 0; rowIndex < dataPoints.rows(); rowIndex++) {
                    if (arrayContains(medoids, rowIndex))
                        continue;
                    swapMedoid(tmpMedoids, medoidIndex, rowIndex);
                    double assignmentCost = calculateAssignmentCost(
                            assign(newMedoids.toArray(new Integer[newMedoids.size()]), dataPoints));
                    if (assignmentCost < minCost) {
                        minCost = assignmentCost;
                        bestSwap = rowIndex;
                    }
                }
                newMedoids.add(bestSwap);
            }
            newCost = calculateAssignmentCost(
                            assign(newMedoids.toArray(new Integer[newMedoids.size()]), dataPoints));
            medoids = newMedoids.toArray(new Integer[newMedoids.size()]);
        }

        // Return the last set of assignments made.
        return convertAssignmentsToArray(assign(medoids, dataPoints));
    }

    private int[] convertAssignmentsToArray(Assignment[] assign) {
        int[] result = new int[assign.length];
        for (int assignIndex = 0; assignIndex < assign.length; assignIndex++) {
            result[assignIndex] = assign[assignIndex].getMedoid();
        }
        return result;
    }

    private boolean arrayContains(Integer[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                return true;
            }
        }
        return false;
    }

    private void swapMedoid(Integer[] newMedoids, int medoidIndex, int rowIndex) {
        newMedoids[medoidIndex] = rowIndex;
    }

    private double calculateAssignmentCost(Assignment[] assignments) {
        double cost = 0;
        for(Assignment assignment : assignments) {
            cost += assignment.getDistance();
        }
        return cost;
    }

    private Assignment[] assign(Integer[] medoids, Matrix dataPoints) {
        Assignment[] result = new Assignment[dataPoints.rows()];
        for (int rowIndex = 0; rowIndex < dataPoints.rows(); rowIndex++) {
            double minDistanceToMedoid = Double.MAX_VALUE;
            int selectedMedoid = 0;
            for (int medoidIndex = 0; medoidIndex < medoids.length; medoidIndex++) {
                if (rowIndex == medoids[medoidIndex]) {
                    continue;
                }
                double distance = KMeansClustering.distance(dataPoints.getRowVector(medoids[medoidIndex]),
                                                            dataPoints.getRowVector(rowIndex));
                if (distance < minDistanceToMedoid) {
                    minDistanceToMedoid = distance;
                    selectedMedoid = medoidIndex;
                }
            }
            result[rowIndex] = new Assignment(selectedMedoid, minDistanceToMedoid);
        }
        return result;
    }

    private Integer[] chooseRandomPointsAsMedoids(int pointNumber,
                                                 int numClusters) {
        List<Integer> indices = IntStream.range(0, pointNumber)
                .boxed().collect(Collectors.toList());
        Collections.shuffle(indices);
        List<Integer> result = indices.subList(0, numClusters);
        result.sort(Integer::min);
        return result.toArray(new Integer[result.size()]);
    }

    private class Assignment {
        private final int medoid;
        private final double distance;

        public Assignment(int medoid, double distance) {
            this.medoid = medoid;
            this.distance = distance;
        }

        public int getMedoid() {
            return medoid;
        }

        public double getDistance() {
            return distance;
        }
    }
}
