package com.zemahzalek.ratingreader;

import java.util.ArrayList;

public class Media {

    private String name;
    private MediaType type;
    private int releaseYear;
    private int nrSeasons;
    private int nrEpisodes;
    private ArrayList<Integer> nrEpisodesPerSeason;
    private ArrayList<ArrayList<String>> episodeUrls;
    private ArrayList<ArrayList<String>> episodeNames;
    private ArrayList<ArrayList<String>> episodeRatings;

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

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getNrSeasons() {
        return nrSeasons;
    }

    public int getNrEpisodes() {
        return nrEpisodes;
    }

    public ArrayList<Integer> getNrEpisodesPerSeason() {
        return nrEpisodesPerSeason;
    }

    public ArrayList<ArrayList<String>> getEpisodeUrls() {
        return episodeUrls;
    }

    public ArrayList<ArrayList<String>> getEpisodeNames() {
        return episodeNames;
    }

    public ArrayList<ArrayList<String>> getEpisodeRatings() {
        return episodeRatings;
    }


    // ---------------------------- SETTERS ---------------------------- //
    public void setName(String name) {
        this.name = name;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setNrSeasons(int nrSeasons) {
        this.nrSeasons = nrSeasons;
    }

    public void setNrEpisodes(int nrEpisodes) {
        this.nrEpisodes = nrEpisodes;
    }

    public void setNrEpisodesPerSeason(ArrayList<Integer> nrEpisodesPerSeason) {
        this.nrEpisodesPerSeason = nrEpisodesPerSeason;
    }

    public void setEpisodeUrls(ArrayList<ArrayList<String>> episodeUrls) {
        this.episodeUrls = episodeUrls;
    }

    public void setEpisodeNames(ArrayList<ArrayList<String>> episodeNames) {
        this.episodeNames = episodeNames;
    }

    public void setEpisodeRatings(ArrayList<ArrayList<String>> episodeRatings) {
        this.episodeRatings = episodeRatings;
    }
}
