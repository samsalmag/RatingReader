package com.zemahzalek.ratingreader.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class Episode {

    private Media media;
    private int seasonNr;
    private int episodeNr;
    private String url;
    private String name;
    private String rating;
    private String length;

    public Episode(Media media) {
        this.media = media;
    }

    public void fetchLength() throws IOException {
        Document websiteCode = Jsoup.connect(url).get();
        Element subtextInformationDiv = websiteCode.getElementsByClass("subtext").first();
        String length = subtextInformationDiv.select("time").text();
        this.length = length;
    }

    // ---------------------------- GETTERS ---------------------------- //
    public Media getMedia() {
        return media;
    }

    public int getSeasonNr() {
        return seasonNr;
    }

    public int getEpisodeNr() {
        return episodeNr;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getLength() {
        return length;
    }

    // ---------------------------- SETTERS ---------------------------- //
    public void setSeasonNr(int seasonNr) {
        this.seasonNr = seasonNr;
    }

    public void setEpisodeNr(int episodeNr) {
        this.episodeNr = episodeNr;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
