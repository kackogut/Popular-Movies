package com.kacper.popularmovies.enums;

public enum SortingOrder {
    POPULARITY_DESCENDING("popular"), RATING_DESCENDING("top_rated");
    String shownName;
    SortingOrder(String shownName){
        this.shownName=shownName;
    }
    public String getShownName(){
        return this.shownName;
    }
}
