package com.kacper.popularmovies.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kacper.popularmovies.data.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public final class JSONutils {
    private static String TITLE = "original_title";
    private static String POSTER = "poster_path";
    private static String INFO = "overview";
    private static String AVG_VOTING = "vote_average";
    private static String AIR_DATE = "release_date";
    private static String MOVIE_ID = "id";

    private JSONutils() {
        throw new AssertionError();
    }

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
                                        movie.getString(POSTER),
                                        movie.getString(MOVIE_ID)));
        }
        return tmpReturnList;
    }
    public static ArrayList<String> getAllTrailersURL(String trailerRequest) throws JSONException{
        JSONArray JSONTrailers =null;
        JSONTrailers = new JSONObject(trailerRequest).getJSONArray("results");
        ArrayList<String>tmpResturnList = new ArrayList<>();

        for(int i=0; i<JSONTrailers.length();i++){
            JSONObject trailer = (JSONObject) JSONTrailers.get(i);
            if(trailer.getString("site").equals("YouTube")){
                tmpResturnList.add("https://www.youtube.com/watch?v="+trailer.getString("key"));
            }

        }return tmpResturnList;
    }
    public static HashMap<String,String> getAllReviews(String movieRequest) throws JSONException{
        JSONArray JSONreviews = null;
        JSONreviews = new JSONObject(movieRequest).getJSONArray("results");
        HashMap<String,String>tmp = new HashMap<>();
        for(int i=0; i<JSONreviews.length();i++){
            JSONObject review = (JSONObject) JSONreviews.get(i);
            tmp.put(review.getString("author"), review.getString("content"));
        }return tmp;
    }

}
