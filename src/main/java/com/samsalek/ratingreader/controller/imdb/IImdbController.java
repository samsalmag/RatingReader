package com.samsalek.ratingreader.controller.imdb;

import com.samsalek.ratingreader.model.MediaType;

public interface IImdbController {

    String fetchMediaName();
    MediaType fetchMediaType();
    String fetchMediaCategory();
    int fetchMediaReleaseYear();
    String fetchMediaLength();
}
