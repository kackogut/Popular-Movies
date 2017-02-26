package com.kacper.popularmovies;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kacper.popularmovies.data.model.Movie;
import com.kacper.popularmovies.utilities.JSONutils;
import com.kacper.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class FetchMovieVolley {
    private ArrayList<Movie> movies;
    private ArrayList<String> trailerURL;
    private HashMap<String,String> reviews;

    private ThreadToUIListener UIListener;
    private DetailedMovieListener detailedMovieListener;
    private Context context;

    public FetchMovieVolley(ThreadToUIListener UIListener, Context context) {
        this.UIListener = UIListener;
        this.context=context;
    }

    public FetchMovieVolley(Context context, DetailedMovieListener detailedMovieListener) {
        this.context = context;
        this.detailedMovieListener = detailedMovieListener;
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
    public void reviewRequest(String movieID){
        URL url = NetworkUtils.buildReviewsURI(movieID);
        reviews = new HashMap<>();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            reviews= JSONutils.getAllReviews(response.toString());
                            detailedMovieListener.setReviews(reviews);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);

    }
    public void trailersRequest(String movieID){
        URL url = NetworkUtils.buildTrailerURI(movieID);
        trailerURL = new ArrayList<>();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            trailerURL = JSONutils.getAllTrailersURL(response.toString());
                            detailedMovieListener.setTrailersURLs(trailerURL);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);
    }

}
