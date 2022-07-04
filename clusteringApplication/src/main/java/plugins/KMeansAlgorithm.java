package plugins;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import kernel.Cluster;
import kernel.MeanNormalization;
import spi.ClusterAlgorithm;
import kernel.DataPoint;
import com.google.auto.service.AutoService;
import spi.DistanceAlgorithm;

import java.util.ArrayList;
import java.util.Random;

/**
 * The algorithm that provides K-means clustering. This class also acts as an example of a cluster algorithm plugin.
 */
@AutoService(ClusterAlgorithm.class)
public class KMeansAlgorithm implements ClusterAlgorithm {

    private int kClusters;
    private int numberOfRounds;


    /**
     * The method which implements the bulk of the clustering algorithm.
     *
     * @param inputData         the collection of data which is to be clustered
     * @param distanceAlgorithm the subroutine used to determine which clusters should be merged
     * @return an array representing a cluster of datapoints
     */
    @Override
    public Cluster[] clusterData(ArrayList<DataPoint> inputData, DistanceAlgorithm distanceAlgorithm) {
        Cluster[] outputClusters = new Cluster[kClusters];

        // if data is empty or only has a single value then there is no clustering to perform
        if (inputData.isEmpty()) {
            outputClusters = new Cluster[0];
            return outputClusters;
        } else if (inputData.size() == 1) {
            Cluster singleValueCluster = new Cluster(inputData.get(0));
            outputClusters = new Cluster[]{singleValueCluster};
            return outputClusters;
        }

        MeanNormalization scaler = new MeanNormalization();
        inputData = scaler.scaleDataset(inputData);
        DataPoint[] centroidPositions = chooseInitalCentroids(inputData);

        for (DataPoint dp : inputData) {
            double minDistance = Double.MAX_VALUE;
            for (DataPoint centroid : centroidPositions) {
                //check to see if datapoint is already in the same position as the centroid to prevent performing unnecessary distance calculations
                if (centroid.equals(dp)) {
                    minDistance = 0;
                } else {
                    double distance = distanceAlgorithm.measureDistance(dp, centroid);
                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                }
            }
        }

        outputClusters = clusterRound(inputData, centroidPositions, distanceAlgorithm);
        for (int i = 0; i < numberOfRounds; i++) {
            DataPoint[] newCentroidPositions = calculateNewCentroids(outputClusters);
            if (checkForUpdatedCentroids(centroidPositions, newCentroidPositions)) {
                centroidPositions = newCentroidPositions;
                clusterRound(inputData, centroidPositions, distanceAlgorithm);
            } else {
                return outputClusters;
            }
        }

        return outputClusters;
    }

    /**
     * Returns the name of the algorithm so it can be displayed as an option in the GUI
     *
     * @return the name of the algorithm
     */
    @Override
    public String getName() {
        return "K-means";
    }

    /**
     * Represents a single round of K means clustering
     *
     * @param inputData         the collection of data that is to be clustered
     * @param centroidPositions the cluster centroids
     * @param distanceAlgorithm method for determining distance
     * @return returns the data points grouped into k clusters
     */
    public Cluster[] clusterRound(ArrayList<DataPoint> inputData, DataPoint[] centroidPositions, DistanceAlgorithm distanceAlgorithm) {
        Cluster[] clusters = new Cluster[centroidPositions.length];
        //initialise array of clusters
        for (int i = 0; i < centroidPositions.length; i++) {
            clusters[i] = new Cluster();
        }

        // for ever data point, find the closest centroid
        for (DataPoint dp : inputData) {
            double minDistance = Double.MAX_VALUE;
            int centroidCandidate = 0;
            //compare distance for all centroids
            for (int i = 0; i < centroidPositions.length; i++) {
                //check to see if datapoint is already in the same position as the centroid to prevent performing unnecessary distance calculations
                if (centroidPositions[i].equals(dp)) {
                    minDistance = 0;
                    centroidCandidate = i;
                    // centroid and data point are in the same position and thus no other centroid can be closer so move onto next datapoint
                    break;
                } else {
                    double distance = distanceAlgorithm.measureDistance(dp, centroidPositions[i]);
                    //if the distance is smaller than the previous min distance, update min distance to reflect the closest centroid found so far
                    if (distance < minDistance) {
                        minDistance = distance;
                        centroidCandidate = i;
                    }
                }
            }
            clusters[centroidCandidate].add(dp);
        }
        return clusters;
    }

    /**
     * Method for randomly choosing the initial centroid positions from the list of data points
     *
     * @param inputData the collection of data supplied for clustering
     * @return the starting centroid positions that will be used in the first round of clustering
     */
    public DataPoint[] chooseInitalCentroids(ArrayList<DataPoint> inputData) {
        // array list used to efficiently check if position candidates are already in the array
        ArrayList<Integer> positions = new ArrayList<Integer>();
        Random random = new Random();

        // loop k times and choose a starting position from the dataset
        for (int i = 0; i < kClusters; i++) {
            int positionCandidate = random.nextInt(inputData.size());

            // check position has already been chosen, if so generate new candidates until a unique value is found
            while (positions.contains(positionCandidate)) {
                positionCandidate = random.nextInt(inputData.size());
            }
            positions.add(positionCandidate);
        }

        // convert positions into data points
        DataPoint[] centroidStartingPositions = new DataPoint[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            centroidStartingPositions[i] = inputData.get(positions.get(i));
        }
        return centroidStartingPositions;
    }

    /**
     * Calculate new centroids based on the mean values of a given cluster
     *
     * @param clusters groups of data points belonging to a cluster
     * @return returns an array of data points which represent the new centroid positions
     */
    public DataPoint[] calculateNewCentroids(Cluster[] clusters) {
        DataPoint[] newCentroids = new DataPoint[clusters.length];

        // for each cluster
        for (int i = 0; i < clusters.length; i++) {
            // sum up features for all data points in cluster
            if (clusters[i].size() > 1) {
                newCentroids[i] = sumFeaturesValues(clusters[i].get(0), clusters[i].get(1));
                for (int j = 2; j < clusters[i].size(); j++) {
                    newCentroids[i] = sumFeaturesValues(newCentroids[i], clusters[i].get(j));
                }
                // divide by number of data points to get the mean
                for (int k = 0; k < clusters[i].get(0).sizeNumerical(); k++) {
                    double meanFeatureValue = newCentroids[i].getNumerical(k) / clusters[i].size();
                    newCentroids[i].setNumerical(k, meanFeatureValue);
                }
                // only one datapoint in cluster so centroid remains the same
            } else {
                newCentroids[i] = clusters[i].get(0);
            }
        }
        return newCentroids;
    }

    /**
     * Takes two data points and adds their features
     *
     * @param centroid  the centroid we are calculating
     * @param dataPoint the datapoint which we are adding to the centroid total
     * @return returns a datapoint contain the combined features of both data points
     */
    public DataPoint sumFeaturesValues(DataPoint centroid, DataPoint dataPoint) {
        DataPoint newCentroidValue = new DataPoint();
        for (int i = 0; i < centroid.sizeNumerical(); i++) {
            double newValue = centroid.getNumerical(i) + dataPoint.getNumerical(i);
            newCentroidValue.addNumerical(newValue);
        }
        return newCentroidValue;
    }

    /**
     * Checks to see if any of the centroid possitions have changed since the last round of clustering.
     *
     * @param oldCentroids the previous centroid positions
     * @param newCentroids the newly calculated centroid positions
     * @return the boolean value for if any of the centroid positions have changed from the last round of clustering
     */
    public boolean checkForUpdatedCentroids(DataPoint[] oldCentroids, DataPoint[] newCentroids) {
        boolean changed = false;
        for (int i = 0; i < oldCentroids.length; i++) {
            if (!(oldCentroids[i].equals(newCentroids[i]))) {
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Displays the user settings that will be used within the algorithm.
     */
    public void displaySettingsPopup(ArrayList<DataPoint> inputData) {
        Dialog<String[]> clusterSettingsPopup = new Dialog<>();
        clusterSettingsPopup.setTitle("Cluster Settings");
        clusterSettingsPopup.setHeaderText("Please select the following algorithm settings");

        Label roundLabel = new Label("Number of rounds:");
        Label clusterNumberLabel = new Label("Number of clusters: ");
        TextField roundInput = new TextField();
        TextField clusterNumberInput = new TextField();

        GridPane dialogContent = new GridPane();
        dialogContent.add(roundLabel, 1, 1);
        dialogContent.add(roundInput, 2, 1);

        dialogContent.add(clusterNumberLabel, 1, 2);
        dialogContent.add(clusterNumberInput, 2, 2);

        ButtonType confirmButton = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        clusterSettingsPopup.getDialogPane().getButtonTypes().add(confirmButton);
        clusterSettingsPopup.getDialogPane().setContent(dialogContent);


        clusterSettingsPopup.showAndWait();


        // try to read the user input as a number, if this fails then print an error and try again
        try {
            int clusterChoice = Integer.valueOf(clusterNumberInput.getText());
            int roundChoice = Integer.valueOf(roundInput.getText());
            if (clusterChoice == 0 || clusterChoice >= inputData.size() || roundChoice == 0) {
                invalidSettingInputs(inputData);
            } else {
                kClusters = clusterChoice;
                numberOfRounds = roundChoice;
            }
        } catch (NumberFormatException e) {
            invalidSettingInputs(inputData);
        }
    }

    /**
     * Displays a warning to the user when invalid inputs are provided at the cluster setting screen
     */
    public void invalidSettingInputs(ArrayList<DataPoint> inputData) {
        Alert nonRoundValue = new Alert(Alert.AlertType.ERROR);
        nonRoundValue.setTitle("Error!");
        nonRoundValue.setHeaderText("Warning: Invalid inputs");
        nonRoundValue.setContentText("Inputs must be greater than 0 and the cluster number must be less than the number of samples");

        nonRoundValue.showAndWait();

        displaySettingsPopup(inputData);
    }

    public int getkClusters() {
        return kClusters;
    }

    public void setkClusters(int kClusters) {
        this.kClusters = kClusters;
    }

    public int getnumberOfRounds() {
        return numberOfRounds;
    }

    public void setnumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }


}
