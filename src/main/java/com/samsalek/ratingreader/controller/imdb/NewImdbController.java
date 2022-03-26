package com.samsalek.ratingreader.controller.imdb;

import com.samsalek.ratingreader.model.MediaType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewImdbController implements IImdbController {

    private Document mainWebsiteCode;

    public NewImdbController(Document websiteCode) {
        this.mainWebsiteCode = websiteCode;
    }

    @Override
    public String fetchMediaName() {
        String c = "sc-94726ce4-0 cMYixt";
        c = "." + c.replaceAll("\\s+", ".");

        Elements elements = mainWebsiteCode.select(c);
        String name = elements.first().child(0).child(0).text();
        return name;
    }

    @Override
    public MediaType fetchMediaType() {
        String c = "ipc-inline-list ipc-inline-list--show-dividers sc-52284603-0 blbaZJ baseAlt";
        c = "." + c.replaceAll("\\s+", ".");

        Elements elements = mainWebsiteCode.select(c);
        String type = elements.first().child(0).text();

        // Sets correct mediaType
        MediaType mediaType;
        if(type.equalsIgnoreCase("TV Series")) {
            mediaType = MediaType.SERIES;
        }
        else if(type.equalsIgnoreCase("TV Mini Series")) {
            mediaType = MediaType.MINISERIES;
        }
        else if(type.contains("Video Game")) {
            mediaType = MediaType.VIDEOGAME;
        }
        else {
            mediaType = MediaType.MOVIE;
        }

        return mediaType;
    }

    @Override
    public String fetchMediaCategory() {
        String c = "ipc-chip-list sc-16ede01-4 bMBIRz";
        c = "." + c.replaceAll("\\s+", ".");

        Elements elements = mainWebsiteCode.select(c);
        Element categories = elements.first();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < categories.children().size(); i++) {
            sb.append(categories.children().get(i).text());
            if(i < categories.children().size()-1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    @Override
    public int fetchMediaReleaseYear() {
        String c = "sc-52284603-2 iTRONr";
        c = "." + c.replaceAll("\\s+", ".");

        Elements elements = mainWebsiteCode.select(c);
        String releaseYear = elements.first().text();

        if(releaseYear.length() > 4) {
            releaseYear = releaseYear.substring(0, 4);
        }

        // If no release year is found
        if(releaseYear.equals("") || releaseYear == null) {
            return -1;
        }

        return Integer.parseInt(releaseYear);
    }

    @Override
    public String fetchMediaLength() {
        String c = "ipc-inline-list ipc-inline-list--show-dividers sc-52284603-0 blbaZJ baseAlt";
        c = "." + c.replaceAll("\\s+", ".");

        Elements elements = mainWebsiteCode.select(c);
        String length = elements.first().child(elements.first().childNodeSize() - 1).text();

        if(Character.isDigit(length.charAt(0)) && Character.isLetter(length.charAt(length.length() - 1))) {
            return length;
        } else {
            return "";
        }
    }
}
