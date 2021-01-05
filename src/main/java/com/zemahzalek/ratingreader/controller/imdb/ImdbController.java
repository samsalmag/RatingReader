package com.zemahzalek.ratingreader.controller.imdb;

import com.zemahzalek.ratingreader.model.Media;
import com.zemahzalek.ratingreader.model.MediaType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.StringJoiner;

public class ImdbController {

    Media media;
    private String mainUrl;
    private Document mainWebsiteCode;

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
            ImdbEpisodeController imdbEpisodeController = new ImdbEpisodeController(media, mainUrl);
            imdbEpisodeController.fetchEpisodeInformation();
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
        if(releaseYear.equals("") || releaseYear == null) {
            return;
        }

        if(releaseYear.length() > 4) {
            releaseYear = releaseYear.substring(releaseYear.length() - 4);            // Keeps only 4 last numbers (the year)
        }

        media.setReleaseYear(Integer.parseInt(releaseYear));
    }

    private void fetchMediaLength() {
        String length = mainWebsiteCode.getElementsByClass("subtext").first().select("time").text();        // Gets first "subtext" div and selects "time" element.
        media.setLength(length);
    }
}
