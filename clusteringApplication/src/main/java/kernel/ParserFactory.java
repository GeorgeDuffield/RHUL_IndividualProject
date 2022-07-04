package kernel;

import spi.FileParser;


import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.ServiceLoader;

public class ParserFactory {
    private ArrayList<FileParser> parsers;
    private ArrayList<String> outputNames;

    /**
     * Constructor which is used to load the currently available plugins.
     */
    public ParserFactory() {
        // load the plugins so that they can be picked up by the service loader
        PluginLoader pluginLoader = new PluginLoader();
        URLClassLoader classLoader = pluginLoader.loadPlugins("parsers");

        ServiceLoader<FileParser> parserLoader = ServiceLoader.load(FileParser.class, classLoader);
        parsers = new ArrayList<>();
        outputNames = new ArrayList<>();

        for (FileParser fp : parserLoader) {
            parsers.add(fp);
            outputNames.add(fp.getName());
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
    public FileParser getParser(String name) {
        for (FileParser fp : parsers) {
            if (fp.getName().equals(name)) {
                return fp;
            }
        }
        return null;
    }
}
