package com.zemahzalek.ratingreader.controller.imdb;

import com.zemahzalek.ratingreader.model.Episode;
import com.zemahzalek.ratingreader.model.EpisodeGroupType;
import com.zemahzalek.ratingreader.model.Media;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class ImdbEpisodeController {

    private Media media;
    private String mainUrl;
    private ArrayList<Document> episodeGroupsWebsiteCode;

    public ImdbEpisodeController(Media media, String mainUrl) {
        this.media = media;
        this.mainUrl = mainUrl;
    }

    public void fetchEpisodeInformation() throws IOException {
        fetchEpisodeGroupType();
        fetchEpisodeGroups();
        fetchEpisodeGroupsWebsiteCode();
        createEpisodes();
        fetchEpisodeInfo();
    }

    private void fetchEpisodeGroupType() throws IOException {
        // Connects to "mainUrl + 'episodes'" which then redirects to correct episode group url (either season or year)
        String imdbEpisodeGroupUrl = Jsoup.connect(mainUrl + "episodes").get().location();      // URL for the specific shows group
        String urlEnding = imdbEpisodeGroupUrl.substring(imdbEpisodeGroupUrl.length() - 9);         // Save the last part of url to determine episode group type

        if(urlEnding.contains("year=")) {
            media.setEpisodeGroupType(EpisodeGroupType.YEAR);
        }
        else {
            media.setEpisodeGroupType(EpisodeGroupType.SEASON);
        }
    }

    private void fetchEpisodeGroups() throws IOException {
        Document episodeGroupWebsiteCode = Jsoup.connect(mainUrl + "episodes").get();

        String episodeGroupDivId;
        if(media.getEpisodeGroupType() == EpisodeGroupType.SEASON) {
            episodeGroupDivId = "bySeason";
        }
        else  {
            episodeGroupDivId = "byYear";
        }

        Element seasonAndYearDiv = episodeGroupWebsiteCode.getElementsByClass("seasonAndYearNav").first();     // Get first div of that name
        Element episodeGroupDiv = seasonAndYearDiv.getElementById(episodeGroupDivId);                                    // Get the episode group div which exist in the previous div

        media.setEpisodeGroups(new ArrayList<>());
        for (Element episodeGroup : episodeGroupDiv.children()) {       // For each episode group
            media.getEpisodeGroups().add(episodeGroup.text());
        }
    }

    private void fetchEpisodeGroupsWebsiteCode() throws IOException {
        episodeGroupsWebsiteCode = new ArrayList<>();
        for (String episodeGroup : media.getEpisodeGroups()) {

            String imdbSeasonURl = mainUrl + "episodes?" + media.getEpisodeGroupType().toString().toLowerCase() + "=";  // URL for the specific shows episode group
            if(!episodeGroup.equals("Unknown")) {
                imdbSeasonURl = imdbSeasonURl + episodeGroup;
            } else {
                imdbSeasonURl = imdbSeasonURl + "-1";           // If episode group equals "Unknown", then it's called "-1" in url.
            }

            Document websiteCode = Jsoup.connect(imdbSeasonURl).get();
            episodeGroupsWebsiteCode.add(websiteCode);
        }
    }

    private void createEpisodes() {
        media.setEpisodes(new ArrayList<>());               // Creates and sets new episodes list
        int nrEpisodes = 0;

        for (int eg = 0; eg < media.getEpisodeGroups().size(); eg++) {
            media.getEpisodes().add(new ArrayList<>());     // Adds each episode group to episodes list

            Element episodesDiv = episodeGroupsWebsiteCode.get(eg).getElementsByClass("list detail eplist").first();
            while(media.getEpisodes().get(eg).size() < episodesDiv.children().size()) {      // While number of episodes in episodes list is less than episodes from div
                media.getEpisodes().get(eg).add(new Episode(media));
                nrEpisodes++;
            }
        }

        media.setNrEpisodes(nrEpisodes);
    }

    private void fetchEpisodeInfo() {
        // Loop through each episode group
        for (int eg = 0; eg < media.getEpisodeGroups().size(); eg++) {

            // Loop through each episode in the episode group
            Element episodesDiv = episodeGroupsWebsiteCode.get(eg).getElementsByClass("list detail eplist").first();
            for(int e = 0; e < episodesDiv.children().size(); e++) {

                // SET EPISODE GROUP AND EPISODE NUMBER
                media.getEpisodes().get(eg).get(e).setEpisodeGroupNr(eg+1);
                media.getEpisodes().get(eg).get(e).setEpisodeNr(e+1);

                // URLs
                String url = episodesDiv.child(e).select("strong").select("a").attr("abs:href");
                media.getEpisodes().get(eg).get(e).setUrl(url);

                // NAMES
                String name = episodesDiv.child(e).select("strong").select("a").text();
                media.getEpisodes().get(eg).get(e).setName(name);

                // RATINGS
                Element ratingElement = episodesDiv.child(e).getElementsByClass("ipl-rating-star__rating").first();
                String rating;
                if(ratingElement == null) {                                 // If no rating exists
                    rating = "NA";
                }
                else if(ratingElement.text().equals("0")) {                 // If rating exists but at the same time doesn't? (ratings can be "0" even if they don't exist)
                    rating = "NA";
                }
                else {
                    rating = ratingElement.text().substring(0, 3);          // Cuts off excess rating information
                }

                media.getEpisodes().get(eg).get(e).setRating(rating);        // Adds rating to list
            }
        }
    }
}
