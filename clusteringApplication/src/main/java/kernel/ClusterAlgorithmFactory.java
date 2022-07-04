package kernel;

import spi.ClusterAlgorithm;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.ServiceLoader;

/**
 * The class which is used to handle the construction and request of clustering algorithms. This class also handles access to the clustering algorithm plugins.
 */
public class ClusterAlgorithmFactory {
    private ArrayList<ClusterAlgorithm> algorithmList;
    private ArrayList<String> algorithmNames;

    /**
     * Constructor used to instantiate the various algorithm plugins
     */
    public ClusterAlgorithmFactory() {
        // load the plugins so that they can be picked up by the service loader
        PluginLoader pluginLoader = new PluginLoader();
        URLClassLoader classLoader = pluginLoader.loadPlugins("clusterAlgorithms");


        // load the pre-existing and new plugins
        ServiceLoader<ClusterAlgorithm> algorithmLoader = ServiceLoader.load(ClusterAlgorithm.class, classLoader);

        // add the plugin information to local variables for easy future access
        algorithmList = new ArrayList<ClusterAlgorithm>();
        algorithmNames = new ArrayList<String>();
        for (ClusterAlgorithm clusterAlgorithm : algorithmLoader) {
            algorithmList.add(clusterAlgorithm);
            algorithmNames.add(clusterAlgorithm.getName());
        }

        // if class loader is null then there is nothing to close
        if (classLoader != null){
            // try and close the class loader to prevent memory leaks
            try {
                classLoader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method which returns the names of all the different algorithms
     *
     * @return the collection of algorithm names
     */
    public ArrayList<String> getNames() {
        return algorithmNames;
    }

    /**
     * Returns the algorithm which corresponds to the selected option in the algorithm drop down.
     *
     * @param algorithmName the name of the algorithm that is being requested
     * @return the selected clustering algorithm
     */
    public ClusterAlgorithm getAlgorithm(String algorithmName) {
        for (int i = 0; i < algorithmList.size(); i++) {
            if (algorithmList.get(i).getName().equals(algorithmName)) {
                return algorithmList.get(i);
            }
        }
        // no item found so return null
        return null;
    }
}
