package pw.edu.pl.clustering;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

public class KMeansClustering {


    public enum SeedAlgorithm {
        RANDOM,
    }

    /**
     * The initial property prefix.
     */
    public static final String PROPERTY_PREFIX =
        "edu.ucla.sspace.clustering.KMeasnsClustering";

    /**
     * A property for setting the {@link KMeansSeed} algorithm to use.
     */
    public static final String KMEANS_SEED_PROPERTY =
        PROPERTY_PREFIX + ".kMeansSeed";


    private static final String DEFAULT_SEED = "RANDOM";
    /**
     * A small number used to determine when the centroids have converged.
     */
    private static final double EPSILON = 1e-4;

    private static final Random random = new Random();

    private static final Logger LOGGER = 
        Logger.getLogger(KMeansClustering.class.getName());

    /**
     * Assignes data points to a cluster
     */
    public int[] cluster(Matrix dataPoints, int numClusters) {
        // Generate the initial seeds for K-Means.  We assume that the seeds
        // returned are a subset of the data points.
    	List<Vector<Double>> centroids = randomSeed(numClusters, dataPoints);

        // Initialize the assignments.
        int[] assignments = new int[dataPoints.rows()];

        // Iterate over the data points by first assigning data points to
        // existing centroids and then re-computing the centroids.  This
        // iteration will continue until the set of existing centroids and the
        // re-computed centroids do not differ by some margin of error.
        boolean converged = false;
        while (!converged) {
            System.err.println("Running one iteration of the K-Means loop");
            // Setup the new set of centroids to be emtpy vectors..
            List<Vector<Double>> newCentroids = new ArrayList<Vector<Double>>(numClusters);
            for (int c = 0; c < numClusters; ++c) {
            	Vector<Double> emptyVector = new Vector<Double>(dataPoints.columns);
            	for(int i = 0; i<emptyVector.capacity(); i++) {
            		emptyVector.add(i, 0.0);
            	}
            	newCentroids.add(c, emptyVector);
            }
            
            double[] numAssignments = new double[numClusters];

            // Iterate through each data point.  Find the nearest centroid and
            // assign the data point to that centroid.  Also recompute the
            // centroids by adding the data point to a new centroid based on the
            // assignment.
            for (int d = 0; d < dataPoints.rows(); ++d) {
                Vector<Double> dataPoint = dataPoints.getRowVector(d);

                // Find the nearest centroid.
                int nearestCentroid = -1;
                double minDistance = Double.MAX_VALUE;
                for (int c = 0; c < numClusters; ++c) {
                    double distance = distance(centroids.get(c), dataPoint);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestCentroid = c;
                    }
                }

                // Include this data point in the new centroid.
                VectorMath.add(newCentroids.get(nearestCentroid), dataPoint);
                // Make the assignment.
                assignments[d] = nearestCentroid;
                numAssignments[nearestCentroid]++;
            }

            // Determine whether or not the centroids have changed at all.  If
            // there has been no significant change, then we can stop iterating
            // and use the most recent set of assignments.  Also, to save an
            // iteration, scale the new centroids based on the number of a data
            // points assigned to it.
            double centroidDifference = 0;
            for (int c = 0; c < numClusters; ++c) {
                // Scale the new centroid.
                if (numAssignments[c] > 0) {
                	Vector<Double> vector = newCentroids.get(c);
                	VectorMath.scale(vector, numAssignments[c]);
                }

                // Compute the difference.
                centroidDifference += Math.pow(
                        distance(centroids.get(c), newCentroids.get(c)), 2);
            }
            converged = centroidDifference < EPSILON;
            centroids = newCentroids;
        }

        // Return the last set of assignments made.
        return assignments;
    }

    /**
     * Computes the euclidean distance between {@code v1} and {@code v2}.
     * {@code v1} is expected to be a vector representing a centroid and
     * {@code v2} is expected to be a data point.
     */
    public static double distance(Vector<Double> v1, Vector<Double> v2) {
    	return DistanceMeasure.euclideanDistance(v1, v2);
    }

    /**
     * Returns the centroids of a {@link Matrix} based on the assignments for
     * each data point.  This is only a helper function for users of {@link
     * KMeansClustering} so that they can reconstruct the centroids.
     */
/*    public static List<Vector<Double>> computeCentroids(Matrix dataPoints,
                                                  Assignment[] assignments,
                                                  int numCentroids) {
        DoubleVector[] centroids = new DoubleVector[numCentroids];
        double[] numAssignments = new double[numCentroids];
        for (int i = 0; i < dataPoints.rows(); ++i) {
            int assignment = assignments[i].assignments()[0];
            VectorMath.add(centroids[i], dataPoints.getRowVector(i));
            numAssignments[i]++;
        }
        for (int c = 0; c < numCentroids; ++c)
            if (numAssignments[c] > 0)
                centroids[c] = new ScaledDoubleVector(
                        centroids[c], 1/numAssignments[c]);
        return centroids;
    }*/

    /**
     * Returns the K-Means objective score of a given solution.
     */
/*    public static double computeObjective(Matrix dataPoints,
                                          DoubleVector[] centroids,
                                          Assignment[] assignments) {
        double objective = 0;
        for (int i = 0; i < dataPoints.rows(); ++i) {
            int assignment = assignments[i].assignments()[0];
            objective += distance(centroids[assignment],
                                       dataPoints.getRowVector(i));
        }
        return objective;
    }*/


    /**
     * Computes the distance between each data point and the given centroid.  If
     * {@code selectMin} is set to true, then this will only overwrite the
     * values in {@code distances} if the new distance is smaller.  Otherwise
     * the new distance will always be stored in {@code distances}.
     *
     * @param distances An array of distances that need to be updated.
     * @param selectMin Set to true a new distance must smaller than the
     *                  current values in {@code distances}.
     * @param dataPoints The set of data points.
     * @param centroid The centroid to compare against.
     */
/*    private static void computeDistances(double[] distances,
                                         boolean selectMin,
                                         Matrix dataPoints,
                                         DoubleVector centroid) {
        for (int i = 0; i < distances.length; ++i) {
            double distance = Similarity.euclideanDistance(
                    centroid, dataPoints.getRowVector(i));
            if (!selectMin || selectMin && distance < distances[i])
                distances[i] = distance;
        }
    }*/

    /**
     * Returns the sum of distances squared.
     */
/*    private static double distanceSum(double[] distances) {
        double sum = 0;
        for (double distance : distances)
            sum += Math.pow(distance, 2);
        return sum;
    }*/


    /**
     * Select seeds at random
     */
    public List<Vector<Double>> randomSeed(int numCentroids, Matrix dataPoints) {
        // Select a subset of data points to be the new centroids.
        BitSet selectedCentroids = randomDistribution(numCentroids, dataPoints.rows());
        List<Vector<Double>> centers = new ArrayList<Vector<Double>>(numCentroids);

        // Convert the selection indices into vectors.
        for (int c = 0, i = selectedCentroids.nextSetBit(0); i >= 0;
                c++, i = selectedCentroids.nextSetBit(i+1))
            centers.add(c,dataPoints.getRowVector(i));
        return centers;
    }
    
    public static BitSet randomDistribution(int valuesToSet, int range) {
        if (valuesToSet >= range)
            throw new IllegalArgumentException("too many values for range");
        BitSet values = new BitSet(range);
        // We will be setting fewer than half of the values, so set everything
        // to false, and mark true until the desired number is reached
        if (valuesToSet < (range / 2)) {
            int set = 0;
            while (set < valuesToSet) {
                int i = (int)(Math.random() * range);
                if (!values.get(i)) {
                    values.set(i, true);
                    set++;
                }
            }
        }
        // We will be setting more than half of the values, so set everything to
        // true, and mark false until the desired number is reached
        else {
            values.set(0, range, true);
            int set = range;
            while (set > valuesToSet) {
                int i = (int)(Math.random() * range);
                if (values.get(i)) {
                    values.set(i, false);
                    set--;
                }
            }
        }
        return values;
    }

}

