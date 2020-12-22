package com.zemahzalek.ratingreader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RatingReader extends Application {

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent rootFXML = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/root.fxml"));
        Scene scene = new Scene(rootFXML, 700, 500);
        stage.setMinWidth(650);
        stage.setMinHeight(400);
        stage.setTitle("Rating Reader 0.6.0");

        stage.setScene(scene);
        stage.show();
    }
}
