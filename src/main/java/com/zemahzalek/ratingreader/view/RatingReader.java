package com.zemahzalek.ratingreader.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RatingReader extends Application {

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent rootFXML = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/root.fxml"));
        Scene scene = new Scene(rootFXML, 700, 500);
        stage.setMinWidth(500 * 1);
        stage.setMinHeight(400);
        String title = readPropertiesFile("dev.name") + " " + readPropertiesFile("dev.version");
        stage.setTitle(title);

        stage.setScene(scene);
        stage.show();
    }

    private String readPropertiesFile(String key) throws IOException {
        Properties properties = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("/dev.properties");
        properties.load(stream);

        return properties.getProperty(key);
    }
}
