package plugins;

import com.google.auto.service.AutoService;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import kernel.Cluster;
import kernel.MeanNormalization;
import spi.ClusterAlgorithm;
import kernel.DataPoint;
import spi.DistanceAlgorithm;

import java.util.ArrayList;

/**
 * The representation of the Agglomerative clustering algorithm.
 */
@AutoService(ClusterAlgorithm.class)
public class AgglomerativeAlgorithm implements ClusterAlgorithm {
    private int roundLimit;

    /**
     * The method which implements the bulk of the clustering algorithm.
     *
     * @param inputData         the array of singleton data which is to be clustered
     * @param distanceAlgorithm the subroutine used to determine which clusters should be merged
     * @return an array representing a cluster of datapoints
     */
    public Cluster[] clusterData(ArrayList<DataPoint> inputData, DistanceAlgorithm distanceAlgorithm) {
        MeanNormalization scaler = new MeanNormalization();
        inputData = scaler.scaleDataset(inputData);

        ArrayList<Cluster> clusters = convertToSingletonClusters(inputData);
        int clusterRoundCounter = 0;


            while (clusters.size() > 1 && clusterRoundCounter < roundLimit) {

                // find the merge candidates and combine them into a single cluster
                int[] clusterToBeMerged = findMergeCandidates(clusters, distanceAlgorithm);

//                System.out.println("round " + clusterRoundCounter);
//                System.out.println(clusters.get(clusterToBeMerged[0]));
//                System.out.println(clusters.get(clusterToBeMerged[1]));

                mergeClusters(clusterToBeMerged[0], clusterToBeMerged[1], clusters);

                clusterRoundCounter++;
            }

        // return final clustered output
        Cluster[] outputClusters = new Cluster[clusters.size()];
        for (int i = 0; i < clusters.size(); i++) {
            outputClusters[i] = clusters.get(i);
        }
        return outputClusters;
    }

    /**
     * Looks through all clusters and finds the clusters with the closest data points
     *
     * @param clusters the collection of clusters in the dataset
     * @return returns the indexes of the two clusters which are to be merged
     */
    public int[] findMergeCandidates(ArrayList<Cluster> clusters, DistanceAlgorithm distanceAlgorithm) {
        double smallestPairDistance = Double.MAX_VALUE;
        int clusterCandidate1 = 0;
        int clusterCandidate2 = 1;

        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                // find min distance between the two clusters
                double minDistance = findMinDistance(clusters.get(i), clusters.get(j), distanceAlgorithm);

                // if this distance is the smallest distance found between clusters, update cluster index's and smallest distance value
                if (minDistance < smallestPairDistance) {
                    smallestPairDistance = minDistance;
                    clusterCandidate1 = i;
                    clusterCandidate2 = j;
                }
            }
        }

        int[] clustersToBeMerged = {clusterCandidate1, clusterCandidate2};
        return clustersToBeMerged;
    }

    /**
     * Finds the smallest distance between two datapoints in a cluster
     *
     * @param cluster1 the first cluster we are comparing
     * @param cluster2 the second cluster we are comparing
     * @return returns the smallest distance between the two clusters
     */
    public double findMinDistance(Cluster cluster1, Cluster cluster2, DistanceAlgorithm distanceAlgorithm) {
        //smallestDistance set to max int so any calculated distance will always be smaller
        double smallestPairDistance = Double.MAX_VALUE;
        // loop through all the values in the first cluster
        for (int i = 0; i < cluster1.size(); i++) {
            // loop through the second cluster and compare a value in the first cluster to all values in the second
            for (int j = 0; j < cluster2.size(); j++) {
                double distance = distanceAlgorithm.measureDistance(cluster1.get(i), cluster2.get(j));

                if (smallestPairDistance > distance) {
                    smallestPairDistance = distance;
                }
            }
        }
        return smallestPairDistance;
    }

    /**
     * Returns the name of the algorithm so it can be displayed as an option in the GUI
     *
     * @return the name of the algorithm
     */
    @Override
    public String getName() {
        return "Agglomerative";
    }

    /**
     * Method used to merge two clusters together and update the list of clusters accordingly.
     *
     * @param clusterOnePosition the index of the first cluster
     * @param clusterTwoPosition the index of the second cluster which is to be merged with cluster one
     * @param clusters           the list of clusters
     */
    public void mergeClusters(int clusterOnePosition, int clusterTwoPosition, ArrayList<Cluster> clusters) {
        Cluster mergeTo = clusters.get(clusterOnePosition);
        Cluster mergeFrom = clusters.get(clusterTwoPosition);

        for (int i = 0; i < mergeFrom.size(); i++) {
            mergeTo.add(mergeFrom.get(i));
        }

        clusters.remove(clusterTwoPosition);
    }

    /**
     * Takes a given array and converts it so that each datapoint is in its own Cluster. Used for algorithms like agglomerative and K-means.
     *
     * @param database the collection of datapoints which are to be clustered
     * @return the previous database such that each datapoint is nested in its own array.
     */
    public ArrayList<Cluster> convertToSingletonClusters(ArrayList<DataPoint> database) {
        ArrayList<Cluster> singletonClusters = new ArrayList<Cluster>();
        for (int i = 0; i < database.size(); i++) {
            Cluster singleton = new Cluster();
            singleton.add(database.get(i));
            singletonClusters.add(singleton);
        }
        return singletonClusters;
    }

    /**
     * Displays the user settings that will be used within the algorithm.
     */
    public void displaySettingsPopup(ArrayList<DataPoint> inputData) {
        Dialog<String[]> clusterSettingsPopup = new Dialog<>();
        clusterSettingsPopup.setTitle("Cluster Settings");
        clusterSettingsPopup.setHeaderText("Please select the following algorithm settings");

        Label roundLabel = new Label("Number of rounds:");
        TextField roundInput = new TextField();

        GridPane dialogContent = new GridPane();
        dialogContent.add(roundLabel, 1, 1);
        dialogContent.add(roundInput, 2, 1);

        ButtonType confirmButton = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        clusterSettingsPopup.getDialogPane().getButtonTypes().add(confirmButton);
        clusterSettingsPopup.getDialogPane().setContent(dialogContent);

        clusterSettingsPopup.showAndWait();

        // read the user inputs, they are not valid then display a warning and to get inputs again
        try {
            int userInput = Integer.valueOf(roundInput.getText());
            if (userInput == 0 || userInput >= inputData.size()) {
                invalidSettingInputs(inputData);
            } else {
                roundLimit = userInput;
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
        nonRoundValue.setHeaderText("Warning: invalid input");
        nonRoundValue.setContentText("Round rounder must be a number greater than 0 and less than the total number of samples");

        nonRoundValue.showAndWait();

        displaySettingsPopup(inputData);
    }

    public void setRoundLimit(int roundLimit) {
        this.roundLimit = roundLimit;
    }
}
