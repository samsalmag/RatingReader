package com.zemahzalek.ratingreader.view;

import com.zemahzalek.ratingreader.util.ViewConstants;
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
        Scene scene = new Scene(rootFXML, ViewConstants.INITIAL_WIDTH, ViewConstants.INITIAL_HEIGHT);

        stage.setScene(scene);
        stage.show();

        String title = readPropertiesFile("dev.name") + " " + readPropertiesFile("dev.version");
        stage.setTitle(title);

        // Decoration width and height (window border width and height)
        double dWidth = scene.getWindow().getWidth()-scene.getWidth();
        double dHeight = scene.getWindow().getHeight()-scene.getHeight();

        stage.setMinWidth(ViewConstants.MIN_WIDTH + dWidth + ViewConstants.RESULT_SCROLLPANE_SCROLLBAR_WIDTH);
        stage.setMinHeight(ViewConstants.MIN_HEIGHT + dHeight);
        stage.setMaxWidth(ViewConstants.MAX_WIDTH + dWidth + ViewConstants.RESULT_SCROLLPANE_SCROLLBAR_WIDTH);
        stage.setMaxHeight(ViewConstants.MAX_HEIGHT + dHeight);
    }

    private String readPropertiesFile(String key) throws IOException {
        Properties properties = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("/dev.properties");
        properties.load(stream);

        return properties.getProperty(key);
    }
}
