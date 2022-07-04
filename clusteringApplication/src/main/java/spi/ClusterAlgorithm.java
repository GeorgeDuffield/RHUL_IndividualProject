package spi;

import kernel.Cluster;
import kernel.DataPoint;

import java.util.ArrayList;

/**
 * The base class which all cluster algorithms should extend.
 */
public interface ClusterAlgorithm {

    /**
     * The method which implements the bulk of the clustering algorithm.
     *
     * @param inputData the array of singleton data which is to be clustered
     * @param distanceAlgorithm the subroutine used to determine which clusters should be merged
     * @return an array representing a cluster of datapoints
     */
    public Cluster[] clusterData(ArrayList<DataPoint> inputData, DistanceAlgorithm distanceAlgorithm);

    /**
     * Returns the name of the algorithm so it can be displayed as an option in the GUI
     * @return the name of the algorithm
     */
    public String getName();

    public void displaySettingsPopup(ArrayList<DataPoint> inputData);
}
