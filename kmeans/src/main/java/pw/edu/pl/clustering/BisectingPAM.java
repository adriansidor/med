package pw.edu.pl.clustering;

/**
 * Created by Artur on 2016-01-24.
 */
public class BisectingPAM extends BisectingKMeans {

    @Override
    protected ClusteringAlgorithm getClusterer() {
        return new PAMClustering();
    }

    @Override
    public String getName() {
        return "Bisecting PAM";
    }
}
