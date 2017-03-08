package com.kacper.popularmovies.data.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kacper on 11/02/2017.
 */

public class MoviesDBContract {
    public static final String CONTENT_AUTHORITY = "com.kacper.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_OVERWIEW = "overwiew";
        public static final String COLUMN_FAVOURITED = "favourite";
        public static final String COLUMN_POSTER = "poster";
        public static final String MOVIE_SERVER_ID = "id";
        public static final String COLUMN_AIR_DATE = "air";

    }


}
