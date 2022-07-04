package com.project.clusteringapplication;

import GUI.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Launches the javaFX side of the application.
 */
public class StartGUI extends Application {
    /**
     * Starts the javaFX
     * @param stage the stage which will be used to display the first screen
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("startingView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Clustering Application");
        stage.setScene(scene);
        stage.setResizable(false);

        Image icon = new Image(Controller.class.getResource("/images/logo.jpg").toExternalForm());
        stage.getIcons().add(icon);

        stage.show();
    }

    /**
     * Initiates the start method.
     * @param args arguments provided by the user prior to starting the application
     */
    public static void main(String[] args) {
        launch();
    }
}