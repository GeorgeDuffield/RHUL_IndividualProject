package plugins;

import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import kernel.*;
import spi.ClusterAlgorithm;
import spi.ClusterOutput;
import spi.DistanceAlgorithm;

import java.util.ArrayList;

/**
 * The scatter graph chart which is used to display cluster data.
 */
public class ScatterGraph implements ClusterOutput {
    /**
     * Creates a scatter chart based on the information provided by the user.
     *
     * @param clusterAlgorithm  the algorithm which is to be used to cluster the data
     * @param inputData         the data which is to be clustered
     * @param distanceAlgorithm the algorithm used to calculate the distance between datapoints
     * @param scene             the scene which will display the output information
     * @param fileName          the name of the file which will be used to label the chart
     * @return returns the scatter chart which has been populated with cluster data
     */
    @Override
    public Chart generateGraphicalOutput(ClusterAlgorithm clusterAlgorithm, ArrayList<DataPoint> inputData, DistanceAlgorithm distanceAlgorithm, Scene scene, String fileName) {
        clusterAlgorithm.displaySettingsPopup(inputData);
        Cluster[] clusters = clusterAlgorithm.clusterData(inputData, distanceAlgorithm);

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Feature 1");
        yAxis.setLabel("Feature 2");

        ScatterChart scatterChart = new ScatterChart(xAxis, yAxis);
        scatterChart.setTitle(fileName);

        for (int i = 0; i < clusters.length; i++) {
            XYChart.Series clusterSeries = new XYChart.Series();
            clusterSeries.setName("Cluster " + (i + 1));
            for (int j = 0; j < clusters[i].size(); j++) {
                double feature1 = clusters[i].get(j).getOriginalNumerical(0);
                double feature2 = clusters[i].get(j).getOriginalNumerical(1);
                clusterSeries.getData().add(new XYChart.Data(feature1, feature2));
            }
            scatterChart.getData().add(clusterSeries);
        }
        return scatterChart;
    }

    @Override
    public String getName() {
        return "Scatter chart";
    }
}
