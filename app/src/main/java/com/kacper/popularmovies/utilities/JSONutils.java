package com.kacper.popularmovies.utilities;

import com.kacper.popularmovies.data.Movie;
import com.kacper.popularmovies.enums.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JSONutils {

    public static ArrayList<Movie> getAllMovies(String pageJSON) throws JSONException{
        JSONArray JSONMovies =null;
        JSONMovies = (JSONArray)new JSONObject(pageJSON).getJSONArray("results");

        ArrayList<Movie>tmpReturnList =  new ArrayList<Movie>();
        for(int i=0;i<JSONMovies.length();i++){
            JSONObject movie = (JSONObject) JSONMovies.get(i);
            tmpReturnList.add(new Movie(movie.getString(MovieInfo.TITLE.getJSONequalivent()),
                                        movie.getString(MovieInfo.INFO.getJSONequalivent()),
                                        movie.getString(MovieInfo.AVG_VOTING.getJSONequalivent()),
                                        movie.getString(MovieInfo.AIR_DATE.getJSONequalivent()),
                                        movie.getString(MovieInfo.POSTER.getJSONequalivent())));

        }
        return tmpReturnList;
    }
}
