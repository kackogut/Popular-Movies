package com.kacper.popularmovies;

import android.app.DownloadManager;
import android.net.Uri;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kacper.popularmovies.data.Movie;
import com.kacper.popularmovies.enums.SortingOrder;
import com.kacper.popularmovies.utilities.JSONutils;
import com.kacper.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;



public class FetchMovieVolley {
    private ArrayList<Movie> movies;
    private ThreadToUIListener UIListener;

    public FetchMovieVolley(ThreadToUIListener UIListener) {
        this.UIListener = UIListener;
    }
    public void getRequest(SortingOrder sortingOrder){
        UIListener.showProgressBar(true);
        URL urlToFech = NetworkUtils.buildBaseURL(sortingOrder);
        movies = new ArrayList<Movie>();
        
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlToFech.toString(),
                new com.android.volley.Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            movies = JSONutils.getAllMovies(response.toString());
                            UIListener.showProgressBar(false);
                            UIListener.setMoviesToAdapter(movies);
                            UIListener.showMovieData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        UIListener.showProgressBar(false);
                        UIListener.showErrorMessage("Error");
                    }
                });
    }
}
