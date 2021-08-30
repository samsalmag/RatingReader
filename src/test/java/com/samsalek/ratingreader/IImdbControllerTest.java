package com.samsalek.ratingreader;

import com.samsalek.ratingreader.controller.imdb.IImdbController;
import com.samsalek.ratingreader.controller.imdb.ImdbHandler;
import com.samsalek.ratingreader.model.MediaType;
import com.samsalek.ratingreader.model.Media;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class IImdbControllerTest {

    ImdbHandler imdbHandler = new ImdbHandler(new Media());
    IImdbController controller;

    @Test
    public void fetchMediaName() {
        try {
            imdbHandler.fetchImdbWebsiteCode("WandaVision");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("WandaVision", controller.fetchMediaName());

            imdbHandler.fetchImdbWebsiteCode("Game of thrones");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Game of Thrones", controller.fetchMediaName());

            imdbHandler.fetchImdbWebsiteCode("Loki");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Loki", controller.fetchMediaName());

            imdbHandler.fetchImdbWebsiteCode("God of war");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("God of War", controller.fetchMediaName());

            imdbHandler.fetchImdbWebsiteCode("Avengers endgame");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Avengers: Endgame", controller.fetchMediaName());

            imdbHandler.fetchImdbWebsiteCode("Spiderman far from home");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Spider-Man: Far from Home", controller.fetchMediaName());

            imdbHandler.fetchImdbWebsiteCode("Black sails");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Black Sails", controller.fetchMediaName());

            imdbHandler.fetchImdbWebsiteCode("Breaking bad");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Breaking Bad", controller.fetchMediaName());

            imdbHandler.fetchImdbWebsiteCode("Fury");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Fury", controller.fetchMediaName());

            imdbHandler.fetchImdbWebsiteCode("Transformers age of extinction");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Transformers: Age of Extinction", controller.fetchMediaName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fetchMediaType() {
        try {
            imdbHandler.fetchImdbWebsiteCode("WandaVision");
            controller = imdbHandler.setWebsiteCodeVersion();
            Assertions.assertEquals(MediaType.MINISERIES, controller.fetchMediaType());

            imdbHandler.fetchImdbWebsiteCode("Game of thrones");
            controller = imdbHandler.setWebsiteCodeVersion();
            Assertions.assertEquals(MediaType.SERIES, controller.fetchMediaType());

            imdbHandler.fetchImdbWebsiteCode("Loki");
            controller = imdbHandler.setWebsiteCodeVersion();
            Assertions.assertEquals(MediaType.SERIES, controller.fetchMediaType());

            imdbHandler.fetchImdbWebsiteCode("God of war");
            controller = imdbHandler.setWebsiteCodeVersion();
            Assertions.assertEquals(MediaType.VIDEOGAME, controller.fetchMediaType());

            imdbHandler.fetchImdbWebsiteCode("Avengers endgame");
            controller = imdbHandler.setWebsiteCodeVersion();
            Assertions.assertEquals(MediaType.MOVIE, controller.fetchMediaType());

            imdbHandler.fetchImdbWebsiteCode("Spiderman far from home");
            controller = imdbHandler.setWebsiteCodeVersion();
            Assertions.assertEquals(MediaType.MOVIE, controller.fetchMediaType());

            imdbHandler.fetchImdbWebsiteCode("Black sails");
            controller = imdbHandler.setWebsiteCodeVersion();
            Assertions.assertEquals(MediaType.SERIES, controller.fetchMediaType());

            imdbHandler.fetchImdbWebsiteCode("Breaking bad");
            controller = imdbHandler.setWebsiteCodeVersion();
            Assertions.assertEquals(MediaType.SERIES, controller.fetchMediaType());

            imdbHandler.fetchImdbWebsiteCode("Fury");
            controller = imdbHandler.setWebsiteCodeVersion();
            Assertions.assertEquals(MediaType.MOVIE, controller.fetchMediaType());

            imdbHandler.fetchImdbWebsiteCode("Transformers age of extinction");
            controller = imdbHandler.setWebsiteCodeVersion();
            Assertions.assertEquals(MediaType.MOVIE, controller.fetchMediaType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fetchMediaCategory(){
        try {
            imdbHandler.fetchImdbWebsiteCode("WandaVision");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Action, Comedy, Drama", controller.fetchMediaCategory());

            imdbHandler.fetchImdbWebsiteCode("Game of thrones");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Action, Adventure, Drama", controller.fetchMediaCategory());

            imdbHandler.fetchImdbWebsiteCode("Loki");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Action, Adventure, Fantasy", controller.fetchMediaCategory());

            imdbHandler.fetchImdbWebsiteCode("God of war");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Action, Adventure, Drama", controller.fetchMediaCategory());

            imdbHandler.fetchImdbWebsiteCode("Avengers endgame");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Action, Adventure, Drama", controller.fetchMediaCategory());

            imdbHandler.fetchImdbWebsiteCode("Spiderman far from home");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Action, Adventure, Sci-Fi", controller.fetchMediaCategory());

            imdbHandler.fetchImdbWebsiteCode("Black sails");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Adventure, Drama", controller.fetchMediaCategory());

            imdbHandler.fetchImdbWebsiteCode("Breaking bad");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Crime, Drama, Thriller", controller.fetchMediaCategory());

            imdbHandler.fetchImdbWebsiteCode("Fury");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Action, Drama, War", controller.fetchMediaCategory());

            imdbHandler.fetchImdbWebsiteCode("Transformers age of extinction");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("Action, Adventure, Sci-Fi", controller.fetchMediaCategory());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fetchMediaReleaseYear() {
        try {
            imdbHandler.fetchImdbWebsiteCode("WandaVision");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals(2021, controller.fetchMediaReleaseYear());

            imdbHandler.fetchImdbWebsiteCode("Game of thrones");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals(2011, controller.fetchMediaReleaseYear());

            imdbHandler.fetchImdbWebsiteCode("Loki");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals(2021, controller.fetchMediaReleaseYear());

            imdbHandler.fetchImdbWebsiteCode("God of war");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals(2018, controller.fetchMediaReleaseYear());

            imdbHandler.fetchImdbWebsiteCode("Avengers endgame");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals(2019, controller.fetchMediaReleaseYear());

            imdbHandler.fetchImdbWebsiteCode("Spiderman far from home");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals(2019, controller.fetchMediaReleaseYear());

            imdbHandler.fetchImdbWebsiteCode("Black sails");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals(2014, controller.fetchMediaReleaseYear());

            imdbHandler.fetchImdbWebsiteCode("Breaking bad");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals(2008, controller.fetchMediaReleaseYear());

            imdbHandler.fetchImdbWebsiteCode("Fury");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals(2014, controller.fetchMediaReleaseYear());

            imdbHandler.fetchImdbWebsiteCode("Transformers age of extinction");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals(2014, controller.fetchMediaReleaseYear());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fetchMediaLength() {
        try {
            imdbHandler.fetchImdbWebsiteCode("WandaVision");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("5h 50min", controller.fetchMediaLength());

            imdbHandler.fetchImdbWebsiteCode("Game of thrones");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("57min", controller.fetchMediaLength());

            imdbHandler.fetchImdbWebsiteCode("Loki");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("", controller.fetchMediaLength());

            imdbHandler.fetchImdbWebsiteCode("God of war");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("", controller.fetchMediaLength());

            imdbHandler.fetchImdbWebsiteCode("Avengers endgame");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("3h 1min", controller.fetchMediaLength());

            imdbHandler.fetchImdbWebsiteCode("Spiderman far from home");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("2h 9min", controller.fetchMediaLength());

            imdbHandler.fetchImdbWebsiteCode("Black sails");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("56min", controller.fetchMediaLength());

            imdbHandler.fetchImdbWebsiteCode("Breaking bad");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("49min", controller.fetchMediaLength());

            imdbHandler.fetchImdbWebsiteCode("Fury");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("2h 14min", controller.fetchMediaLength());

            imdbHandler.fetchImdbWebsiteCode("Transformers age of extinction");
            controller = imdbHandler.setWebsiteCodeVersion();
            assertEquals("2h 45min", controller.fetchMediaLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
