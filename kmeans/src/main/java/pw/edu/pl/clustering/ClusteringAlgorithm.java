package pw.edu.pl.clustering;

/**
 * Created by Artur on 2016-01-25.
 */
public interface ClusteringAlgorithm {

    int[] cluster(Matrix dataPoints, int numClusters);

    String getName();
}
