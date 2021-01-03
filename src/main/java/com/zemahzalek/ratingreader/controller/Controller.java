package com.zemahzalek.ratingreader.controller;

import com.zemahzalek.ratingreader.model.Episode;
import com.zemahzalek.ratingreader.util.ViewConstants;
import com.zemahzalek.ratingreader.view.EpisodeItem;
import com.zemahzalek.ratingreader.model.Media;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class Controller {

    Media media;
    ImdbController imdbController;
    private String previousSearch;
    private boolean searching;

    @FXML private AnchorPane rootAnchorPane;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private ComboBox seasonComboBox;
    @FXML private Label loadingLabel;
    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private Label categoryLabel;
    @FXML private Label lengthLabel;
    @FXML private ScrollPane resultScrollPane;
    @FXML private FlowPane resultFlowPane;

    public Controller() {
        init();
    }

    // Init for FXML objects
    @FXML
    private void initialize() {
        initSeasonComboBox();
        initSearchTextField();
        initResultFlowPane();
        loadingLabel.setVisible(false);
        nameLabel.setVisible(false);
        typeLabel.setVisible(false);
        categoryLabel.setVisible(false);
        lengthLabel.setVisible(false);
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
        seasonComboBox.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observableValue, oldValue, newValue) -> {
            if(newValue == null || oldValue == null) {
                return;
            }

            // If new season choice is not equal to already selected one
            if(!newValue.equals(oldValue)) {
                updateResults(Integer.valueOf(newValue));   // Update results on season change
            }
        });
    }

    private void initSearchTextField() {
        // Add event so ENTER key can be pressed to search
        searchTextField.setOnKeyPressed(keyEvent -> {
            // If key pressed was enter and is currently not searching
            if(keyEvent.getCode().equals(KeyCode.ENTER) && !searching) {
                // Do search
                search(searchTextField.getText());
            }
        });
    }

    private void initResultFlowPane() {
        resultFlowPane.setPrefWidth(resultScrollPane.getWidth() - ViewConstants.RESULT_SCROLLPANE_SCROLLBAR_WIDTH);

        // Add listener to scrollpane's width so resultFlowPane will change width correctly (to scrollpane's viewport width (scrollbar excluded))
        resultScrollPane.widthProperty().addListener((observableValue, oldValue, newValue) ->
                resultFlowPane.setPrefWidth(resultScrollPane.getWidth() - ViewConstants.RESULT_SCROLLPANE_SCROLLBAR_WIDTH));
    }

    // ------------------  ------------------ //

    private void search(String searchTerm) {
        System.out.println(previousSearch);
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
        searching = true;
        previousSearch = searchTextField.getText();     // Set previous search text
    }

    private void updateResults(int season) {
        resultFlowPane.getChildren().clear();           // Remove all children

        // Do only if media search is a TV Series
        if(media.isSeries()) {
            for (Episode episode : media.getEpisodes().get(season-1)) {  // -1 because of index out of bounds
                resultFlowPane.getChildren().add(new EpisodeItem(resultScrollPane, episode));
            }
        }

        nameLabel.setVisible(true);
        typeLabel.setVisible(true);
        categoryLabel.setVisible(true);
        lengthLabel.setVisible(true);
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
    private void onPressSearchButton() {
        removeFocus();
        System.out.println(searching);
        if(!searching) {
            search(searchTextField.getText());
        }
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
                nameLabel.setText(media.getName() + " (" + media.getReleaseYear() + ")");
                typeLabel.setText(media.getType().getName());
                categoryLabel.setText(media.getCategory());
                lengthLabel.setText(media.getLength());
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
                searching = false;
            });
        }
    }
}
