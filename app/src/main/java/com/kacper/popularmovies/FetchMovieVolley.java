package com.kacper.popularmovies;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kacper.popularmovies.data.Movie;
import com.kacper.popularmovies.utilities.JSONutils;
import com.kacper.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;



public class FetchMovieVolley {
    private ArrayList<Movie> movies;
    private ThreadToUIListener UIListener;
    private Context context;

    public FetchMovieVolley(ThreadToUIListener UIListener, Context context) {
        this.UIListener = UIListener;
        this.context=context;
    }
    public void getRequest(String sortingOrder){
        UIListener.showProgressBar(true);
        URL urlToFech = NetworkUtils.buildBaseURL(sortingOrder);
        movies = new ArrayList<Movie>();
        
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,urlToFech.toString(), null,
                new com.android.volley.Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
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
                        UIListener.showErrorMessage("Insert valid Api Key");
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        requestQueue.add(req);

    }
}
