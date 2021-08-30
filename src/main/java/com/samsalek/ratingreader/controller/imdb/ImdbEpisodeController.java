package com.samsalek.ratingreader.controller.imdb;

import com.samsalek.ratingreader.model.Episode;
import com.samsalek.ratingreader.model.EpisodeGroupType;
import com.samsalek.ratingreader.model.Media;
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
        media.setWorstEpisodes(null);
        media.setBestEpisodes(null);

        // Loop through each episode group
        for (int eg = 0; eg < media.getEpisodeGroups().size(); eg++) {

            // Loop through each episode in the episode group
            Element episodesDiv = episodeGroupsWebsiteCode.get(eg).getElementsByClass("list detail eplist").first();
            for(int e = 0; e < episodesDiv.children().size(); e++) {

                Episode episode = media.getEpisodes().get(eg).get(e);

                // SET EPISODE GROUP AND EPISODE NUMBER
                episode.setEpisodeGroupNr(eg+1);
                episode.setEpisodeNr(e+1);

                fetchUrl(episode, episodesDiv, e);
                fetchNames(episode, episodesDiv, e);
                fetchRatings(episode, episodesDiv, e);
                fetchAirdate(episode, episodesDiv, e);
                fetchNrRatings(episode, episodesDiv, e);

                if(!episode.getRating().equals("NA")) {
                    setWorstAndBestEpisodes(episode);
                }
            }
        }
    }

    private void fetchUrl(Episode episode, Element episodesDiv, int e){
        String url = episodesDiv.child(e).select("strong").select("a").attr("abs:href");
        episode.setUrl(url);
    }

    private void fetchNames(Episode episode, Element episodesDiv, int e){
        String name = episodesDiv.child(e).select("strong").select("a").text();
        episode.setName(name);
    }

    private void fetchRatings(Episode episode, Element episodesDiv, int e){
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
        episode.setRating(rating);        // Adds rating to list
    }

    private void fetchAirdate(Episode episode, Element episodesDiv, int e) {
        String airdate = episodesDiv.child(e).getElementsByClass("airdate").first().text();
        episode.setAirdate(airdate);
    }

    private void fetchNrRatings(Episode episode, Element episodesDiv, int e) {
        Element nrRatingsElement = episodesDiv.child(e).getElementsByClass("ipl-rating-star__total-votes").first();
        if(nrRatingsElement != null) {
            String nrRatings = nrRatingsElement.text();
            nrRatings = nrRatings.replaceAll("[()]", "").replaceAll(",", " ");      // Remove parentheses and replace ',' with space
            episode.setNrRatings(nrRatings);
        } else {
            episode.setNrRatings("0");
        }
    }

    private void setWorstAndBestEpisodes(Episode episode) {
        // Worst rated episodes
        if(media.getWorstEpisodes() == null) {
            ArrayList<Episode> episodes = new ArrayList<>();
            episodes.add(episode);
            media.setWorstEpisodes(episodes);
        } else {
            for (Episode ep : media.getWorstEpisodes()) {
                if(Math.abs(Double.parseDouble(episode.getRating()) - Double.parseDouble(ep.getRating())) < 0.01) {
                    media.getWorstEpisodes().add(episode);
                    break;
                } else if(Double.parseDouble(episode.getRating()) < Double.parseDouble(ep.getRating())) {
                    media.getWorstEpisodes().clear();
                    media.getWorstEpisodes().add(episode);
                    break;
                }
            }
        }

        // Best rated episodes
        if(media.getBestEpisodes() == null) {
            ArrayList<Episode> episodes = new ArrayList<>();
            episodes.add(episode);
            media.setBestEpisodes(episodes);
        } else {
            for (Episode ep : media.getBestEpisodes()) {
                if(Math.abs(Double.parseDouble(episode.getRating()) - Double.parseDouble(ep.getRating())) < 0.01) {
                    media.getBestEpisodes().add(episode);
                    break;
                } else if(Double.parseDouble(episode.getRating()) > Double.parseDouble(ep.getRating())) {
                    media.getBestEpisodes().clear();
                    media.getBestEpisodes().add(episode);
                    break;
                }
            }
        }
    }
}
