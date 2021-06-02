package org.openjfx.MetaHeuristicAlgoVisualizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
//    	Parent root = FXMLLoader.load(getClass().getResource("/resources/org/openjfx/MetaHeuristicAlgoVisualizer/primary.fxml"));
    	Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
        scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}