package com.kacper.popularmovies.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.kacper.popularmovies.data.model.Movie;
import com.kacper.popularmovies.data.provider.MoviesDBContract;

/**
 * Created by kacper on 18/03/2017.
 */

public class AddingToDatabaseService extends IntentService {


    public AddingToDatabaseService() {
        super("AddMovieToDatabase");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Movie mMovie = intent.getParcelableExtra("movie");
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesDBContract.MovieEntry.MOVIE_SERVER_ID, mMovie.getMovieId());
        contentValues.put(MoviesDBContract.MovieEntry.COLUMN_OVERWIEW, mMovie.getOverwiew());
        contentValues.put(MoviesDBContract.MovieEntry.COLUMN_POSTER, mMovie.getImageURI());
        contentValues.put(MoviesDBContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
        contentValues.put(MoviesDBContract.MovieEntry.COLUMN_RATING, mMovie.getAverageRating());
        contentValues.put(MoviesDBContract.MovieEntry.COLUMN_AIR_DATE, mMovie.getAirDate());
        Log.v("dsadsa",getApplicationContext().getContentResolver().insert(MoviesDBContract.MovieEntry.CONTENT_URI, contentValues).toString());
    }
}
