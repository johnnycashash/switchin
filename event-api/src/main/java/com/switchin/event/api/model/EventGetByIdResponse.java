package com.switchin.event.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class EventGetByIdResponse implements Serializable {

    private static final long serialVersionUID = 7621071075786169611L;

    @JsonProperty(value = "id")
    private final String id;

    @JsonProperty(value = "author")
    private final String author;

    @JsonProperty(value = "country")
    private final String country;

    @JsonProperty(value = "image_link")
    private final String imageLink;

    @JsonProperty(value = "language")
    private final String language;

    @JsonProperty(value = "link")
    private final String link;

    @JsonProperty(value = "pages")
    private final Integer pages;

    @JsonProperty(value = "title")
    private final String title;

    @JsonProperty(value = "year")
    private final Integer year;

    public EventGetByIdResponse(Event event) {
        this.id = event.getId();
        this.author = event.getAuthor();
        this.country = event.getCountry();
        this.imageLink = event.getImageLink();
        this.language = event.getLanguage();
        this.link = event.getLink();
        this.pages = event.getPages();
        this.title = event.getTitle();
        this.year = event.getYear();
    }

    public EventGetByIdResponse(String id, Event event) {
        this.id = id;
        this.author = event.getAuthor();
        this.country = event.getCountry();
        this.imageLink = event.getImageLink();
        this.language = event.getLanguage();
        this.link = event.getLink();
        this.pages = event.getPages();
        this.title = event.getTitle();
        this.year = event.getYear();
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getCountry() {
        return country;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getLanguage() {
        return language;
    }

    public String getLink() {
        return link;
    }

    public Integer getPages() {
        return pages;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventGetByIdResponse that = (EventGetByIdResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EventGetByIdResponse{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", country='" + country + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", language='" + language + '\'' +
                ", link='" + link + '\'' +
                ", pages=" + pages +
                ", title='" + title + '\'' +
                ", year=" + year +
                '}';
    }

}
