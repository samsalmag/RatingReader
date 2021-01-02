package com.zemahzalek.ratingreader.view;

import com.zemahzalek.ratingreader.model.Episode;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class EpisodeItem extends AnchorPane {

    private ScrollPane rootNode;
    private Episode episode;
    @FXML private Label episodeNrLabel;
    @FXML private Label ratingLabel;
    @FXML private ToggleButton episodeNameToggleButton;
    @FXML private Label episodeNameLabel;
    @FXML private ToggleButton episodeLengthToggleButton;
    @FXML private ImageView loadingImageView;

    public EpisodeItem(Node rootNode, Episode episode) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/episodeItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.rootNode = (ScrollPane) rootNode;
        this.episode = episode;
        init();

        episodeNrLabel.setText("EP" + episode.getEpisodeNr());
        episodeNameLabel.setVisible(false);
        ratingLabel.setText(episode.getRating());
        loadingImageView.setVisible(false);

        // Change background color
        if(episode.getEpisodeNr() % 2 == 0) {
            setStyle("-fx-background-color: #e9e9e9");
        } else {
            setStyle("-fx-background-color: #e3e3e3");
        }

        if(episode.getLength() != null) {
            displayLength(episode.getLength());
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

    private void displayLength(String lengthText) {
        episodeLengthToggleButton.setDisable(true);
        episodeLengthToggleButton.setStyle("-fx-opacity: 1.0; -fx-background-color: lightgray;");        // Resets opacity change when nodes are disabled.
        episodeLengthToggleButton.setText(lengthText);
    }

    // -------- FXML -------- //

    @FXML
    private void onPressEpisodeNameToggleButton() {
        removeFocus();
        // If toggle is not selected; set episode name to visible on button press.
        // Else do opposite
        if(!episodeNameToggleButton.isSelected()) {
            episodeNameLabel.setVisible(true);
            episodeNameLabel.setText(episode.getName());
        } else {
            episodeNameLabel.setVisible(false);
        }
    }

    @FXML
    private void onPressEpisodeLengthToggleButton() {
        removeFocus();

        // Do not start new thread if length already has been fetched
        if(episode.getLength() != null) {
            displayLength(episode.getLength());
        } else {
            displayLength("Loading...");
            loadingImageView.setVisible(true);

            // New thread where the length fetch occurs to avoid freezing the JavaFX Application thread
            new Thread(new GetLengthRunnable()).start();
        }
    }

    // ------------------  ------------------ //

    private class GetLengthRunnable implements Runnable {

        @Override
        public void run() {

            // Fetch length
            try {
                episode.fetchLength();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Is run after length is fetched.
            Platform.runLater(() -> {
                episodeLengthToggleButton.setText(episode.getLength());
                loadingImageView.setVisible(false);
            });
        }
    }
}
