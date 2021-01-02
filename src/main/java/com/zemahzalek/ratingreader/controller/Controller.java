package com.zemahzalek.ratingreader.controller;

import com.zemahzalek.ratingreader.model.Episode;
import com.zemahzalek.ratingreader.view.EpisodeItem;
import com.zemahzalek.ratingreader.model.Media;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class Controller {

    Media media;
    ImdbController imdbController;
    private String previousSearch;

    @FXML private AnchorPane rootAnchorPane;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private ComboBox seasonComboBox;
    @FXML private Label loadingLabel;
    @FXML private Label resultNameLabel;
    @FXML private Label resultTypeLabel;
    @FXML private Label resultCategoryLabel;
    @FXML private ScrollPane resultScrollPane;
    @FXML private FlowPane resultFlowPane;

    public Controller() {
        init();
    }

    @FXML
    private void initialize() {
        initSeasonComboBox();
        initSearchTextField();
        loadingLabel.setVisible(false);
        resultNameLabel.setVisible(false);
        resultTypeLabel.setVisible(false);
        resultCategoryLabel.setVisible(false);
    }

    // -------- INIT -------- //
    private void init() {
        media = new Media();
        imdbController = new ImdbController(media);
    }

    private void initSeasonComboBox() {

        // Disable combobox at start
        seasonComboBox.setDisable(true);

        // Adds ChangeListener to check for value change
        seasonComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observableValue, String oldValue, String newValue) {
                if(newValue == null || oldValue == null) {
                    return;
                }

                // If new season choice is not equal to already selected one
                if(!newValue.equals(oldValue)) {
                    updateResults(Integer.valueOf(newValue));   // Update results on season change
                }
            }
        });
    }

    private void initSearchTextField() {

        // Add event so ENTER key can be pressed to search
        searchTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                // If key pressed was enter
                if(keyEvent.getCode().equals(KeyCode.ENTER)) {
                    // Do search
                    search(searchTextField.getText());
                }
            }
        });
    }

    // ------------------  ------------------ //

    private void search(String searchTerm) {
        if(searchTerm.equals("") || searchTerm == null) {
            return;
        }

        // If a search has been done before
        if(previousSearch != null) {
            // If new search is the same as previous search
            if(searchTerm.equals(previousSearch)) {
                return;
            }
        }

        // New thread where the media information fetch occurs to avoid freezing the JavaFX Application thread
        new Thread(new SearchMediaRunnable(searchTerm)).start();     // Start the new thread
    }

    private void updateResults(int season) {
        resultFlowPane.getChildren().clear();           // Remove all children

        // Do only if media search is a TV Series
        if(media.isSeries()) {
            for (Episode episode : media.getEpisodes().get(season-1)) {  // -1 because of index out of bounds
                resultFlowPane.getChildren().add(new EpisodeItem(resultScrollPane, episode));
            }
        }

        resultNameLabel.setVisible(true);
        resultTypeLabel.setVisible(true);
        resultCategoryLabel.setVisible(true);
    }

    private void populateSeasonComboBox() {
        seasonComboBox.getItems().clear();                  // Empty combo box
        for (int i = 1; i <= media.getNrSeasons(); i++) {
            seasonComboBox.getItems().add(String.valueOf(i));
        }
    }

    // ------- FXML ------- //

    @FXML
    private void removeFocus() {
        // Sets focus to root anchor pane instead of pressed node
        rootAnchorPane.requestFocus();
    }

    @FXML
    private void onPressSearchButton() throws IOException {
        removeFocus();
        search(searchTextField.getText());
    }

    // ------------------  ------------------ //

    private class SearchMediaRunnable implements Runnable {

        private String searchTerm;

        public SearchMediaRunnable(String searchTerm) {
            this.searchTerm = searchTerm;
        }

        @Override
        public void run() {
            loadingLabel.setVisible(true);
            searchButton.setDisable(true);

            // Get the media information
            try {
                imdbController.setMedia(searchTerm);  // Sets media
            } catch (IOException e) {
                e.printStackTrace();
            }

            // The UI updater. This is what will happen after media information is gathered. Updater is called below.
            // Runs the UI updater after JavaFX Application thread is done (after the media information is gathered)
            Platform.runLater(() -> {
                resultNameLabel.setText(media.getName() + " (" + media.getReleaseYear() + ")");
                resultTypeLabel.setText(media.getType().getName());
                resultCategoryLabel.setText(media.getCategory());
                updateResults(1);       // Get season 1 on search

                // Only do this if media is a TV Series
                if(media.isSeries()) {
                    populateSeasonComboBox();
                    seasonComboBox.getSelectionModel().select(0);       // Select 1st season on ComboBox
                    seasonComboBox.setDisable(false);
                } else {                                                  // Disables seasonComboBox if media isn't a series
                    seasonComboBox.getItems().clear();
                    seasonComboBox.setDisable(true);
                }

                loadingLabel.setVisible(false);     // Disable Loading Icon
                searchButton.setDisable(false);     // Enable search button after result is displayed
                previousSearch = searchTextField.getText();     // Set previous search text
            });
        }
    }
}
