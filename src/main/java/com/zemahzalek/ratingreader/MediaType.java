package com.zemahzalek.ratingreader;

public enum MediaType {
    MOVIE, SERIES, MINISERIES, OTHER;

    // Converts to string with first letter uppercase and rest lowercase
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
