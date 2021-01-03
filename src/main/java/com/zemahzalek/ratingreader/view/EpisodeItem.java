package com.zemahzalek.ratingreader.view;

import com.zemahzalek.ratingreader.model.Episode;
import javafx.application.Platform;
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
    @FXML private ToggleButton nameToggleButton;
    @FXML private Label nameLabel;
    @FXML private Label lengthLabel;
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
        displayLength();

        episodeNrLabel.setText("EP" + episode.getEpisodeNr());
        nameLabel.setVisible(false);
        ratingLabel.setText(episode.getRating());

        // Change background color
        if(episode.getEpisodeNr() % 2 == 0) {
            setStyle("-fx-background-color: #e9e9e9");
        } else {
            setStyle("-fx-background-color: #e3e3e3");
        }
    }

    private void removeFocus() {
        // Sets focus to root anchor pane instead of pressed node
        rootNode.getParent().requestFocus();
    }

    private void displayLength() {
        // Do not start new thread if length already has been fetched
        if(episode.getLength() != null) {
            lengthLabel.setText(episode.getLength());
            loadingImageView.setVisible(false);
            lengthLabel.setLayoutX(getLengthLabelXPosition());
        }
        else {
            lengthLabel.setText("");
            loadingImageView.setVisible(true);

            // New thread where the length fetch occurs to avoid freezing the JavaFX Application thread
            new Thread(new GetLengthRunnable()).start();
        }
    }

    private double getLengthLabelXPosition() {
        return nameToggleButton.getLayoutX() + nameToggleButton.getPrefWidth() - 10;
    }

    // -------- FXML -------- //

    @FXML
    private void onPressEpisodeNameToggleButton() {
        removeFocus();
        // If toggle is not selected; set episode name to visible on button press.
        // Else do opposite
        if(!nameToggleButton.isSelected()) {
            nameLabel.setVisible(true);
            nameLabel.setText(episode.getName());
        } else {
            nameLabel.setVisible(false);
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
                lengthLabel.setText(episode.getLength());
                loadingImageView.setVisible(false);
                lengthLabel.setLayoutX(getLengthLabelXPosition());
            });
        }
    }
}
