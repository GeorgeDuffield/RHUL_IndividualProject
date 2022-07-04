import kernel.Cluster;
import kernel.DataPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCluster {

    @Test
    void testLength(){
        DataPoint dp = new DataPoint();
        Cluster testCluster = new Cluster(dp);

        assertTrue(testCluster.size() == 1, "Cluster was instantiated with a single clusters and thus the length should be 1");
    }

    @Test
    void testMultipleValueInstantiation(){
        Double[] dataValues1 = new Double[] {1.0, 2.0};
        String[] stringValues1 = new String[]{"1.0", "2.0"};
        DataPoint dp1 = new DataPoint("1.0, 3.0", stringValues1, dataValues1);

        Double[] dataValues2 = new Double[] {3.0, 4.0};
        String[] stringValues2 = new String[]{"3.0", "4.0"};
        DataPoint dp2 = new DataPoint("3.0, 4.0", stringValues2, dataValues2);

        DataPoint[] data = new DataPoint[] {dp1, dp2};

        Cluster testCluster = new Cluster(data);

        assertTrue(testCluster.size() == 2, "Cluster was instantiated with two clusters and thus the length should be 2");
    }

    @Test
    void testGetIndex(){
        Double[] dataValues1 = new Double[] {1.0, 2.0};
        String[] stringValues1 = new String[]{"1.0", "2.0"};
        DataPoint dp1 = new DataPoint("1.0, 3.0", stringValues1, dataValues1);


        Double[] dataValues2 = new Double[] {3.0, 4.0};
        String[] stringValues2 = new String[]{"3.0", "4.0"};
        DataPoint dp2 = new DataPoint("3.0, 4.0", stringValues2, dataValues2);

        DataPoint[] data = new DataPoint[] {dp1, dp2};

        Cluster testCluster = new Cluster(data);
        assertTrue(testCluster.get(1).equals(dp2), "Fetching second datapoint so value should match the second datapoint added to the cluster");
    }

    @Test
    void testAddData(){
        Double[] dataValues1 = new Double[] {1.0, 2.0};
        String[] stringValues1 = new String[]{"1.0", "3.0"};
        DataPoint dp1 = new DataPoint("1.0, 3.0", stringValues1, dataValues1);

        Double[] dataValues2 = new Double[] {3.0, 4.0};
        String[] stringValues2 = new String[]{"3.0", "4.0"};
        DataPoint dp2 = new DataPoint("3.0, 4.0", stringValues2, dataValues2);

        DataPoint[] data = new DataPoint[] {dp1, dp2};

        Cluster testCluster = new Cluster(data);

        Double[] dataValues3 = new Double[] {5.0, 6.0};
        String[] stringValues3 = new String[]{"5.0", "6.0"};
        DataPoint dp3 = new DataPoint("5.0, 6.0", stringValues3, dataValues3);
        testCluster.add(dp3);

        assertTrue(testCluster.get(2).equals(dp3), "Last datapoint in the cluster should be the one which was most recently added");
    }

    @Test
    void testToString(){
        Double[] dataValues1 = new Double[] {1.0, 2.0};
        String[] stringValues1 = new String[]{"1.0", "3.0"};
        DataPoint dp1 = new DataPoint("1.0, 3.0", stringValues1, dataValues1);

        Double[] dataValues2 = new Double[] {3.0, 4.0};
        String[] stringValues2 = new String[]{"3.0", "4.0"};
        DataPoint dp2 = new DataPoint("3.0, 4.0", stringValues2, dataValues2);

        DataPoint[] data = new DataPoint[] {dp1, dp2};

        Cluster testCluster = new Cluster(data);

        System.out.println(testCluster.toString());
    }
}
