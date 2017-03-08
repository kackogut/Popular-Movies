package com.kacper.popularmovies.listeners;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kacper on 21/02/2017.
 */

public interface DetailedMovieListener {
    void setTrailersURLs(ArrayList<String> trailers);
    void setReviews(HashMap<String ,String>reviews);
}
