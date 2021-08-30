package com.samsalek.ratingreader.controller.imdb;

import com.samsalek.ratingreader.model.MediaType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.StringJoiner;

public class OldImdbController implements IImdbController {

    private Document mainWebsiteCode;

    OldImdbController(Document websiteCode) {
        this.mainWebsiteCode = websiteCode;
    }

    @Override
    public String fetchMediaName() {
        String name = mainWebsiteCode.getElementsByClass("title_wrapper").first().child(0).text();
        return name;
    }

    @Override
    public MediaType fetchMediaType() {
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

        return mediaType;
    }

    @Override
    public String fetchMediaCategory() {
        Elements categories = mainWebsiteCode.getElementsByClass("subtext").first().select("a");    // Gets first "subtext" div and selects all elements of type "a".
        categories.remove(categories.size() - 1);                                                                 // Remove last element as it isn't a category (last one is release date).

        StringJoiner sj = new StringJoiner(", ");
        for (Element category : categories) {
            sj.add(category.text());
        }

        //mediaCategory = mediaCategory.replaceAll("[^A-Za-z]", "");      // Remove all non alphabetic characters
        return sj.toString();
    }

    @Override
    public int fetchMediaReleaseYear() {
        String releaseYear = mainWebsiteCode.getElementsByClass("subtext").first().select("a").last().text();   // Gets first "subtext" div and selects last element of type "a".
        releaseYear = releaseYear.replaceAll("[^\\d]", "");     // Remove all non numeric characters

        // If no release year is found
        if(releaseYear.equals("") || releaseYear == null) {
            return -1;
        }

        if(releaseYear.length() > 4) {
            releaseYear = releaseYear.substring(releaseYear.length() - 4);            // Keeps only 4 last numbers (the year)
        }

        return Integer.parseInt(releaseYear);
    }

    @Override
    public String fetchMediaLength() {
        String length = mainWebsiteCode.getElementsByClass("subtext").first().select("time").text();        // Gets first "subtext" div and selects "time" element.
        return length;
    }
}
