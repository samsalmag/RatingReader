package com.zemahzalek.ratingreader.model;

import java.util.ArrayList;

public class Media {

    private String name;
    private MediaType type;
    private String category;
    private int releaseYear;
    private String length;
    private int nrSeasons;
    private int nrEpisodes;
    private ArrayList<ArrayList<Episode>> episodes;

    public boolean isSeries() {
        return type.equals(MediaType.SERIES) || type.equals(MediaType.MINISERIES);
    }


    // ---------------------------- GETTERS ---------------------------- //
    public String getName() {
        return name;
    }

    public MediaType getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getLength() {
        return length;
    }

    public int getNrSeasons() {
        if(!isSeries()) {
            return 0;
        }
        return nrSeasons;
    }

    public int getNrEpisodes() {
        return nrEpisodes;
    }

    public ArrayList<ArrayList<Episode>> getEpisodes() {
        return episodes;
    }

    // ---------------------------- SETTERS ---------------------------- //
    public void setName(String name) {
        this.name = name;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setNrSeasons(int nrSeasons) {
        this.nrSeasons = nrSeasons;
    }

    public void setNrEpisodes(int nrEpisodes) {
        this.nrEpisodes = nrEpisodes;
    }

    public void setEpisodes(ArrayList<ArrayList<Episode>> episodes) {
        this.episodes = episodes;
    }
}
