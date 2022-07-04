package plugins;

import kernel.Cluster;
import kernel.DataPoint;
import org.apache.commons.text.similarity.LevenshteinDistance;
import spi.DistanceAlgorithm;

import java.util.ArrayList;

/**
 * Classed used to determine the euclidean distance between two given datapoints.
 */
public class EuclideanDistanceAlgorithm implements DistanceAlgorithm {

    /**
     * Measure the Euclidean distance between two datapoints.
     *
     * @param datapoint1 - the first datapoint which we are measuring from
     * @param datapoint2 - the datapoint we are measuring to
     * @return returns the distance between the two datapoints
     */
    public double measureDistance(DataPoint datapoint1, DataPoint datapoint2) {
        double squaredTotalDistance = 0;

        // for each dimension find the squired distance between the two dimensions and add it to the total sum
        // start by calculating distance between numerical values
        for (int i = 0; i < datapoint1.sizeNumerical(); i++) {
            squaredTotalDistance += Math.pow((datapoint1.getNumerical(i) - datapoint2.getNumerical(i)), 2);
        }

        // calculate difference between categorical data and add to total distance
        LevenshteinDistance distance = new LevenshteinDistance();
        for (int i = 0; i < datapoint1.sizeCategorical(); i++) {
            int diff = distance.apply(datapoint1.getCategorical(i), datapoint2.getCategorical(i));
            squaredTotalDistance += Math.pow((diff), 2);
        }

        // squire root is applied before returning so that proper distance measurement is returned
        return Math.sqrt(squaredTotalDistance);
    }

    @Override
    public String getName() {
        return "Euclidean Distance";
    }
}
