package spi;

import javafx.stage.Stage;
import kernel.DataPoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface FileParser {
    public ArrayList<DataPoint> readDB() throws IOException;
    public String getName();
    public void setDatabase(File file) throws FileNotFoundException;
    public File chooseFile(Stage stage);

    public String getFileName();

    public int getNumberOfLines();

    public int getFeatureNumber();
}
