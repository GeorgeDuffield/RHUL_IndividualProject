package plugins;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kernel.DataPoint;
import spi.FileParser;

import java.io.*;
import java.util.ArrayList;

/**
 * Parses the requested database into a cluster ready state.
 */
public class DefaultParser implements FileParser {

    private InputStream iStream;
    private InputStreamReader streamReader;
    private BufferedReader bufferReader;

    private String fileName;
    private int numberOfLines;
    private int featureNumber;

    public String getName(){
        return "Default";
    }

    /**
     * sets the data base which is to be used within the application.
     * @param dbFile
     * @throws FileNotFoundException
     */
    @Override
    public void setDatabase(File dbFile) throws FileNotFoundException {
        iStream = new FileInputStream(dbFile);
        streamReader = new InputStreamReader(iStream);
        bufferReader = new BufferedReader(streamReader);
        fileName = dbFile.getName();
    }

    /**
     * Allows the user to choose a file based on a given type which is defined within this method.
     * @param stage the stage used to display the file picker
     * @return the file which has been selected
     */
    @Override
    public File chooseFile(Stage stage) {
        // create a new file chooser which will allow the user to select the file they want to cluster
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to cluster");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.data", "*.csv"));

        // Ensure that old file remains if new file is not selected
        File newFile = fileChooser.showOpenDialog(stage);
        return newFile;
    }

    /**
     * Reads a single line from the currently connected database (Included for testing purposes).
     *
     * @return the line that has been read from the database
     * @throws IOException the exception that is thrown if the database cannot be accessed
     */
    public String readLine() throws IOException {
        return bufferReader.readLine();
    }

    /**
     * Used to read all data from the database, split data and assign its data type.
     *
     * @return returns the database as an array of objects
     * @throws IOException
     */
    public ArrayList<DataPoint> readDB() throws IOException {
        ArrayList<DataPoint> database = new ArrayList<DataPoint>();

        String DBline;
        int lineNumber = 0;
        int featureNum = 0;
        // read until all lines in the DB
        while ((DBline = bufferReader.readLine()) != null && DBline.length() > 0) {
            // Split line from DB and add to the array
            DataPoint dp = seperateData(DBline);

            if (featureNum == 0) {
                featureNum = dp.sizeTotal();
                database.add(dp);
                lineNumber++;
            } else {
                if (dp.sizeTotal() != featureNum) {
                    System.err.println("Failed to read: " + dp.toString() + " due to incorrect feature size");
                } else {
                    database.add(dp);
                    lineNumber++;
                }
            }
        }
        featureNumber = featureNum;
        numberOfLines = lineNumber;
        return database;
    }

    /**
     * Parses a line read in the database into a collection of attributes.
     *
     * @param DBline the string of text that is to be parsed
     * @return the collection of attributes created during the parsing process
     */
    public DataPoint seperateData(String DBline) {

        // Splits string based on common DB delimiters found during research
        String[] dataPointAsString = DBline.split(",|\\s+|\\t+|;|:");


        DataPoint dp = new DataPoint(DBline);
        for (int i = 0; i < dataPointAsString.length; i++) {
            //try and parse as a number, if that fails, assume its categorical
            try {
                Double d = Double.parseDouble(dataPointAsString[i]);
                dp.addNumerical(d);
            } catch (NumberFormatException failedToParseAsDouble) {
                dp.addCategorical(dataPointAsString[i]);
            }
        }
        return dp;
    }

    public String getFileName() {
        return fileName;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public int getFeatureNumber() {
        return featureNumber;
    }

}
