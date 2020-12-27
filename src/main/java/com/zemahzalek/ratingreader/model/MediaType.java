package com.zemahzalek.ratingreader.model;

public enum MediaType {
    MOVIE("Movie"), SERIES("Series"), MINISERIES("Miniseries"), VIDEOGAME("Video Game"), OTHER("Other");

    private String name;

    MediaType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
