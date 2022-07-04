import kernel.*;
import org.junit.jupiter.api.Test;
import plugins.DefaultParser;
import plugins.KMeansAlgorithm;
import plugins.EuclideanDistanceAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestKmeans {
    EuclideanDistanceAlgorithm singleLinkage = new EuclideanDistanceAlgorithm();
    KMeansAlgorithm kmeans = new KMeansAlgorithm();
    File iris = new File("src/main/resources/iris.data");

    @Test
    void testBasicClassConstruction() {
        KMeansAlgorithm km = new KMeansAlgorithm();
    }

    @Test
    void clusterEmptyList() {
        ArrayList<DataPoint> data = new ArrayList<>();
        EuclideanDistanceAlgorithm linkage = new EuclideanDistanceAlgorithm();
        Cluster[] newClusters = kmeans.clusterData(data, linkage);
        assertTrue(newClusters.length == 0, "No data was provided so an empty list should be returned");
    }

    @Test
    void clusterSingleValue() {
        DataPoint dp = new DataPoint("1 2 3");
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        dataPoints.add(dp);

        EuclideanDistanceAlgorithm linkage = new EuclideanDistanceAlgorithm();
        Cluster[] newClusters = kmeans.clusterData(dataPoints, linkage);
        assertTrue(newClusters[0].get(0).equals(dp), "Only single piece of data provided so output should be a single cluster");
    }

    @Test
    void generateInitialCentroidPositions() {
        Double[] input1 = {2.5, 5.0};
        Double[] input2 = {4.9, 4.3};
        Double[] input3 = {1.0, 2.0};

        String[] s = new String[0];

        DataPoint dp1 = new DataPoint("", s, input1);
        DataPoint dp2 = new DataPoint("", s, input2);
        DataPoint dp3 = new DataPoint("", s, input3);

        ArrayList<DataPoint> dpList = new ArrayList<>();
        dpList.add(dp1);
        dpList.add(dp2);
        dpList.add(dp3);

        DataPoint[] positions = kmeans.chooseInitalCentroids(dpList);

        boolean numbersInRange = true;
        for (DataPoint dp : positions) {
            if (dp != dp1 || dp != dp2 || dp != dp3) {
                numbersInRange = false;
            }
        }
        assertTrue(numbersInRange, "Each initial position should match at one of the existing disappoints");
    }

    @Test
    void testFirstClusterRound() {
        Double[] input1 = {2.5, 5.0};
        Double[] input2 = {4.9, 4.3};
        Double[] input3 = {1.0, 2.0};
        Double[] input4 = {11.0, 18.0};

        String[] s = new String[0];

        DataPoint dp1 = new DataPoint("", s, input1);
        DataPoint dp2 = new DataPoint("", s, input2);
        DataPoint dp3 = new DataPoint("", s, input3);
        DataPoint dp4 = new DataPoint("", s, input4);

        ArrayList<DataPoint> dpList = new ArrayList<>();
        dpList.add(dp1);
        dpList.add(dp2);
        dpList.add(dp3);
        dpList.add(dp4);

        DataPoint[] centroids = new DataPoint[]{dp1, dp3};

        Cluster[] output = kmeans.clusterRound(dpList, centroids, singleLinkage);

        assertTrue(output[0].size() == 3 && output[1].size() == 1,
                "first cluster will contain dp1, which is the closest centroid for dp2 and 4. Thus the second cluster should only contain dp3");
    }

    @Test
    void TestNumericalMeanCentroid(){
        Double[] input1 = {5.0, 1.0};
        Double[] input2 = {10.0, 9.0};

        String[] s = new String[0];

        DataPoint dp1 = new DataPoint("", s, input1);
        DataPoint dp2 = new DataPoint("", s, input2);
        Cluster c = new Cluster();
        c.add(dp1);
        c.add(dp2);

        Cluster[] clusters = new Cluster[]{c};

        DataPoint[] newCentroids = kmeans.calculateNewCentroids(clusters);
        assertTrue(newCentroids.length == 1 && newCentroids[0].getNumerical(0) == 7.5 && newCentroids[0].getNumerical(1) == 5.0,
               "There should only be a single centroid as there is only a single cluster, first feature should be 7.5 ((10+5)/2 = 7.5)" +
                       "and second feature should be 5 ((1+9)/2 = 5.0)");
    }

    @Test
    void testNewCentroidUsingMultipleDataPoints(){
        Double[] input1 = {2.5, 5.0, 5.0};
        Double[] input2 = {4.9, 4.3, 7.2};
        Double[] input3 = {1.0, 2.0, 20.0};
        Double[] input4 = {11.0, 18.0, 1.0};

        String[] s = new String[0];

        DataPoint dp1 = new DataPoint("", s, input1);
        DataPoint dp2 = new DataPoint("", s, input2);
        DataPoint dp3 = new DataPoint("", s, input3);
        DataPoint dp4 = new DataPoint("", s, input4);

        ArrayList<DataPoint> dpList = new ArrayList<>();
        dpList.add(dp1);
        dpList.add(dp2);
        dpList.add(dp3);
        dpList.add(dp4);

        Cluster c = new Cluster();
        c.add(dp1);
        c.add(dp2);
        c.add(dp3);
        c.add(dp4);

        Cluster[] clusters = new Cluster[]{c};

        DataPoint[] newCentroids = kmeans.calculateNewCentroids(clusters);
        assertTrue(newCentroids[0].getNumerical(0) == 4.85 && newCentroids[0].getNumerical(1) == 7.325 && newCentroids[0].getNumerical(2) == 8.3,
                "New centroid should match the mean values of all three features");
    }

    @Test
    void checkIfCentroidsNotChanged(){
        Double[] input1 = {2.5, 5.0, 5.0};
        Double[] input2 = {4.9, 4.3, 7.2};

        String[] s = new String[0];

        DataPoint dp1 = new DataPoint("", s, input1);
        DataPoint dp2 = new DataPoint("", s, input2);

        DataPoint[] oldCentroids = new DataPoint[]{dp1, dp2};
        DataPoint[] newCentroids = new DataPoint[]{dp1, dp2};

        boolean centroidsChanged = kmeans.checkForUpdatedCentroids(oldCentroids, newCentroids);

        assertTrue(centroidsChanged == false, "Identical centroids provided to function so centroids have not changed, thus expected value is false");
    }

    @Test
    void checkIfCentroidsChanged(){
        Double[] input1 = {2.5, 5.0, 5.0};
        Double[] input2 = {4.9, 4.3, 7.2};

        Double[] differnetInput = {4.9, 4.4, 7.2};

        String[] s = new String[0];

        DataPoint dp1 = new DataPoint("", s, input1);
        DataPoint dp2 = new DataPoint("", s, input2);
        DataPoint dp3 = new DataPoint("", s, differnetInput);

        DataPoint[] oldCentroids = new DataPoint[]{dp1, dp2};
        DataPoint[] newCentroids = new DataPoint[]{dp1, dp3};

        boolean centroidsChanged = kmeans.checkForUpdatedCentroids(oldCentroids, newCentroids);

        assertTrue(centroidsChanged == true, "Centroids are slightly different so function should return true");
    }

    @Test
    void testMainClusterMethod(){
        Double[] input1 = {4.5, 5.0};
        Double[] input2 = {4.9, 4.3};
        Double[] input3 = {1.0, 2.0};
        String[] catData = new String[0];

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);
        DataPoint dp3 = new DataPoint("", catData, input3);

        ArrayList<DataPoint> dpList = new ArrayList<>();
        dpList.add(dp1);
        dpList.add(dp2);
        dpList.add(dp3);

        kmeans.setnumberOfRounds(3);
        kmeans.setkClusters(2);
        Cluster[] outClusters = kmeans.clusterData(dpList,singleLinkage);

        boolean input1And2InSameCluster = false;
        if(outClusters[0].contains(dp1)){
            input1And2InSameCluster = outClusters[0].contains(dp2);
        } else {
            input1And2InSameCluster = outClusters[1].contains(dp1);
        }
        assertTrue(input1And2InSameCluster, "input 1 and 2 are closer than input 3. As there are only two clusters, " +
                "input 1 and 2 should always end up in the same cluster");
    }

    @Test
    void testClusterIris() throws IOException {
        DefaultParser DBparser = new DefaultParser();
        DBparser.setDatabase(iris);
        ArrayList<DataPoint> database = DBparser.readDB();

        kmeans.setnumberOfRounds(3);
        kmeans.setkClusters(3);

        Cluster outputClusters[] = kmeans.clusterData(database, singleLinkage);

    }




}
