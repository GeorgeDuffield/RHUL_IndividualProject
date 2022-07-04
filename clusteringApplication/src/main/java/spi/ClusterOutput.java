package spi;

import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import kernel.DataPoint;

import java.util.ArrayList;

public interface ClusterOutput {
    public Chart generateGraphicalOutput(ClusterAlgorithm clusterAlgorithm, ArrayList<DataPoint> inputData, DistanceAlgorithm linageMethod, Scene scene, String fileName);
    public String getName();
}
