package com.kacper.popularmovies;

import android.os.AsyncTask;
import android.view.View;

import com.kacper.popularmovies.data.Movie;
import com.kacper.popularmovies.enums.SortingOrder;
import com.kacper.popularmovies.utilities.JSONutils;
import com.kacper.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kacper on 04/02/2017.
 */

public class FetchMovieTask extends AsyncTask<SortingOrder, Void, ArrayList<Movie>> {

    private ThreadToUIListener UIListener;
    boolean apiWrong ;

    public FetchMovieTask(ThreadToUIListener UIListener) {
        this.UIListener = UIListener;
        apiWrong=false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        UIListener.showProgressBar(false);
    }
    @Override
    protected ArrayList<Movie> doInBackground(SortingOrder... params) {
        if(params.length == 0)
            return null;
        SortingOrder order = params[0];
        URL requestURL = NetworkUtils.buildBaseURL(order);

        try {
            String jsonMovieResponse = NetworkUtils.getResponseFromURL(requestURL);
            ArrayList<Movie> tmpReturnMovies = JSONutils.getAllMovies(jsonMovieResponse);
            return tmpReturnMovies;
        } catch (IOException e) {
            apiWrong=true;
        } catch (JSONException e) {
            e.printStackTrace();
        }return null;
    }
    @Override
    protected void onPostExecute(ArrayList<Movie>movies) {
        UIListener.showProgressBar(false);
        if (movies != null) {
            UIListener.showMovieData();
            UIListener.setMoviesToAdapter(movies);
        } else {
            if(apiWrong){
                UIListener.showErrorMessage("Wrong API Key");
            }
            UIListener.showErrorMessage(null);
        }
    }
}

