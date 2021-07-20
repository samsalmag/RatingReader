package com.zemahzalek.ratingreader.controller.imdb;

import com.zemahzalek.ratingreader.model.Media;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ImdbController {

    IImdbController controller;

    Media media;
    private String mainUrl;
    private Document mainWebsiteCode;

    public ImdbController(Media media) {
        this.media = media;
    }

    public void setMedia(String mediaName) throws IOException {
        fetchImdbWebsiteCode(mediaName);
        setWebsiteCodeVersion();
        media.setUrl(mainUrl);
        media.setName(controller.fetchMediaName());
        media.setType(controller.fetchMediaType());
        media.setCategory(controller.fetchMediaCategory());
        media.setReleaseYear(controller.fetchMediaReleaseYear());
        media.setLength(controller.fetchMediaLength());

        if(media.isSeries()) {
            ImdbEpisodeController imdbEpisodeController = new ImdbEpisodeController(media, mainUrl);
            imdbEpisodeController.fetchEpisodeInformation();
        }
    }

    private Document fetchGoogleWebsiteCode(String searchTerm) throws IOException {
        searchTerm = searchTerm.replaceAll("\\s", "+");
        String googleSearchURL = "https://www.google.com/search?q=" + searchTerm + "+imdb";
        return Jsoup.connect(googleSearchURL).get();
    }

    public void fetchImdbWebsiteCode(String searchTerm) throws IOException {
        Document googleWebsiteCode = fetchGoogleWebsiteCode(searchTerm);
        Element firstResultURLDiv = googleWebsiteCode.getElementsByClass("yuRUbf").first();  // Gets div of the first google search result
        String firstResultURL = firstResultURLDiv.select("a").attr("href");   // Selects link element and gets URL from attribute
        mainUrl = firstResultURL;
        mainWebsiteCode = Jsoup.connect(firstResultURL)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0")
                .referrer("http://www.google.com")
                .maxBodySize(0)
                .get();
    }

    public IImdbController setWebsiteCodeVersion() {

        Elements old = mainWebsiteCode.select(".title_wrapper");
        Elements newest = mainWebsiteCode.select(".TitleHeader__TitleText-sc-1wu6n3d-0.dxSWFG");

        if(old.size() != 0) {
            controller = new OldImdbController(mainWebsiteCode);
        } else if(newest.size() != 0) {
            controller = new NewImdbController(mainWebsiteCode);
        } else {
            throw new NullPointerException();
        }

        return controller;
    }

    public IImdbController getController() {
        return controller;
    }
}
