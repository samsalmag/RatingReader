package com.zemahzalek.ratingreader.controller.imdb;

import com.zemahzalek.ratingreader.model.MediaType;

public interface IImdbController {

    String fetchMediaName();
    MediaType fetchMediaType();
    String fetchMediaCategory();
    int fetchMediaReleaseYear();
    String fetchMediaLength();
}
