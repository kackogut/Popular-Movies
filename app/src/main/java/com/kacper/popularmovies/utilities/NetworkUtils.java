package com.kacper.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.kacper.popularmovies.enums.ImageSizes;
import com.kacper.popularmovies.enums.SortingOrder;

import org.json.JSONArray;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

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


    public static URL buildBaseURL(SortingOrder sortBy){
        Uri uriToUrl = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortBy.getShownName())
                .appendQueryParameter(URI_PARAM_API_KEY,DATABASE_API_KEY)
                .appendQueryParameter(URI_PARAM_PAGE,PAGE_TO_LOAD).build();
        Log.v("URL ",uriToUrl.toString());
        return getURLfromUri(uriToUrl);

    }
    public static URL buildImageURL(String imageAdress, ImageSizes imageSize){
        Uri uriToUrl = Uri.parse(IMAGE_URL).buildUpon()
                .appendPath(imageSize.getJSONsize())
                .appendEncodedPath(imageAdress)
                .build();
        return getURLfromUri(uriToUrl);
    }
    public static String getResponseFromURL(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
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
