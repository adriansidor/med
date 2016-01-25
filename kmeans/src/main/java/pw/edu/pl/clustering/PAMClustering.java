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
public class PAMClustering implements ClusteringAlgorithm {

    /**
     * Assignes data points to a cluster
     */
    public int[] cluster(Matrix dataPoints, int numClusters) {
        Integer[] medoids = chooseRandomPointsAsMedoids(dataPoints.rows(), numClusters);
        Assignment[] assignments = assign(medoids, dataPoints);
        List<List<Integer>> clusters = getClusters(assignments, numClusters);

        double cost = Double.MAX_VALUE;
        double newCost = calculateAssignmentCost(assignments);


        while(newCost < cost) {
            //System.err.println("Running one iteration of the PAM loop");

            cost = newCost;
            List<Integer> newMedoids = new ArrayList<>();
            for (int medoidIndex = 0; medoidIndex < medoids.length; medoidIndex++) {
                int bestSwap = medoids[medoidIndex];
                double minCost = Double.MAX_VALUE;
                Integer[] tmpMedoids = Arrays.copyOf(medoids, medoids.length);
                for (Integer clusterRowIndex : clusters.get(medoidIndex)) {
                    if (arrayContains(medoids, clusterRowIndex))
                        continue;
                    swapMedoid(tmpMedoids, medoidIndex, clusterRowIndex);
                    double assignmentCost = calculateAssignmentCost(
                            assign(newMedoids.toArray(new Integer[newMedoids.size()]), dataPoints));
                    if (assignmentCost < minCost) {
                        minCost = assignmentCost;
                        bestSwap = clusterRowIndex;
                    }
                }
                newMedoids.add(bestSwap);
            }
            assignments = assign(newMedoids.toArray(new Integer[newMedoids.size()]), dataPoints);
            newCost = calculateAssignmentCost(assignments);
            clusters = getClusters(assignments, numClusters);
            medoids = newMedoids.toArray(new Integer[newMedoids.size()]);
        }

        // Return the last set of assignments made.
        return convertAssignmentsToArray(assign(medoids, dataPoints));
    }

    @Override
    public String getName() {
        return "PAM";
    }

    protected List<List<Integer>> getClusters(Assignment[] assignments, int numClusters) {
        List<List<Integer>> result = new ArrayList<>();
        for (int clusterIndex = 0; clusterIndex < numClusters; clusterIndex++) {
            result.add(new ArrayList<>());
        }
        for (int rowIndex = 0; rowIndex < assignments.length; rowIndex++) {
            result.get(assignments[rowIndex].getMedoid()).add(rowIndex);
        }
        return result;
    }

    protected int[] convertAssignmentsToArray(Assignment[] assign) {
        int[] result = new int[assign.length];
        for (int assignIndex = 0; assignIndex < assign.length; assignIndex++) {
            result[assignIndex] = assign[assignIndex].getMedoid();
        }
        return result;
    }

    protected boolean arrayContains(Integer[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                return true;
            }
        }
        return false;
    }

    protected void swapMedoid(Integer[] newMedoids, int medoidIndex, int rowIndex) {
        newMedoids[medoidIndex] = rowIndex;
    }

    protected double calculateAssignmentCost(Assignment[] assignments) {
        double cost = 0;
        for(Assignment assignment : assignments) {
            cost += assignment.getDistance();
        }
        return cost;
    }

    protected Assignment[] assign(Integer[] medoids, Matrix dataPoints) {
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

    protected Integer[] chooseRandomPointsAsMedoids(int pointNumber,
                                                 int numClusters) {
        List<Integer> indices = IntStream.range(0, pointNumber)
                .boxed().collect(Collectors.toList());
        Collections.shuffle(indices);
        List<Integer> result = indices.subList(0, numClusters);
        result.sort(Integer::min);
        return result.toArray(new Integer[result.size()]);
    }

    protected class Assignment {
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
