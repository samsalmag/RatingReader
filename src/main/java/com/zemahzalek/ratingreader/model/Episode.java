package com.zemahzalek.ratingreader.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        String c = "TitleBlock__TitleMetaDataContainer-sc-1nlhx7j-2 hWHMKr";
        c = "." + c.replaceAll("\\s+", ".");

        Elements elements = websiteCode.select(c);

        Element informationContainer = elements.first().child(0);
        if(informationContainer.children().size() == 0) {
            return;
        }
        String length = informationContainer.children().last().text();

        if(Character.isDigit(length.charAt(0))) {
            this.length = length;
        } else {
            this.length = "NA";
        }
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
