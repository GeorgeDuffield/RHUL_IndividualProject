package spi;
import kernel.DataPoint;

/**
 * Interface used to standardise the various distance algorithms that may be used within the project.
 */
public interface DistanceAlgorithm {
    public double measureDistance(DataPoint datapoint1, DataPoint datapoint2);
    public String getName();
}
