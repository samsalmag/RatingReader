package com.zemahzalek.ratingreader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class ImdbController {

    Media media;

    private String mainUrl;

    private Document mainWebsiteCode;
    private ArrayList<Document> seasonsWebsiteCode;

    public ImdbController(Media media) {
        this.media = media;
    }

    void setMedia(String mediaName) throws IOException {
        fetchImdbWebsiteCode(mediaName);
        fetchMediaName();
        fetchMediaType();
        fetchMediaReleaseYear();
        //fetchMediaCategory();

       if(media.isSeries()) {
            fetchNrSeasons();
            fetchSeasonsWebsiteCode();
            fetchNrEpisodesPerSeason();
            fetchNrEpisodes();
            fetchEpisodeInfo();
        }
    }

    // ---------- FETCHERS ---------- //
    private Document fetchGoogleWebsiteCode(String searchTerm) throws IOException {
        searchTerm = searchTerm.replaceAll("\\s", "+");
        String googleSearchURL = "https://www.google.com/search?q=" + searchTerm + "+imdb";
        return Jsoup.connect(googleSearchURL).get();
    }

    private void fetchImdbWebsiteCode(String searchTerm) throws IOException {
        Document googleWebsiteCode = fetchGoogleWebsiteCode(searchTerm);
        Element firstResultURLDiv = googleWebsiteCode.getElementsByClass("yuRUbf").first();  // Gets div of the first google search result
        String firstResultURL = firstResultURLDiv.select("a").attr("href");   // Selects link element and gets URL from attribute
        mainUrl = firstResultURL;
        mainWebsiteCode = Jsoup.connect(firstResultURL).get();
    }

    private void fetchSeasonsWebsiteCode() throws IOException {

        ArrayList<Document> websiteCodes = new ArrayList<>();
        for(int i = 1; i <= media.getNrSeasons(); i++) {
            String IMDbSeasonURl = mainUrl + "episodes?season=" + i;   // URL for the specific shows season
            Document websiteCode = Jsoup.connect(IMDbSeasonURl).get();
            websiteCodes.add(websiteCode);
        }
        seasonsWebsiteCode = websiteCodes;

    }

    private void fetchMediaName() {
        if(mainWebsiteCode == null) {
            return;
        }

        String name = mainWebsiteCode.getElementsByClass("title_wrapper").first().child(0).text();
        media.setName(name);
    }

    private void fetchMediaType() {
        if(mainWebsiteCode == null) {
            return;
        }

        String type = mainWebsiteCode.getElementsByClass("subtext").first().select("a").last().text();    // Gets first "subtext" div and selects last element of type "a"
        type = type.replaceAll("[^A-Za-z]", "");                                                           // Remove all non alphabetic characters

        // Sets correct mediaType
        MediaType mediaType;
        switch (type) {
            case "Movie":
                mediaType = MediaType.MOVIE;
                break;

            case "TVSeries":
                mediaType = MediaType.SERIES;
                break;

            case "TVMiniSeries":
                mediaType = MediaType.MINISERIES;
                break;

            default:
                mediaType = MediaType.OTHER;
                break;
        }

        media.setType(mediaType);
    }

    private void fetchMediaReleaseYear() {
        if(mainWebsiteCode == null) {
            return;
        }

        String releaseYear = mainWebsiteCode.getElementsByClass("subtext").first().select("a").last().text();   // Gets first "subtext" div and selects last element of type "a"

        char firstChar = releaseYear.charAt(0);
        // Check if firstChar is a number
        if(firstChar >= '0' && firstChar <= '9') {
            releaseYear = releaseYear.replaceAll("[^\\d]", "");     // Remove all non numeric characters
            releaseYear = releaseYear.substring(releaseYear.length() - 4);            // Keeps only 4 last numbers (the year)
        } else {
            releaseYear = releaseYear.replaceAll("[^\\d]", "");     // Remove all non numeric characters
            releaseYear = releaseYear.substring(0,4);                                 // Cuts of excess year information
        }

        media.setReleaseYear(Integer.parseInt(releaseYear));
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

        String seasonsAndYearsDivName = "seasons-and-year-nav";
        int seasonDivIndex = 3;
        Element seasonsAndYearDiv = mainWebsiteCode.getElementsByClass(seasonsAndYearsDivName).first();     // Get first div of that name
        Element seasonsDiv = seasonsAndYearDiv.child(seasonDivIndex);               // Get the seasons div which exist in the previous div
        int nrSeasons = seasonsDiv.children().size();          // Get number of children of this div (number of seasons)
        media.setNrSeasons(nrSeasons);
    }

    private void fetchNrEpisodesPerSeason() {

        ArrayList<Integer> episodeAmount = new ArrayList<>();
        for (int i = 0; i < media.getNrSeasons(); i++) {

            Element episodesDiv = seasonsWebsiteCode.get(i).getElementsByClass("list detail eplist").first();
            Integer nrEpisodes = 0;
            for(int j = 0; j < episodesDiv.children().size(); j++) {
                nrEpisodes++;
            }

            episodeAmount.add(nrEpisodes);
        }
        media.setNrEpisodesPerSeason(episodeAmount);
    }

    private void fetchNrEpisodes() {
        int nrEpisodes = 0;
        for (Integer nrEpisodesPerSeason : media.getNrEpisodesPerSeason()) {
            nrEpisodes += nrEpisodesPerSeason;
        }
        media.setNrEpisodes(nrEpisodes);
    }

    private void fetchEpisodeInfo() {

        ArrayList<ArrayList<String>> episodeUrls = new ArrayList<>();
        ArrayList<ArrayList<String>> episodeNames = new ArrayList<>();
        ArrayList<ArrayList<String>> episodeRatings = new ArrayList<>();

        // Loop through each season
        for (int i = 0; i < media.getNrSeasons(); i++) {

            ArrayList<String> seasonEpisodeUrls = new ArrayList<>();
            ArrayList<String> seasonEpisodeNames = new ArrayList<>();
            ArrayList<String> seasonEpisodeRatings = new ArrayList<>();

            // Loop through each episode in the season
            Element episodesDiv = seasonsWebsiteCode.get(i).getElementsByClass("list detail eplist").first();
            for(Element episodeDiv : episodesDiv.children()) {
                // URLs
                String url = episodeDiv.select("strong").select("a").attr("abs:href");
                seasonEpisodeUrls.add(url);

                // NAMES
                String name = episodeDiv.select("strong").select("a").text();
                seasonEpisodeNames.add(name);

                // RATINGS
                String rating;
                if(episodeDiv.getElementsByClass("ipl-rating-star__rating").first() == null) {            // If no rating exists
                    rating = "NA";
                }
                else {
                    rating = episodeDiv.getElementsByClass("ipl-rating-star__rating").first().text();    // Gets all divs with an episode rating
                    rating = rating.substring(0, 3);                                                                // Cuts off excess rating information
                }
                seasonEpisodeRatings.add(rating);                                                                   // Adds rating to list
            }

            // Add each season to total list
            episodeUrls.add(seasonEpisodeUrls);
            episodeNames.add(seasonEpisodeNames);
            episodeRatings.add(seasonEpisodeRatings);
        }

        // Replace null list in media class with new total list
        media.setEpisodeUrls(episodeUrls);
        media.setEpisodeNames(episodeNames);
        media.setEpisodeRatings(episodeRatings);
    }
}
