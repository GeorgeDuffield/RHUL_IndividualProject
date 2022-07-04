import kernel.Cluster;
import kernel.DataPoint;
import plugins.EuclideanDistanceAlgorithm;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.lang.Math.round;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEuclideanDistanceAlgorithm {
    EuclideanDistanceAlgorithm eDistance = new EuclideanDistanceAlgorithm();
    // empty string provided for original tests where only numerical data was used
    String[] catData = new String[0];

    // test 1
    @Test
    void testTwoDimensionNumericalEuclideanDistance() {
        ArrayList<Cluster> clusters = new ArrayList<Cluster>();
        Double[] input1 = {2.0, 5.0};
        Double[] input2 = {5.0, 10.0};
        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);


        // multiple by 100 and then round to get a whole number after which divide by 100 to get original value to two decimal places
        double distance = round(eDistance.measureDistance(dp1, dp2) * 100.0) / 100.0;
        assertEquals(5.83, distance, "distance between the two datapoints should round to 5.83 ");
    }

    // test 2
    @Test
    void testThreeDimensionEuclideanDistance() {
        Double[] input1 = {2.0, 5.0, 1.0};
        Double[] input2 = {5.0, 10.0, 8.0};

        DataPoint dp1 = new DataPoint("", catData, input1);
        DataPoint dp2 = new DataPoint("", catData, input2);

        // multiple by 100 and then round to get a whole number after which divide by 100 to get original value to two decimal places
        double distance = round(eDistance.measureDistance(dp1, dp2) * 100.0) / 100.0;
        assertEquals(9.11, distance, "distance between the two datapoints should round to 9.11 ");
    }

    // Test 3
    @Test
    void testCategoricalDataDistance() {
        Double[] numInput1 = new Double[0];
        String[] catInput1 = new String[]{"MIT"};

        Double[] numInput2 = new Double[0];
        String[] catInput2 = new String[]{"NUC"};

        DataPoint dp1 = new DataPoint("", catInput1, numInput1);
        DataPoint dp2 = new DataPoint("", catInput2, numInput2);

        double distance = eDistance.measureDistance(dp1, dp2);
        assertEquals(3.0, distance, "Three substitutions are needed to turn MIT to NUC, thus distance should be 3");
    }

    // Test 4
    @Test
    void testMultiTypedDistance() {
        Double[] numInput1 = new Double[]{4.0};
        String[] catInput1 = new String[]{"MIT"};

        Double[] numInput2 = new Double[]{2.0};
        String[] catInput2 = new String[]{"NIV"};

        DataPoint dp1 = new DataPoint("", catInput1, numInput1);
        DataPoint dp2 = new DataPoint("", catInput2, numInput2);

        double distance = round(eDistance.measureDistance(dp1, dp2) * 100.0) / 100.0;
        assertEquals(2.83, distance,
                "squared distance between 4 and 2 is 4, squared distance between MIT and NIV is 4, thus output should be sprt 8 ie 2.83 (rounded)");
    }

    // test 5
    @Test
    void testMultiCategoricalDistance() {
        Double[] numInput1 = new Double[0];
        String[] catInput1 = new String[]{"MIT", "CAT", "SFZ"};

        Double[] numInput2 = new Double[0];
        String[] catInput2 = new String[]{"NIV", "RAT", "S"};

        DataPoint dp1 = new DataPoint("", catInput1, numInput1);
        DataPoint dp2 = new DataPoint("", catInput2, numInput2);

        double distance = eDistance.measureDistance(dp1, dp2);
        assertEquals(3.0, distance, "total squared distance of 9 so sqrt distance should be 3");
    }
}
