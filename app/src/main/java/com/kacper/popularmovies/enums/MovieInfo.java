package com.kacper.popularmovies.enums;

public enum MovieInfo {
    TITLE("original_title"),POSTER("poster_path"),INFO("overview"),AVG_VOTING("vote_average"),AIR_DATE("release_date");
    String JSONequalivent;
    MovieInfo(String JSONequalivent){
        this.JSONequalivent=JSONequalivent;
    }
    public String getJSONequalivent(){
        return this.JSONequalivent;
    }
}
