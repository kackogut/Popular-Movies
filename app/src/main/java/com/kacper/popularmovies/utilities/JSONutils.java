package com.kacper.popularmovies.utilities;

import com.kacper.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JSONutils {
    private static String TITLE = "original_title";
    private static String POSTER = "poster_path";
    private static String INFO = "overview";
    private static String AVG_VOTING = "vote_average";
    private static String AIR_DATE = "release_date";

    public static ArrayList<Movie> getAllMovies(String pageJSON) throws JSONException{
        JSONArray JSONMovies =null;
        JSONMovies = new JSONObject(pageJSON).getJSONArray("results");

        ArrayList<Movie>tmpReturnList =  new ArrayList<Movie>();
        for(int i=0;i<JSONMovies.length();i++){
            JSONObject movie = (JSONObject) JSONMovies.get(i);
            tmpReturnList.add(new Movie(movie.getString(TITLE),
                                        movie.getString(INFO),
                                        movie.getString(AVG_VOTING),
                                        movie.getString(AIR_DATE),
                                        movie.getString(POSTER)));

        }
        return tmpReturnList;
    }
}
