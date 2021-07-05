package com.zemahzalek.ratingreader;

import com.zemahzalek.ratingreader.controller.imdb.ImdbController;
import com.zemahzalek.ratingreader.model.Media;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ImdbControllerTest {

    @Test
    public void setWebsiteCodeVersionTest() {

        ImdbController imdbController = new ImdbController(new Media());

        try {
            for(int i = 0; i < 15; i++) {

                imdbController.fetchImdbWebsiteCode("WandaVision");
                imdbController.setWebsiteCodeVersion();

                imdbController.fetchImdbWebsiteCode("Breaking Bad");
                imdbController.setWebsiteCodeVersion();

                imdbController.fetchImdbWebsiteCode("Loki");
                imdbController.setWebsiteCodeVersion();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
