package com.zemahzalek.ratingreader.controller;

import com.zemahzalek.ratingreader.model.Episode;
import com.zemahzalek.ratingreader.model.Media;
import com.zemahzalek.ratingreader.model.MediaType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;

public class ImdbController {

    Media media;
    private String mainUrl;

    private Document mainWebsiteCode;
    private ArrayList<Document> seasonsWebsiteCode;

    public ImdbController(Media media) {
        this.media = media;
    }

    public void setMedia(String mediaName) throws IOException {
        fetchImdbWebsiteCode(mediaName);
        fetchMediaName();
        fetchMediaType();
        fetchMediaCategory();
        fetchMediaReleaseYear();
        fetchMediaLength();

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
        String name = mainWebsiteCode.getElementsByClass("title_wrapper").first().child(0).text();
        media.setName(name);
    }

    private void fetchMediaType() {
        String type = mainWebsiteCode.getElementsByClass("subtext").first().select("a").last().text();    // Gets first "subtext" div and selects last element of type "a"
        type = type.replaceAll("[^A-Za-z]", "");                                                           // Remove all non alphabetic characters

        // Sets correct mediaType
        MediaType mediaType;
        if(type.equalsIgnoreCase("TVSeries")) {
            mediaType = MediaType.SERIES;
        }
        else if(type.equalsIgnoreCase("TVMiniSeries")) {
            mediaType = MediaType.MINISERIES;
        }
        else if(type.contains("Videogame")) {
            mediaType = MediaType.VIDEOGAME;
        }
        else {
            mediaType = MediaType.MOVIE;
        }

        media.setType(mediaType);
    }

    private void fetchMediaCategory() {
        Elements categories = mainWebsiteCode.getElementsByClass("subtext").first().select("a");    // Gets first "subtext" div and selects all elements of type "a".
        categories.remove(categories.size() - 1);                                                                 // Remove last element as it isn't a category (last one is release date).

        StringJoiner sj = new StringJoiner(", ");
        for (Element category : categories) {
            sj.add(category.text());
        }

        //mediaCategory = mediaCategory.replaceAll("[^A-Za-z]", "");      // Remove all non alphabetic characters
        media.setCategory(sj.toString());
    }


    private void fetchMediaReleaseYear() {
        String releaseYear = mainWebsiteCode.getElementsByClass("subtext").first().select("a").last().text();   // Gets first "subtext" div and selects last element of type "a".
        releaseYear = releaseYear.replaceAll("[^\\d]", "");     // Remove all non numeric characters

        // If no release year is found
        if(releaseYear.equals("")) {
            return;
        }

        if(releaseYear.length() > 4) {
            releaseYear = releaseYear.substring(releaseYear.length() - 4);            // Keeps only 4 last numbers (the year)
        }

        media.setReleaseYear(Integer.parseInt(releaseYear));
    }

    // TODO
    private void fetchMediaLength() {
        String length = mainWebsiteCode.getElementsByClass("subtext").first().select("time").text();        // Gets first "subtext" div and selects "time" element.
        media.setLength(length);
        System.out.println(length);
    }

    private void fetchNrSeasons() {
        String seasonsAndYearsDivName = "seasons-and-year-nav";
        int seasonDivIndex = 3;
        Element seasonsAndYearDiv = mainWebsiteCode.getElementsByClass(seasonsAndYearsDivName).first();     // Get first div of that name
        Element seasonsDiv = seasonsAndYearDiv.child(seasonDivIndex);               // Get the seasons div which exist in the previous div
        int nrSeasons = seasonsDiv.children().size();          // Get number of children of this div (number of seasons)
        media.setNrSeasons(nrSeasons);
    }

    private void fetchNrEpisodesPerSeason() {
        media.setEpisodes(new ArrayList<>());               // Creates and sets new episodes list

        for (int s = 0; s < media.getNrSeasons(); s++) {
            media.getEpisodes().add(new ArrayList<>());     // Adds each season to episodes list

            Element episodesDiv = seasonsWebsiteCode.get(s).getElementsByClass("list detail eplist").first();
            while(media.getEpisodes().get(s).size() < episodesDiv.children().size()) {      // While number of episodes in episodes list is less than episodes from div
                media.getEpisodes().get(s).add(new Episode(media));
            }
        }
    }

    private void fetchNrEpisodes() {
        if(!media.isSeries()) {
            media.setNrEpisodes(0);
        }

        int nrEpisodes = 0;
        if(nrEpisodes == 0) {
            for (ArrayList<Episode> season : media.getEpisodes()) {
                nrEpisodes += season.size();
            }
        }

        media.setNrEpisodes(nrEpisodes);
    }

    private void fetchEpisodeInfo() {

        // Loop through each season
        for (int s = 0; s < media.getNrSeasons(); s++) {

            // Loop through each episode in the season
            Element episodesDiv = seasonsWebsiteCode.get(s).getElementsByClass("list detail eplist").first();
            for(int e = 0; e < episodesDiv.children().size(); e++) {

                // SET SEASON AND EPISODE NUMBER
                media.getEpisodes().get(s).get(e).setSeasonNr(s+1);
                media.getEpisodes().get(s).get(e).setEpisodeNr(e+1);

                // URLs
                String url = episodesDiv.child(e).select("strong").select("a").attr("abs:href");
                media.getEpisodes().get(s).get(e).setUrl(url);

                // NAMES
                String name = episodesDiv.child(e).select("strong").select("a").text();
                media.getEpisodes().get(s).get(e).setName(name);

                // RATINGS
                String rating;
                if(episodesDiv.child(e).getElementsByClass("ipl-rating-star__rating").first() == null) {            // If no rating exists
                    rating = "NA";
                }
                else {
                    rating = episodesDiv.child(e).getElementsByClass("ipl-rating-star__rating").first().text();    // Gets all divs with an episode rating
                    rating = rating.substring(0, 3);                                                                          // Cuts off excess rating information
                }
                media.getEpisodes().get(s).get(e).setRating(rating);                                                          // Adds rating to list
            }
        }
    }
}
