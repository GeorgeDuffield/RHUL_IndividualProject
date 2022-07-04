package com.project.clusteringapplication;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kernel.DataPoint;
import kernel.ParserFactory;
import spi.FileParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller which is used to manage the data displayed in the file details screen
 */
public class FileViewController{

    @FXML
    private Button newFileButton;

    @FXML
    private Button clusterSettingsButton;


    @FXML
    private Text sampleText;

    @FXML
    private Text featureText;

    @FXML
    private Text fileNameText;

    @FXML
    private Button returnButton;


    private File inputFile;

    private ArrayList<DataPoint> data;

    private FileParser parser;

    private Scene startScene;

    /**
     * @param event choose new file button press
     * @throws IOException thrown if something goes wrong with selecting a file
     */
    @FXML
    protected void chooseNewFile(ActionEvent event) throws IOException {
        Stage stage = (Stage) newFileButton.getScene().getWindow();

        // try and select a new file, if no item is selected then output a warning
        try{
            inputFile = parser.chooseFile(stage);
            processFile(inputFile);
        } catch (NullPointerException e){
            System.err.println("Warning: No file selected");
        }
    }

    /**
     * Read the file using the parser to update the information displayed on the file detail screen.
     * @param inputFile the file which is to be analysed
     * @throws IOException thrown if the file cant be read properly
     */
    public void processFile(File inputFile) throws IOException {
        parser.setDatabase(inputFile);

        // read the database to calculate DB info such as the number of samples
        data = parser.readDB();

        fileNameText.setText(parser.getFileName());
        int lineNum = parser.getNumberOfLines();
        if(lineNum == 0){
            throw new IOException();
        }
        sampleText.setText(String.valueOf(lineNum));
        featureText.setText(String.valueOf(parser.getFeatureNumber()));

        // update input file field
        setInputFile(inputFile);
    }

    /**
     * Method which is used to change the view to the cluster settings screen
     * @param event the cluster setting button press
     * @throws IOException thrown if the root cannot be loaded
     */
    @FXML
    void openSettings(ActionEvent event) throws IOException {
        Stage landing = (Stage) newFileButton.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("clusterSettingsView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // set the file information within the next screen so it can be used in clustering
        SettingsViewController settingsViewController = loader.getController();
        settingsViewController.setfileViewScene(landing.getScene());
        settingsViewController.setData(data);
        settingsViewController.setFileName(inputFile.getName());

        // display file window
        landing.setScene(scene);
    }

    @FXML
    void returnToStart(ActionEvent event) {
        Stage stage = (Stage) newFileButton.getScene().getWindow();
        stage.setScene(startScene);
    }

    public void setParser(FileParser parser) {
        this.parser = parser;
    }

    public void setStartStage(Scene startScene) {
        this.startScene = startScene;
    }

    /**
     * Setter used to update the current input file
     *
     * @param inputFile the new data file
     */
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }


}
