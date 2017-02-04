package com.kacper.popularmovies.data;


public class Movie {

    String title;
    String overwiew;
    String averageRating;
    String airDate;
    String imageURI;

    public Movie(String title, String overwiew, String averageRating, String airDate, String imageURI) {
        this.title = title;
        this.overwiew = overwiew;
        this.averageRating = averageRating;
        this.airDate = airDate;
        this.imageURI = imageURI;
    }

    public String getTitle() {
        return title;
    }

    public String getOverwiew() {
        return overwiew;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public String getAirDate() {
        return airDate;
    }

    public String getImageURI() {
        return imageURI;
    }
}
