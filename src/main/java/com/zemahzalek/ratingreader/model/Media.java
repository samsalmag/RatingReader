package com.zemahzalek.ratingreader.model;

import java.util.ArrayList;

public class Media {

    private String name;
    private MediaType type;
    private String category;
    private int releaseYear;
    private String length;
    private String url;

    private EpisodeGroupType episodeGroupType;
    private int nrEpisodes;
    private ArrayList<String> episodeGroups;
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

    public EpisodeGroupType getEpisodeGroupType() {
        return episodeGroupType;
    }

    public int getNrEpisodes() {
        return nrEpisodes;
    }

    public ArrayList<String> getEpisodeGroups() {
        return episodeGroups;
    }

    public ArrayList<ArrayList<Episode>> getEpisodes() {
        return episodes;
    }

    public String getUrl() {
        return url;
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

    public void setEpisodeGroupType(EpisodeGroupType episodeGroupType) {
        this.episodeGroupType = episodeGroupType;
    }

    public void setNrEpisodes(int nrEpisodes) {
        this.nrEpisodes = nrEpisodes;
    }

    public void setEpisodeGroups(ArrayList<String> episodeGroups) {
        this.episodeGroups = episodeGroups;
    }

    public void setEpisodes(ArrayList<ArrayList<Episode>> episodes) {
        this.episodes = episodes;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
