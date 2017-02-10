package com.kacper.popularmovies;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kacper.popularmovies.data.Movie;
import com.kacper.popularmovies.enums.SortingOrder;
import com.kacper.popularmovies.utilities.JSONutils;
import com.kacper.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;


public class FetchMovieVolley {
    private ArrayList<Movie> movies;
    private ThreadToUIListener UIListener;

    public FetchMovieVolley(ThreadToUIListener UIListener) {
        this.UIListener = UIListener;
    }
    public void getRequest(SortingOrder sortingOrder, Context context){
        UIListener.showProgressBar(true);
        URL urlToFech = NetworkUtils.buildBaseURL(sortingOrder);
        movies = new ArrayList<Movie>();


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,urlToFech.toString(),null,
                new com.android.volley.Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("dhsvajuf",response.toString());
                        try {
                            movies = JSONutils.getAllMovies(response.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        UIListener.showProgressBar(false);
                        UIListener.setMoviesToAdapter(movies);
                        UIListener.showMovieData();
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        UIListener.showProgressBar(false);
                        UIListener.showErrorMessage("Error");
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        requestQueue.add(req);
    }
}
