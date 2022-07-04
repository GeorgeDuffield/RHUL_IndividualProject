package com.project.clusteringapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kernel.ParserFactory;
import spi.FileParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class StartViewController implements Initializable {
    @FXML
    private Button startButton;

    @FXML
    private ComboBox<String> parserSelect;

    @FXML
    FileViewController fileViewController;

    ParserFactory parserFactory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateParserDropDown();
    }


    /**
     * Used to update the parser dropdown for when ever plugins are loaded.
     */
    private void updateParserDropDown() {
        parserFactory = new ParserFactory();
        ArrayList<String> parserNames = parserFactory.getNames();

        // clear previous items to remove old/ duplicate parser options
        parserSelect.getItems().clear();
        for (String s : parserNames) {
            parserSelect.getItems().add(s);
        }
        parserSelect.setValue(parserNames.get(0));
    }

    /**
     * Chooses the file which is to be clustered, after which the file information screen is shown.
     *
     * @param event the file selection button event
     * @throws IOException the execption which is thrown if something goes wrong with the file select
     */
    @FXML
    protected void chooseFile(ActionEvent event) throws IOException {
        Stage landing = (Stage) startButton.getScene().getWindow();
        FileParser parser = parserFactory.getParser(parserSelect.getSelectionModel().getSelectedItem());

        try {
            File chosenFile = parser.chooseFile(landing);
            // Initialise a new file controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fileView.fxml"));
            Parent root = loader.load();
            fileViewController = (FileViewController) loader.getController();
            Scene scene = new Scene(root);

            fileViewController.setStartStage(landing.getScene());
            fileViewController.setParser(parser);
            fileViewController.processFile(chosenFile);

            // display file window
            landing.setScene(scene);

            // if not file is selected, print warning to command line
            // am error popup is not shown as the user may simply wish to change cluster settings before viewing the file
        } catch (NullPointerException e) {
            System.err.println("No file selected");
        } catch (IOException e){
            Alert emptyFile = new Alert(Alert.AlertType.ERROR);
            emptyFile.setTitle("Error!");
            emptyFile.setHeaderText("Warning: Empty file selected");
            emptyFile.setContentText("File which was selected is empty or cannot be read");

            emptyFile.showAndWait();
        }
    }

    /**
     * The method which handles the selection of the plugin folder.
     *
     * @param event the plugin directory selection button press
     */
    @FXML
    void openPluginManager(ActionEvent event) {
        Stage landing = (Stage) startButton.getScene().getWindow();
        // select the directory
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(landing);
        Preferences preferences = Preferences.userNodeForPackage(StartViewController.class);

        // check that a valid directory has been chosen before trying to create files
        if (directory != null) {
            File[] directoryFile = directory.listFiles();

            //no files in directory so create folder for each plugin
            if (directoryFile.length == 0) {
                File clusterPluginDir = new File(directory.getPath() + File.separator + "clusterAlgorithms");
                File distancePluginDir = new File(directory.getPath() + File.separator + "distanceAlgorithms");
                File outputPluginDir = new File(directory.getPath() + File.separator + "outputs");
                File parserPluginDir = new File(directory.getPath() + File.separator + "parsers");

                clusterPluginDir.mkdir();
                distancePluginDir.mkdir();
                outputPluginDir.mkdir();
                parserPluginDir.mkdir();
            } else {
                boolean clusterPluginDirExists = false;
                boolean distancePluginDirExists = false;
                boolean outputPluginDirExists = false;
                boolean parserPluginDirExists = false;

                // search for which folders exist (if any)
                for (int i = 0; i < directoryFile.length; i++) {
                    String fileName = directoryFile[i].getName();
                    switch (fileName) {
                        case "clusterAlgorithms":
                            clusterPluginDirExists = true;
                            break;
                        case "distanceAlgorithms":
                            distancePluginDirExists = true;
                            break;
                        case "outputs":
                            outputPluginDirExists = true;
                            break;
                        case "parsers":
                            parserPluginDirExists = true;
                    }

                }

                //create any plugin directories that dont already exist
                if (!clusterPluginDirExists) {
                    File clusterPluginDir = new File(directory.getPath() + File.separator + "clusterAlgorithms");
                    clusterPluginDir.mkdir();
                }
                if (!distancePluginDirExists) {
                    File distancePluginDir = new File(directory.getPath() + File.separator + "distanceAlgorithms");
                    distancePluginDir.mkdir();
                }
                if (!outputPluginDirExists) {
                    File outputPluginDir = new File(directory.getPath() + File.separator + "outputs");
                    outputPluginDir.mkdir();
                }
                if (!parserPluginDirExists) {
                    File parserPluginDir = new File(directory.getPath() + File.separator + "parsers");
                    parserPluginDir.mkdir();
                }

            }

            // save the selected plugin folder as a preference so that it can be automatically accessed the next time the app is used.

            preferences.put("pluginDir", directory.getPath());

            // new parser plugins may have been loaded so update the factory and reset parser dropdown
            updateParserDropDown();

        }

    }
}