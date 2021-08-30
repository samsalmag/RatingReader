package com.samsalek.ratingreader;

import com.samsalek.ratingreader.controller.imdb.IImdbController;
import com.samsalek.ratingreader.controller.imdb.ImdbHandler;
import com.samsalek.ratingreader.controller.imdb.NewImdbController;
import com.samsalek.ratingreader.controller.imdb.OldImdbController;
import com.samsalek.ratingreader.model.Media;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ImdbHandlerTest {

    ImdbHandler imdbHandler = new ImdbHandler(new Media());
    IImdbController controller;
    int oldCount, newCount = 0;

    private void reset() {
        imdbHandler = new ImdbHandler(new Media());
        controller = null;
        oldCount = 0;
        newCount = 0;
    }

    @Test
    public void setWebsiteCodeVersionTest() {
        for(int i = 0; i < 10; i++) {
            search("WandaVision");
            search("Loki");
            search("Black Sails");
            search("Breaking Bad");
            search("Game of Thrones");
        }

        System.out.println("Old controllers: " + oldCount);
        System.out.println("New controllers: " + newCount);
        reset();
    }

    private void search(String searchTerm) {
        try {
            imdbHandler.fetchImdbWebsiteCode(searchTerm);
            controller = imdbHandler.setWebsiteCodeVersion();

            if(controller instanceof OldImdbController) {
                oldCount++;
            } else if(controller instanceof NewImdbController) {
                newCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
