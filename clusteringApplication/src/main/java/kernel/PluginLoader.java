package kernel;

import com.project.clusteringapplication.StartViewController;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.prefs.Preferences;

/**
 * The class which is responsible for loading a given type of plugins with the user of service loaders
 */
public class PluginLoader {

    private // filter used to ensure that only jar files are selected from the directory
    FilenameFilter jarFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".jar");
        }
    };

    /**
     * Used to add the plugin jar files to the class path so that they can be picked up by the service loaders
     *
     * @return the class loader which now has access to the algorithm plugins
     */
    public URLClassLoader loadPlugins(String directory) {
        Preferences preferences = Preferences.userNodeForPackage(StartViewController.class);
        String path = preferences.get("pluginDir", "default");
        File pluginDir = new File(path);
        File[] subDirs = pluginDir.listFiles();

        File[] pluginJarFiles = null;
        // search though all the files in the plugin directory
        for (File f : subDirs) {
            // find the algorithm directory
            if (f.isDirectory() && f.getName().equals(directory)) {
                // add all the files in the algorithm directory that pass the .jar filter
                pluginJarFiles = f.listFiles(jarFilter);

                //algorithm directory has been found so stop looping
                break;
            }
        }

        if (pluginJarFiles != null) {
            // if jar files have been found, concert their paths to URLS
            try {
                URL[] urls = new URL[pluginJarFiles.length];
                for (int i = 0; i < pluginJarFiles.length; i++) {
                    urls[i] = pluginJarFiles[i].toURI().toURL();
                }
                // add the URLS to the class loader to allow access to the contents of the jar files
                URLClassLoader classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
                return classLoader;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
