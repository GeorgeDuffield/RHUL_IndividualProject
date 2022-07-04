package com.project.clusteringapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kernel.ClusterAlgorithmFactory;
import kernel.ClusterOutputFactory;
import kernel.DistanceAlgorithmFactory;
import spi.ClusterAlgorithm;
import spi.ClusterOutput;
import kernel.DataPoint;
import spi.DistanceAlgorithm;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * The controller used to handle user interactions within the cluster setting view.
 */
public class SettingsViewController implements Initializable {
    @FXML
    private ComboBox<String> algorithmDropDown;

    @FXML
    private ComboBox<String> distanceDropDown;


    @FXML
    private ComboBox<String> outputDropDown;

    @FXML
    private Button clusterButton;

    @FXML
    private Button returnButton;

    private Scene fileViewScene;

    public void setData(ArrayList<DataPoint> data) {
        this.data = data;
    }

    private ArrayList<DataPoint> data;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String fileName;


    public void setfileViewScene(Scene scene) {
        fileViewScene = scene;
    }

    private ArrayList<DistanceAlgorithm> distanceAlgorithmList;
    private ClusterAlgorithmFactory clusterAlgorithmFactory;
    private ClusterOutputFactory clusterOutputFactory;
    private DistanceAlgorithmFactory distanceAlgorithmFactory;

    /**
     * Upon initalisation the class will instantiate the javaFX elements with plugin options.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //create factories to access plugins
        clusterAlgorithmFactory = new ClusterAlgorithmFactory();
        clusterOutputFactory = new ClusterOutputFactory();
        distanceAlgorithmFactory = new DistanceAlgorithmFactory();

        // get the names of each plugin and add them to the option dropdowns
        ArrayList<String> algorithmNames = clusterAlgorithmFactory.getNames();
        ArrayList<String> clusterOutputNames = clusterOutputFactory.getNames();
        ArrayList<String> distanceAlgorithmNames = distanceAlgorithmFactory.getNames();

        for(String s : algorithmNames){
            algorithmDropDown.getItems().add(s);
        }

        for (String s : clusterOutputNames){
            outputDropDown.getItems().add(s);
        }

        for(String s : distanceAlgorithmNames){
            distanceDropDown.getItems().add(s);
        }


        // Set default value to first item in the list
        distanceDropDown.setValue(distanceAlgorithmNames.get(0));
        algorithmDropDown.setValue(algorithmNames.get(0));
        outputDropDown.setValue(clusterOutputNames.get(0));
    }

    @FXML
    void returnToFileSettings(ActionEvent event) {
        Stage stage = (Stage) algorithmDropDown.getScene().getWindow();
        stage.setScene(fileViewScene);

    }

    /**
     * Takes the settings selected by the user and begines the clustering process.
     * @param event the start clustering button press
     * @throws IOException
     */
    @FXML
    void startClustering(ActionEvent event) throws IOException {
        // load the information from the setting dropdowns
        ClusterAlgorithm clusterAlgorithm = clusterAlgorithmFactory.getAlgorithm(algorithmDropDown.getSelectionModel().getSelectedItem());
        DistanceAlgorithm distanceAlgorithm = distanceAlgorithmFactory.getAlgorithm(distanceDropDown.getSelectionModel().getSelectedItem());
        ClusterOutput output = clusterOutputFactory.getClusterOutput(outputDropDown.getSelectionModel().getSelectedItem());
        Chart outputChart = output.generateGraphicalOutput(clusterAlgorithm, data, distanceAlgorithm, fileViewScene, fileName);

        // load the output screen
        Stage landing = (Stage) algorithmDropDown.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("clusterOutputView.fxml"));
        Parent root = loader.load();

        // button which is used to return to the main menu
        Button restartButton = new Button("Start again");
        restartButton.setOnMouseClicked(event1 -> {
            try {
                FXMLLoader loader2 = new FXMLLoader(getClass().getResource("startingView.fxml"));
                Parent root2 = loader2.load();
                Scene scene2 = new Scene(root2);
                landing.setScene(scene2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Button used to return to the cluster settings
        Button settingsButton = new Button("Return to cluster Settings");
        settingsButton.setOnMouseClicked(event2 -> {
            landing.setScene(algorithmDropDown.getScene());
        });

        //add chart and button to vbox so that they can be displayed together
        VBox vBox = new VBox();
        vBox.getChildren().add(outputChart);

        // add buttons to box and alight to the center
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(restartButton, settingsButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10,0,10,0));

        //add the buttons to the main container
        vBox.getChildren().add(buttonBox);

        // show the chart screen
        Scene scene = new Scene(vBox);
        landing.setScene(scene);
    }
}
