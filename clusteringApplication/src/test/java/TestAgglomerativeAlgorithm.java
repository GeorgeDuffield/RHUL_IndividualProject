import kernel.*;
import org.junit.jupiter.api.Test;
import plugins.DefaultParser;
import spi.DistanceAlgorithm;
import plugins.AgglomerativeAlgorithm;
import plugins.EuclideanDistanceAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAgglomerativeAlgorithm {
    AgglomerativeAlgorithm aggCluster = new AgglomerativeAlgorithm();
    DistanceAlgorithm euclideanDistance = new EuclideanDistanceAlgorithm();

    // empty string provided for original tests where only numerical data was used
    String[] catData = new String[0];
    File iris = new File("src/main/resources/iris.data");

    // test 1
    @Test
    void testCreateAggloCluster() {
        AgglomerativeAlgorithm AgglomerativeAlgorithm = new AgglomerativeAlgorithm();
    }

    //test 2
    @Test
    void testBaseCase() {
        ArrayList<Cluster> clusters = new ArrayList<Cluster>();
        Double[] input = {2.0, 4.0};
        DataPoint dp = new DataPoint("", catData, input);

        ArrayList<DataPoint> dpList = new ArrayList<>();
        dpList.add(dp);

        Cluster[] clusteredOutput = aggCluster.clusterData(dpList, euclideanDistance);
        assertTrue(clusteredOutput[0].get(0).equals(dp), "Only single value input so no data to cluster");
    }


    //test 3
    @Test
    void testMergeClusters() {
        ArrayList<Cluster> clusters = new ArrayList<Cluster>();
        Double[] input1 = {5.0, 9.0, 11.0, 20.0};
        Double[] input2 = {10.0, 50.0, 2.0, 7.0};

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);

        Cluster cluster1 = new Cluster(dp1);
        Cluster cluster2 = new Cluster(dp2);

        clusters.add(cluster1);
        clusters.add(cluster2);

        aggCluster.mergeClusters(0, 1, clusters);

        Cluster mergedCluster = clusters.get(0);

        assertTrue(mergedCluster.get(0).equals(dp1) && mergedCluster.get(1).equals(dp2),
                "Clusters have been merged and so both datapoints should be in the first cluster");
    }

    // test 4
    @Test
    void testTwoValues() {
        ArrayList<Cluster> clusters = new ArrayList<Cluster>();
        Double[] input1 = {2.0};
        Double[] input2 = {4.0};

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);

        ArrayList<DataPoint> dpList = new ArrayList<>();
        dpList.add(dp1);
        dpList.add(dp2);

        aggCluster.setRoundLimit(1);
        Cluster[] clusteredOutput = aggCluster.clusterData(dpList, euclideanDistance);

        assertTrue(clusteredOutput[0].get(0).equals(dp1) && clusteredOutput[0].get(1).equals(dp2), "Two values should be are the only two which " +
                "can be chosen for clustering and thus should be returned as a single cluster");
    }

    // test 5
    @Test
    void testThreeValues() {
        Double[] input1 = {2.0};
        Double[] input2 = {4.0};
        Double[] input3 = {10.0};

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);
        DataPoint dp3 = new DataPoint("", catData, input3);

        ArrayList<DataPoint> dpList = new ArrayList<>();
        dpList.add(dp1);
        dpList.add(dp2);
        dpList.add(dp3);

        aggCluster.setRoundLimit(2);
        Cluster[] clusteredOutput = aggCluster.clusterData(dpList, euclideanDistance);

        DataPoint[] dps = new DataPoint[]{dp1, dp2, dp3};
        Cluster predicted = new Cluster(dps);

        assertTrue(clusteredOutput[0].get(0).equals(dp1) && clusteredOutput[0].get(1).equals(dp2),
                "smallest distance is between 2 and 4 so they should be merged first, " + "the resulting cluster should then be merged with cluster 3");
    }

    // This test was made redundant when implementing multidimensional data but has been left to show the progression of the algorithms development
//    @Test
//    void testMultiSizedArrays(){
//        ArrayList<int[]> data = new ArrayList<int[]>();
//        int[] input1 = {2,5,10};
//        int[] input2 = {15, 20};
//        int[] input3 = {3, 50, 100};
//
//        data.add(input1);
//        data.add(input2);
//        data.add(input3);
//
//        int[] clusteredOutput = aggCluster.clusterData(data);
//        int[] predictedCluster = {2, 5, 10, 3, 50, 100, 15, 20};
//        assertTrue(Arrays.equals(clusteredOutput, predictedCluster), "Cluster one should be merged with cluster three and then cluster 2");
//    }

    // test 6
    // This test was added to ensure that the application would work with decimal numbers as previous implementation used ints
    @Test
    void testTwoDecimalClusters() {
        Double[] input1 = {2.5};
        Double[] input2 = {4.9};

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);

        ArrayList<DataPoint> dpList = new ArrayList<>();
        dpList.add(dp1);
        dpList.add(dp2);

        aggCluster.setRoundLimit(1);
        Cluster[] clusteredOutput = aggCluster.clusterData(dpList, euclideanDistance);

        assertTrue(clusteredOutput[0].get(0).equals(dp1) && clusteredOutput[0].get(1).equals(dp2), "Only two clusters to be merged thus output should be {{2.5},{4.9}}");
    }

    // test 7
    // This test was added to ensure that the application would work with decimal numbers as previous implementation used ints
    @Test
    void testTwoDimentionDatapoints() {
        Double[] input1 = {2.5, 5.0};
        Double[] input2 = {4.9, 4.3};
        Double[] input3 = {1.0, 2.0};

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);
        DataPoint dp3 = new DataPoint("", catData, input3);

        ArrayList<DataPoint> dpList = new ArrayList<>();
        dpList.add(dp1);
        dpList.add(dp2);
        dpList.add(dp3);

        aggCluster.setRoundLimit(2);
        Cluster[] clusteredOutput = aggCluster.clusterData(dpList, euclideanDistance);
        double[][] predictedCluster = {{2.5, 5}, {4.9, 4.3}, {1, 2.0}};

        assertTrue(clusteredOutput[0].get(0).equals(dp1) && clusteredOutput[0].get(1).equals(dp2) && clusteredOutput[0].get(2).equals(dp3), "first dp is closest to dp2 so datapoint order in the cluster should be dp1, dp2, dp3");
    }


    // test 8
    @Test
    void clusterIris() throws IOException {
        DefaultParser DBparser = new DefaultParser();
        File iris2 = new File("src/main/resources/iris3.csv");
        DBparser.setDatabase(iris2);
        ArrayList<DataPoint> database = DBparser.readDB();

        aggCluster.setRoundLimit(149);
        Cluster[] clusteredIris = aggCluster.clusterData(database, euclideanDistance);
        assertTrue(clusteredIris[0].size() == 150, "After being turned into singletons, the array returned should represent a single cluster containing the 150 datapoints");
    }

    // test 9
    @Test
    void testIndividualClusters() throws IOException {
        DefaultParser DBparser = new DefaultParser();
        DBparser.setDatabase(iris);
        ArrayList<DataPoint> database = DBparser.readDB();
        ArrayList<Cluster> singletonDB = aggCluster.convertToSingletonClusters(database);
        Double[] irisFirstLine = {5.1, 3.5, 1.4, 0.2};

        DataPoint testLine = singletonDB.get(0).get(0);
        Double[] firstLineValues = new Double[4];

        for(int i = 0; i < testLine.sizeNumerical(); i++){
            firstLineValues[i] = testLine.getNumerical(i);
        }

        assertTrue(Arrays.equals(irisFirstLine, firstLineValues), "The first line of iris should remain the same, " +
                "the only differenece being that is nested in its own cluster");
    }

    // The following tests have been imported as part of removing the previous single linkage class

    //test 10
    @Test
    void testSingleMinDistance() {
        Double[] input1 = {5.0, 5.0};
        Double[] input2 = {10.0, 10.0};

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);

        Cluster cluster1 = new Cluster(dp1);
        Cluster cluster2 = new Cluster(dp2);

        double minDistance = Math.round(aggCluster.findMinDistance(cluster1, cluster2, euclideanDistance) * 100.0) / 100.0;
        assertEquals(7.07, minDistance, "only two values so the distance is the squared root of (5^2 + 5^2) ie 7.07 when rounded");
    }

    //test 11
    @Test
    void testMultiDataMinDistance() {
        Double[] input1 = {5.0, 9.0};
        Double[] input2 = {11.0, 20.0};
        Double[] input3 = {10.0, 10.0};
        Double[] input4 = {15.0, 20.0};

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);
        DataPoint[] datapoints1 = new DataPoint[]{dp1, dp2};

        DataPoint dp3 = new DataPoint("", catData, input3);
        DataPoint dp4 = new DataPoint("", catData, input4);
        DataPoint[] datapoints2 = new DataPoint[]{dp3, dp4};

        Cluster cluster1 = new Cluster(datapoints1);
        Cluster cluster2 = new Cluster(datapoints2);

        double minDistance = Math.round(aggCluster.findMinDistance(cluster1, cluster2, euclideanDistance) * 100.0) / 100.0;

        assertEquals(4.00, minDistance, "smallest distance is between the points {11,20} and {15,20} " +
                "which has the distance of 4.00");
    }

    // test 9
    @Test
    void clusterPromoters() throws IOException {
        DefaultParser DBparser = new DefaultParser();
        File promoters = new File("src/main/resources/promoters.data");

        DBparser.setDatabase(promoters);
        ArrayList<DataPoint> database = DBparser.readDB();

        aggCluster.setRoundLimit(52);
        Cluster[] clusteredDB = aggCluster.clusterData(database, euclideanDistance);
        System.out.println(clusteredDB[0].size());
        assertTrue(clusteredDB[0].size() == 53, "there are 106 samples, thus the final cluster should have 106 datapoints");
    }
}
