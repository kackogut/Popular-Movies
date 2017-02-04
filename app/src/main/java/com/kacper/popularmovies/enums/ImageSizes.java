package com.kacper.popularmovies.enums;


public enum ImageSizes {
    SMALL("w342"),MEDIUM("w500"),BIG("w780"),ORGINAL("ogrinal");
    String JSONsize;
    ImageSizes(String JSONsize){
        this.JSONsize=JSONsize;
    }
    public String getJSONsize(){
        return this.JSONsize;
    }
}
