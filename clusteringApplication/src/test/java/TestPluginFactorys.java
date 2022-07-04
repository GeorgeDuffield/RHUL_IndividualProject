import kernel.ClusterAlgorithmFactory;
import kernel.ClusterOutputFactory;
import kernel.DistanceAlgorithmFactory;
import kernel.ParserFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Due to their similarity and simplistic implementation, the plugin factories are tested in a single combined file.
 */
public class TestPluginFactorys {
    private ArrayList<String> names;

    @Test
    public void createNewFactorys() {
        ClusterAlgorithmFactory clusterAlgorithmFactory = new ClusterAlgorithmFactory();
        ClusterOutputFactory outputFactory = new ClusterOutputFactory();
        ParserFactory parserFactory = new ParserFactory();
        DistanceAlgorithmFactory distanceAlgorithmFactory = new DistanceAlgorithmFactory();
    }

    @Test
    public void getDefaultClusterAlgorithms() {
        ClusterAlgorithmFactory clusterAlgorithmFactory = new ClusterAlgorithmFactory();
        names = clusterAlgorithmFactory.getNames();

        assertTrue(names.get(0).equals("Agglomerative") && names.get(1).equals("K-means"), "Both algorithms should be present " +
                "and in the order specificed in the Meta-inf files");
    }

    @Test
    public void getDefaultParser(){
        ParserFactory parserFactory = new ParserFactory();
        names = parserFactory.getNames();
        assertTrue(names.get(0).equals("Default"), "Only current parser available is the default parser thus " +
                "first value should be 'default' ");
    }

    @Test
    public void getDefaultOutput(){
        ClusterOutputFactory outputFactory = new ClusterOutputFactory();
        names = outputFactory.getNames();
        assertTrue(names.get(0).equals("Scatter chart"), "Only current parser available is the default parser thus " +
                "first value should be 'default' ");
    }


    @Test
    public void getDefaultDistance(){
        DistanceAlgorithmFactory distanceAlgorithmFactory = new DistanceAlgorithmFactory();
        names = distanceAlgorithmFactory.getNames();
        assertTrue(names.get(0).equals("Euclidean Distance"), "Only current parser available is the default parser thus " +
                "first value should be 'default' ");
    }
}
