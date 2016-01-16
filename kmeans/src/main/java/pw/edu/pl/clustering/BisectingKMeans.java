package pw.edu.pl.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BisectingKMeans {

    public int[] cluster(Matrix dataPoints, int numClusters) {
        // Handle a simple base case.
        if (numClusters <= 1) {
            int[] assignments = new int[dataPoints.rows()];
            for (int i = 0; i < assignments.length; ++i)
                assignments[i] = 0;
            return assignments;
        }

        // Instantiate a KMeansClustering instance which will be used to bisect
        KMeansClustering kmeans = new KMeansClustering();

        // Create a count of cluster assignments.
        int[] numAssignments = new int[numClusters];

        // Create a list of lists. The inner list represents the vectors
        // assigned to a particular cluster. We use this method so that we can
        // easily transform the cluster to a Matrix
        List<List<Vector<Double>>> clusters = new ArrayList<List<Vector<Double>>>(numClusters);
        for (int c = 0; c < numClusters; ++c)
            clusters.add(0, new ArrayList<Vector<Double>>());

        // Make the first bisection.
        int[] assignments = kmeans.cluster(dataPoints, 2);

        // Count the number of assignments made to each cluster and move the
        // vectors in to the corresponding list.
        for (int i = 0; i < assignments.length; ++i) {
            int assignment = assignments[i];
            numAssignments[assignment]++;
            clusters.get(assignment).add(dataPoints.getRowVector(i));
        }

        // Generate the numClusters - 2 clusters by finding the largest cluster
        // and bisecting it. Of the 2 resulting clusters, one will maintain the
        // same cluster index and the other will be given a new cluster index,
        // namely k, the current cluster number.
        for (int k = 2; k < numClusters; k++) {
            // Find the largest cluster.
            int largestSize = 0;
            int largestIndex = 0;
            for (int c = 0; c < numClusters; ++c) {
                if (numAssignments[c] > largestSize) {
                    largestSize = numAssignments[c];
                    largestIndex = c;
                }
            }

            // Get the list of vectors representing the cluster being split and
            // the cluster that will hold the vectors split off from this
            // cluster.
            List<Vector<Double>> originalCluster = clusters.get(largestIndex);
            List<Vector<Double>> newCluster = clusters.get(k);

            // Split the largest cluster.
            Matrix clusterToSplit = new Matrix(originalCluster);
            int[] newAssignments = kmeans.cluster(clusterToSplit, 2);

            // Clear the lists for cluster being split and the new cluster.
            // Also clear the number of assignments.
            originalCluster.clear();
            newCluster.clear();
            numAssignments[largestIndex] = 0;
            numAssignments[k] = 0;

            // Reassign data points in the largest cluster. Data points
            // assigned to the 0 cluster maintain their cluster number in the
            // real assignment list. Data points assigned to cluster 1 get the
            // new cluster number, k.
            for (int i = 0, j = 0; i < dataPoints.rows(); ++i) {
                if (assignments[i] == largestIndex) {
                    // Make the assignment for vectors that keep their
                    // assignment.
                    if (newAssignments[j] == 0) {
                        originalCluster.add(dataPoints.getRowVector(i));
                        numAssignments[largestIndex]++;
                    }
                    // Make the assignment for vectors that have changed their
                    // assignment.
                    else {
                        newCluster.add(dataPoints.getRowVector(i));
                        assignments[i] = k;
                        numAssignments[k]++;
                    }
                    j++;
                }
            }
        }
        return assignments;
    }
}
