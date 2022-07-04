package kernel;
import spi.DistanceAlgorithm;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.ServiceLoader;

/**
 * Factory used to produce and access different distance algorithms
 */
public class DistanceAlgorithmFactory {
    private ArrayList<DistanceAlgorithm> algorithmList;
    private ArrayList<String> algorithmNames;

    /**
     * Constructor used to instantiate the various algorithm plugins
     */
    public DistanceAlgorithmFactory() {
        // load the plugins so that they can be picked up by the service loader
        PluginLoader pluginLoader = new PluginLoader();
        URLClassLoader classLoader = pluginLoader.loadPlugins("distanceAlgorithms");


        // load the pre-existing and new plugins
        ServiceLoader<DistanceAlgorithm> algorithmLoader = ServiceLoader.load(DistanceAlgorithm.class, classLoader);

        // add the plugin information to local variables for easy future access
        algorithmList = new ArrayList<DistanceAlgorithm>();
        algorithmNames = new ArrayList<String>();
        for (DistanceAlgorithm distanceAlgorithm : algorithmLoader) {
            algorithmList.add(distanceAlgorithm);
            algorithmNames.add(distanceAlgorithm.getName());
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
    public DistanceAlgorithm getAlgorithm(String algorithmName) {
        for (int i = 0; i < algorithmList.size(); i++) {
            if (algorithmList.get(i).getName().equals(algorithmName)) {
                return algorithmList.get(i);
            }
        }
        // no item found so return null
        return null;
    }
}
