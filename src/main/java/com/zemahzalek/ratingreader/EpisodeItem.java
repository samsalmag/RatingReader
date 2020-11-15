package com.zemahzalek.ratingreader;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class EpisodeItem extends AnchorPane {

    private ScrollPane rootNode;
    @FXML private Label episodeNrLabel;
    @FXML private Label ratingLabel;
    @FXML private ToggleButton episodeNameToggleButton;
    @FXML private Label episodeNameLabel;

    public EpisodeItem(Node rootNode, int episodeNr, String rating) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/episodeItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.rootNode = (ScrollPane) rootNode;
        init();

        episodeNrLabel.setText("EP" + String.valueOf(episodeNr));
        episodeNameLabel.setVisible(false);
        ratingLabel.setText(rating);

        // Change background color
        if(episodeNr % 2 == 0) {
            setStyle("-fx-background-color: #e9e9e9");
        } else {
            setStyle("-fx-background-color: #e3e3e3");
        }
    }

    private void init() {
        setPrefWidth(rootNode.getWidth());
        rootNode.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                setPrefWidth(newValue.doubleValue());
            }
        });
    }

    private void removeFocus() {
        // Sets focus to root anchor pane instead of pressed node
        rootNode.getParent().requestFocus();
    }

    // -------- FXML -------- //

    @FXML
    private void onPressEpisodeNameToggleButton() {
        removeFocus();
        // If toggle is not selected; set episode name to visible on button press.
        // Else do opposite
        if(!episodeNameToggleButton.isSelected()) {
            episodeNameLabel.setVisible(true);
        } else {
            episodeNameLabel.setVisible(false);
        }
    }
}
