package com.kacper.popularmovies.data.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{

    private String title;
    private String overwiew;
    private String averageRating;
    private String airDate;
    private String imageURI;
    private String movieId;

    public Movie(String title, String overwiew, String averageRating, String airDate, String imageURI, String movieId) {
        this.title = title;
        this.overwiew = overwiew;
        this.averageRating = averageRating;
        this.airDate = airDate;
        this.imageURI = imageURI;
        this.movieId = movieId;
    }
    public Movie(Parcel input){
        String []array=new String[6];
        input.readStringArray(array);
        this.title=array[0];
        this.overwiew=array[1];
        this.averageRating=array[2];
        this.airDate=array[3];
        this.imageURI=array[4];
        this.movieId=array[5];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.title,this.overwiew,this.averageRating,this.airDate,this.imageURI, this.movieId});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie (in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public String getMovieId() {
        return movieId;
    }
}
