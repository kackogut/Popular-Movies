package com.kacper.popularmovies.listeners;

import java.util.ArrayList;

/**
 * Created by kacper on 04/02/2017.
 */

public interface ThreadToUIListener {
    void showMovieData();
    void showErrorMessage(String message);
    void showProgressBar(boolean showOrNot);
    void setMoviesToAdapter(ArrayList movies);

}
