package com.zemahzalek.ratingreader.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class Episode {

    private Media media;
    private int episodeGroupNr;
    private int episodeNr;
    private String url;
    private String name;
    private String rating;
    private String length;
    private String airdate;
    private String nrRatings;

    public Episode(Media media) {
        this.media = media;
    }

    public void fetchLength() throws IOException {
        Document websiteCode = Jsoup.connect(url).get();
        Element subtextInformationDiv = websiteCode.getElementsByClass("TitleBlock__TitleMetaDataContainer-sc-1nlhx7j-4 cgfrOx").first();
        //Element subtextInformationDiv2 = subtextInformationDiv.get(0);
        //String length = subtextInformationDiv.getAllElements().last().text();
        this.length = length;
    }

    // ---------------------------- GETTERS ---------------------------- //
    public Media getMedia() {
        return media;
    }

    public int getEpisodeGroupNr() {
        return episodeGroupNr;
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

    public String getAirdate() {
        return airdate;
    }

    public String getNrRatings() {
        return nrRatings;
    }

    // ---------------------------- SETTERS ---------------------------- //
    public void setEpisodeGroupNr(int episodeGroupNr) {
        this.episodeGroupNr = episodeGroupNr;
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

    public void setAirdate(String airdate) {
        this.airdate = airdate;
    }

    public void setNrRatings(String nrRatings) {
        this.nrRatings = nrRatings;
    }
}
