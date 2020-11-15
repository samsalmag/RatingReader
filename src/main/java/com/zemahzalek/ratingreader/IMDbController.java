package com.zemahzalek.ratingreader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class IMDbController {

    private String mediaName;
    private String mediaType;
    private int mediaReleaseYear;
    private int nrSeasons;
    private String mediaSeasonHeader;
    private ArrayList<ArrayList<String>> mediaEpisodeRatings;

    private String IMDbMainURL;

    private Document googleWebpageCode;
    private Document IMDbMainWebpageCode;
    private Document IMDbSeasonWebpageCode;

    void setMedia(String mediaName) throws IOException {
        fetchGoogleWebpageCode(mediaName);
        fetchIMDbWebpageCode();
        fetchMediaName();
        fetchMediaType();
        fetchNrSeasons();
        fetchMediaReleaseYear();
        fetchSeasonHeader();
        fetchEpisodeRatings();
    }

    // ---------- FETCHERS ---------- //
    private void fetchGoogleWebpageCode(String searchTerm) throws IOException {
        searchTerm = searchTerm.replaceAll("\\s", "+");
        String googleSearchURL = "https://www.google.com/search?q=" + searchTerm + "+imdb";
        googleWebpageCode = Jsoup.connect(googleSearchURL).get();
    }

    private void fetchIMDbWebpageCode() throws IOException {
        Element firstResultURLDiv = googleWebpageCode.getElementsByClass("yuRUbf").first();  // Gets div of the first google search result
        String firstResultURL = firstResultURLDiv.select("a").attr("href");   // Selects link element and gets URL from attribute
        IMDbMainURL = firstResultURL;
        IMDbMainWebpageCode = Jsoup.connect(firstResultURL).get();
    }

    private void fetchIMDbSeasonWebpageCode(int season) throws IOException {
        if(!isTVSeries()) {
            return;
        }

        String IMDbSeasonURl = IMDbMainURL + "episodes?season=" + season;   // URL for the specific shows season
        IMDbSeasonWebpageCode = Jsoup.connect(IMDbSeasonURl).get();
    }

    private void fetchMediaName() {
        if(IMDbMainWebpageCode == null) {
            return;
        }

        mediaName = IMDbMainWebpageCode.getElementsByClass("title_wrapper").first().child(0).text();
    }

    private void fetchMediaType() {
        if(IMDbMainWebpageCode == null) {
            return;
        }

        String type = IMDbMainWebpageCode.getElementsByClass("subtext").first().select("a").last().text();    // Gets first "subtext" div and selects last element of type "a"
        type = type.replaceAll("[^A-Za-z]", "");               // Remove all non alphabetic characters

        // Corrects mediaType string
        if(type.equals("TVSeries")) {
            type = "TV Series";
        } else {
            type = "Movie";
        }

        mediaType = type;
    }

    private void fetchMediaReleaseYear() {
        if(IMDbMainWebpageCode == null) {
            return;
        }

        String releaseYear = IMDbMainWebpageCode.getElementsByClass("subtext").first().select("a").last().text();   // Gets first "subtext" div and selects last element of type "a"

        char firstChar = releaseYear.charAt(0);
        // Check if firstChar is a number
        if(firstChar >= '0' && firstChar <= '9') {
            releaseYear = releaseYear.replaceAll("[^\\d]", "");     // Remove all non numeric characters
            releaseYear = releaseYear.substring(releaseYear.length() - 4);            // Keeps only 4 last numbers (the year)
        } else {
            releaseYear = releaseYear.replaceAll("[^\\d]", "");     // Remove all non numeric characters
            releaseYear = releaseYear.substring(0,4);                                 // Cuts of excess year information
        }

        mediaReleaseYear = Integer.parseInt(releaseYear);
    }

    // TODO
    /*
    private void fetchMediaCategory() {
        Elements elementAs = IMDbMainWebpageCode.getElementsByClass("subtext").first().select("a");    // Gets first "subtext" div and selects last element of type "a"
        elementAs.remove(elementAs.size() - 1);

        String mediaCategory = null;
        mediaCategory = mediaCategory.replaceAll("[^A-Za-z]", "");      // Remove all non alphabetic characters
        return mediaCategory;
    }
    */

    private void fetchNrSeasons() {
        if(!isTVSeries()) {
            nrSeasons = 0;
            return;
        }

        String seasonsAndYearsDivName = "seasons-and-year-nav";
        int seasonDivIndex = 3;
        Element seasonsAndYearDiv = IMDbMainWebpageCode.getElementsByClass(seasonsAndYearsDivName).first();     // Get first div of that name
        Element seasonsDiv = seasonsAndYearDiv.child(seasonDivIndex);               // Get the seasons div which exist in the previous div
        nrSeasons = seasonsDiv.children().size();          // Get number of children of this div (number of seasons)
    }

    private void fetchSeasonHeader() throws IOException {
        if(!isTVSeries()) {
            return;
        }

        if(IMDbSeasonWebpageCode == null) {
            fetchIMDbSeasonWebpageCode(1);
        }

        String seasonHeaderID = "episode_top";
        mediaSeasonHeader = IMDbSeasonWebpageCode.getElementById(seasonHeaderID).text();
    }

    private void fetchEpisodeRatings() throws IOException {
        if(!isTVSeries()) {
            return;
        }

        if(nrSeasons == 0) {
            return;
        }

        if(IMDbSeasonWebpageCode == null) {
            fetchIMDbSeasonWebpageCode(1);
        }

        ArrayList<ArrayList<String>> episodeRatings = new ArrayList<>();

        for(int i = 1; i <= nrSeasons; i++) {
            fetchIMDbSeasonWebpageCode(i);
            ArrayList<String> seasonEpisodeRatings = new ArrayList<>();

            String ratingDivName = "ipl-rating-widget";
            String ratingClassName = "ipl-rating-star__rating";

            Elements ratingDivs = IMDbSeasonWebpageCode.select("div." + ratingDivName);   // Gets all divs with an episode rating
            for(Element div : ratingDivs) {
                String rating = div.getElementsByClass(ratingClassName).text();                  // Gets rating text from class in each div
                rating = rating.substring(0, 3);                                                 // Cuts off excess rating information
                seasonEpisodeRatings.add(rating);            // Adds rating to list
            }

            episodeRatings.add(seasonEpisodeRatings);
        }

        mediaEpisodeRatings = episodeRatings;
    }

    // -------- GETTERS -------- //

    public String getMediaName() {
        return mediaName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public int getMediaReleaseYear() {
        return mediaReleaseYear;
    }

    public int getNrSeasons() {
        return nrSeasons;
    }

    public String getMediaSeasonHeader() {
        return mediaSeasonHeader;
    }

    public ArrayList<String> getMediaEpisodeRatings(int season) {
        return mediaEpisodeRatings.get(season);
    }

    // -------- OTHER -------- //

    boolean isTVSeries() {
        return getMediaType().equals("TV Series");
    }
}
