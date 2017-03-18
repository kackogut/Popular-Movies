package com.kacper.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kacper on 01/02/2017.
 */


public final class NetworkUtils {
    //TODO Insert your API key here
    private final static String DATABASE_API_KEY = "";
    private final static String URI_PARAM_API_KEY = "api_key";

    private final static String BASE_URL = "http://api.themoviedb.org/3/movie";
    private final static String IMAGE_URL = "http://image.tmdb.org/t/p/";

    private final static String URI_PARAM_PAGE = "page";
    private final static String PAGE_TO_LOAD = "1";

    public final static String FAVOURITED_MOVIES = "favourited";
    public final static String SORTING_POPULARITY = "popular";
    public final static String SORTING_RATING ="top_rated";

    public final static String MEDIUM_SIZE_POSTER = "w500";

    private final static String VIDEO = "videos";

    private final static String REVIEWS = "reviews";


    public static URL buildBaseURL(String sortBy){
        Uri uriToUrl = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(URI_PARAM_API_KEY,DATABASE_API_KEY)
                .appendQueryParameter(URI_PARAM_PAGE,PAGE_TO_LOAD).build();
        return getURLfromUri(uriToUrl);

    }
    public static URL buildImageURL(String imageAdress, String imageSize){
        Uri uriToUrl = Uri.parse(IMAGE_URL).buildUpon()
                .appendPath(imageSize)
                .appendEncodedPath(imageAdress)
                .build();
        return getURLfromUri(uriToUrl);
    }


    public static URL buildTrailerURI(String imageID){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(imageID)
                .appendPath(VIDEO)
                .appendQueryParameter(URI_PARAM_API_KEY,DATABASE_API_KEY).build();
        return getURLfromUri(uri);
    }

    public static URL buildReviewsURI(String movieID){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieID)
                .appendPath(REVIEWS)
                .appendQueryParameter(URI_PARAM_API_KEY,DATABASE_API_KEY).build();
        return getURLfromUri(uri);

    }
    private static URL getURLfromUri(Uri uri){
        URL buildedURL = null;
        try {
            buildedURL = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return buildedURL;
    }



}
