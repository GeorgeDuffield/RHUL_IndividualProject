package kernel;

import com.project.clusteringapplication.StartViewController;
import spi.ClusterOutput;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.ServiceLoader;
import java.util.prefs.Preferences;

/**
 * Factory used to find and access cluster output plugins.
 */
public class ClusterOutputFactory {
    private ArrayList<ClusterOutput> clusterOutputs;
    private ArrayList<String> outputNames;

    /**
     * Constructor which is used to load the currently available plugins.
     */
    public ClusterOutputFactory() {
        // load the plugins so that they can be picked up by the service loader
        PluginLoader pluginLoader = new PluginLoader();
        URLClassLoader classLoader = pluginLoader.loadPlugins("outputs");

        ServiceLoader<ClusterOutput> clusterOutputLoader = ServiceLoader.load(ClusterOutput.class, classLoader);
        clusterOutputs = new ArrayList<ClusterOutput>();
        outputNames = new ArrayList<>();

        for (ClusterOutput co : clusterOutputLoader) {
            clusterOutputs.add(co);
            outputNames.add(co.getName());
        }

        // if class loader is null then there is nothing to close
        if (classLoader != null) {
            // try and close the class loader to prevent memory leaks
            try {
                classLoader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method used to get the collection of cluster output names.
     *
     * @return the list of output names
     */
    public ArrayList<String> getNames() {
        return outputNames;
    }

    /**
     * Returns the cluster output based on the name provided.
     *
     * @param name the name of the cluster output which is being requested
     * @return the output object that was requested
     */
    public ClusterOutput getClusterOutput(String name) {
        for (ClusterOutput co : clusterOutputs) {
            if (co.getName().equals(name)) {
                return co;
            }
        }
        return null;
    }
}
